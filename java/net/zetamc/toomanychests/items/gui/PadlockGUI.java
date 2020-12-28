package net.zetamc.toomanychests.items.gui;

import java.io.IOException;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.*;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.*;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.BiomeEvent.GetWaterColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.zetamc.toomanychests.client.gui.PadlockButton;
import net.zetamc.toomanychests.client.gui.PadlockButton.ColorID;
import net.zetamc.toomanychests.util.Reference;

@SideOnly(Side.CLIENT)
public class PadlockGUI extends GuiScreen {
	private static final ResourceLocation TEXTURE = new ResourceLocation (Reference.MOD_ID + ":textures/gui/container/padlock.png");

    //protected List<PadlockButton> buttonList = Lists.<PadlockButton>newArrayList();
	public int[] inputCode = new int[5];
	public int cursorPos = 0;
	
	private int beginWidth, beginHeight;
	private int scaleX = 176, scaleY = 133;
	
	private int padStartX = 61, padStartY = 133, padPlaceX = 61, padPlaceY = 49, padScaleX = 59, padScaleY = 74;
	private int buttonListX = 42, buttonListY = 49;
	
	boolean unlocked = false;
	
	private TileEntityLockable te;
	private EntityPlayer player;
	private World world;
	
	public PadlockGUI (TileEntityLockable te, EntityPlayer player, World world) {
		this.te = te;
		this.player = player;
		this.world = world;
	}
	
	public void initGui () {
		this.buttonList.clear ();

		beginWidth = (this.width - scaleX) / 2;
		beginHeight = (this.height - scaleY) / 2;
		
		this.buttonList.add (new PadlockButton (10, beginWidth + padPlaceX + 1, beginHeight + padPlaceY + 55, "X", ColorID.RED));
		this.buttonList.add (new PadlockButton (0, beginWidth + padPlaceX + 19, beginHeight + padPlaceY + 55, "0"));
		this.buttonList.add (new PadlockButton (11, beginWidth + padPlaceX + 37, beginHeight + padPlaceY + 55, "C", ColorID.GREEN));
		this.buttonList.add (new PadlockButton (1, beginWidth + padPlaceX + 1, beginHeight + padPlaceY + 37, "1"));
		this.buttonList.add (new PadlockButton (2, beginWidth + padPlaceX + 19, beginHeight + padPlaceY + 37, "2"));
		this.buttonList.add (new PadlockButton (3, beginWidth + padPlaceX + 37, beginHeight + padPlaceY + 37, "3"));
		this.buttonList.add (new PadlockButton (4, beginWidth + padPlaceX + 1, beginHeight + padPlaceY + 19, "4"));
		this.buttonList.add (new PadlockButton (5, beginWidth + padPlaceX + 19, beginHeight + padPlaceY + 19, "5"));
		this.buttonList.add (new PadlockButton (6, beginWidth + padPlaceX + 37, beginHeight + padPlaceY + 19, "6"));
		this.buttonList.add (new PadlockButton (7, beginWidth + padPlaceX + 1, beginHeight + padPlaceY + 1, "7"));
		this.buttonList.add (new PadlockButton (8, beginWidth + padPlaceX + 19, beginHeight + padPlaceY + 1, "8"));
		this.buttonList.add (new PadlockButton (9, beginWidth + padPlaceX + 37, beginHeight + padPlaceY + 1, "9"));
	}
	
	@Override
	protected void actionPerformed (GuiButton button) {
		if (!(button instanceof PadlockButton)) return;
		
		int id = button.id;
		if (id > 9) {
			switch (id) {
				case 10:
					clearCode ();
					break;
				case 11:
					sendCode ();
					break;
			}
			return;
		}
		
		if (cursorPos == 5) return;
		inputCode [cursorPos++] = id;
	}
	
	private void sendCode () {
		IBlockState state = world.getBlockState (te.getPos ());
		NetHandlerPlayClient nethandler = mc.getConnection ();
		
		String strCode = "";
		for (int i = 0; i < cursorPos; i++)
			strCode += String.valueOf (inputCode [i]);
		
		Reference.LOGGER.info (strCode);
		
		LockCode code = new LockCode (strCode);

		Reference.LOGGER.info ("TileEntity " + te.getDisplayName ().getUnformattedText () + " locked: " + te.isLocked ());
		if (te.isLocked () && strCode.equals (te.getLockCode ().getLock ())) {
			world.playSound (player, player.getPosition (), SoundEvents.BLOCK_NOTE_PLING, SoundCategory.BLOCKS, 1, 2);
			unlocked = true;
			for (int i = 0; i < 12; i++)
				buttonList.get (i).visible = false;
		} else if (!te.isLocked ()) {
			te.setLockCode (code);
			world.playSound (player, te.getPos (), SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 1, 1);
			
			if (player instanceof EntityPlayerMP) {
				EntityPlayerMP mpplayer = (EntityPlayerMP)player;
				
				((TileEntityLockable)mpplayer.world.getTileEntity (te.getPos ())).setLockCode (code);
			}
			
			/*
			 * TODO: write packet buffer and sending code
			 */
			mc.player.closeScreen ();
		} else {
			clearCode ();
			tryOpen (strCode);		// Done here to let the block handle the incorrect lock code
		}
	}
	
	public void tryOpen (String strCode) {
		Block block = te.getBlockType ();
		EntityPlayerMP fakePlayer = new EntityPlayerMP (null, null, null, null);
		ItemStack codeHolder = new ItemStack (Items.PAPER);
		fakePlayer.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, codeHolder.setStackDisplayName (strCode));
		// Commented because we need to tell the server what we want; not sure how to do this yet
		//block.onBlockActivated (te.getWorld (), te.getPos (), te.getWorld ().getBlockState (te.getPos ()), fakePlayer, EnumHand.MAIN_HAND, EnumFacing.DOWN, 0, 0, 0);
	}
	
	private void clearCode () {
		inputCode = new int [5];
		cursorPos = 0;
	}
	
	public void drawScreen (int x, int y, float f) {
		drawDefaultBackground ();
		
		beginWidth = (this.width - scaleX) / 2;
		beginHeight = (this.height - scaleY) / 2;
		
		GL11.glColor4f (1, 1, 1, 1);
		this.mc.renderEngine.bindTexture (TEXTURE);

		drawTexturedModalRect (beginWidth, beginHeight, 0, 0, scaleX, scaleY);
		if (!unlocked)
			drawTexturedModalRect (beginWidth + padPlaceX, beginHeight + padPlaceY, padStartX, padStartY, padScaleX, padScaleY);
		
		this.fontRenderer.drawString (te.getDisplayName ().getUnformattedText (), beginWidth + 6, beginHeight + 8, 4210752);
		
		for (int i = 0; i < 5; i++) {
			String num = "_";
			if (i < cursorPos)
				num = String.valueOf (inputCode [i]);
			
			this.fontRenderer.drawString (num, beginWidth + 49 + i*18, beginHeight + 24, 16777215);
		}
		
		drawSpecial (x, y, f);
	}
	
	public void drawSpecial (int mouseX, int mouseY, float partialTicks) {
		for (GuiButton button : buttonList)
			((PadlockButton)button).drawButton (this.mc, mouseX, mouseY, partialTicks);
		
		for (GuiLabel label : labelList)
			label.drawLabel (mc, mouseX, mouseY);
	}
	
	public boolean doesGuiPauseGame () {
        return false;
    }
	
	public void updateScreen () {
        super.updateScreen ();
        
        // This was in the GuiContainer class; not sure why it is written this way but I assume it's in the event of some desync
        boolean playerDied = !this.mc.player.isEntityAlive () || this.mc.player.isDead;
        // Update created to detect if the entity changed or no longer exists
        TileEntity worldTe = world.getTileEntity (te.getPos ());
        boolean blockDestroyed = worldTe != te;
        
        if (playerDied || blockDestroyed) {
            this.mc.player.closeScreen ();
            return;
        }
    }
	
	protected void keyTyped (char typedChar, int keyCode) throws IOException {
		if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches (keyCode))
			this.mc.player.closeScreen ();
	}
}
