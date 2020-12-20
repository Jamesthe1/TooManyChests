package net.zetamc.toomanychests.entity.player;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;

public class CustomChestUIFix extends EntityPlayerMP {

	public CustomChestUIFix (MinecraftServer server, WorldServer worldIn, GameProfile profile, PlayerInteractionManager interactionManagerIn) {
		super (server, worldIn, profile, interactionManagerIn);
	}

	public void displayGUIChest (IInventory chestInventory, int guiId) {
        if (chestInventory instanceof ILootContainer && ((ILootContainer)chestInventory).getLootTable () != null && this.isSpectator ())
            this.sendStatusMessage ((new TextComponentTranslation ("container.spectatorCantOpen", new Object[0])).setStyle ((new Style ()).setColor (TextFormatting.RED)), true);
        else {
            if (this.openContainer != this.inventoryContainer)
                this.closeScreen ();

            if (chestInventory instanceof ILockableContainer) {
                ILockableContainer ilockablecontainer = (ILockableContainer)chestInventory;

                if (ilockablecontainer.isLocked () && !this.canOpen(ilockablecontainer.getLockCode ()) && !this.isSpectator ()) {
                    this.connection.sendPacket (new SPacketChat (new TextComponentTranslation ("container.isLocked", new Object[] {chestInventory.getDisplayName ()}), ChatType.GAME_INFO));
                    this.connection.sendPacket (new SPacketSoundEffect (SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, this.posX, this.posY, this.posZ, 1.0F, 1.0F));
                    return;
                }
            }

            this.getNextWindowId ();

            if (chestInventory instanceof IInteractionObject) {
                this.connection.sendPacket (new SPacketOpenWindow (this.currentWindowId, ((IInteractionObject)chestInventory).getGuiID (), chestInventory.getDisplayName (), chestInventory.getSizeInventory ()));
                this.openContainer = ((IInteractionObject)chestInventory).createContainer (this.inventory, this);
            } else {
                this.connection.sendPacket (new SPacketOpenWindow (this.currentWindowId, "minecraft:container", chestInventory.getDisplayName (), chestInventory.getSizeInventory ()));
                this.openContainer = new ContainerChest (this.inventory, chestInventory, this);
            }

            this.openContainer.windowId = this.currentWindowId;
            this.openContainer.addListener (this);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post (new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open (this, this.openContainer));
        }
    }
}
