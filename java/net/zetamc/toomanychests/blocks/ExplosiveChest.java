package net.zetamc.toomanychests.blocks;

import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Explosion;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.zetamc.toomanychests.blocks.tileentity.TileEntityCustomChest;
import net.zetamc.toomanychests.blocks.tileentity.TileEntityExplosiveChest;

public class ExplosiveChest extends ChestBase {
	public int maxTime;
	
	public ExplosiveChest (String name, SoundType soundType, Material material, int guiId, int invSize, int stackSize, boolean isTrapped, String tool, float hardness, float blastResistance, boolean canBeDouble, int maxTime) {
		super (name, soundType, material, guiId, invSize, stackSize, isTrapped, tool, hardness, blastResistance, canBeDouble);
		this.maxTime = maxTime;
	}
	
	public void explode (World world, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
		if (!world.isRemote)
			world.playSound ((EntityPlayer)null, pos.getX (), pos.getY (), pos.getZ (), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1, 0.85f);
		else {
			world.spawnParticle (EnumParticleTypes.EXPLOSION_LARGE, pos.getX () + 0.5f, pos.getY () + 0.5f, pos.getZ () + 0.5f, 0, 0, 0);
			world.spawnParticle (EnumParticleTypes.EXPLOSION_NORMAL, pos.getX () + 0.5f, pos.getY () + 0.5f, pos.getZ () + 0.5f, 0, 0, 0);
		}
		
		world.setBlockToAir (pos);
	}
	
	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem (hand);

        if (!itemstack.isEmpty () && (itemstack.getItem () == Items.FLINT_AND_STEEL || itemstack.getItem () == Items.FIRE_CHARGE)) {
            this.explode (world, pos, state, player);

            if (itemstack.getItem () == Items.FLINT_AND_STEEL)
                itemstack.damageItem (1, player);
            else if (!player.capabilities.isCreativeMode)
                itemstack.shrink (1);
            
            return true;
        }
		
		if (!world.isRemote) {
			ILockableContainer ilockablecontainer = this.getLockableContainer (world, pos);
			
			if (ilockablecontainer != null) {
				TileEntityExplosiveChest tntchest = (TileEntityExplosiveChest)ilockablecontainer;
				player.displayGUIChest (tntchest);
				if (tntchest.isLocked () && !player.canOpen (tntchest.getLockCode ()) && player instanceof EntityPlayerMP) {
					((EntityPlayerMP)player).connection.sendPacket (new SPacketChat (new TextComponentTranslation ("container.timewarn", String.valueOf ((int)Math.ceil ((float)tntchest.getFuseTimer ()/20)), ChatType.CHAT)));
					if (!tntchest.isFused ()) {
						world.playSound ((EntityPlayer)null, pos, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1, 1);
						tntchest.fuse ();
					}
				} else if (tntchest.isFused ()) {
					((EntityPlayerMP)player).connection.sendPacket (new SPacketChat (new TextComponentTranslation ("container.timeclear", ChatType.CHAT)));
					world.playSound ((EntityPlayer)null, pos, SoundEvents.BLOCK_NOTE_PLING, SoundCategory.BLOCKS, 1, 1);
					tntchest.defuse ();
				}
				//player.openGui (Main.instance, guiId, world, pos.getX (), pos.getY (), pos.getZ ());
				
				if (chestType == BlockChest.Type.BASIC)
					player.addStat (StatList.CHEST_OPENED);
				else
					player.addStat (StatList.TRAPPED_CHEST_TRIGGERED);
			}
		}
		
		return true;
	}
	
	// Overridden because we don't want the chest to drop items
	@Override
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		this.explode (world, pos, state, (EntityLivingBase)null);
		world.removeTileEntity (pos);
	}
	
	public boolean canDropFromExplosion (Explosion explosionIn) {
        return false;
    }
	
	@Override
	public TileEntity createTileEntity (World world, IBlockState state) {
		return new TileEntityExplosiveChest (invSize, stackSize, regName, canBeDouble, maxTime*20);
	}
}
