package net.zetamc.toomanychests.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zetamc.toomanychests.Main;
import net.zetamc.toomanychests.util.Reference;

public class Padlock extends ItemBase {
	public Padlock () {
		super ("padlock", CreativeTabs.TOOLS);
		this.maxStackSize = 1;
	}
	
	@Override
	public EnumActionResult onItemUseFirst (EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand) {
		//pos = pos.offset (facing);
        ItemStack itemstack = player.getHeldItem (hand);
        IBlockState contactState = world.getBlockState (pos);
        Block contact = contactState.getBlock ();
        
        if (contact.hasTileEntity (contactState)) {
        	TileEntity te = world.getTileEntity (pos);
        	
        	if (te instanceof TileEntityLockable) {
        		player.openGui (Main.instance, Reference.GUI_PADLOCK, world, pos.getX (), pos.getY (), pos.getZ ());
        		return EnumActionResult.SUCCESS;
        	}
        }
        
        return EnumActionResult.PASS;
	}
}
