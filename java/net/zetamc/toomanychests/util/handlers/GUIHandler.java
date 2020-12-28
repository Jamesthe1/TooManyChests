package net.zetamc.toomanychests.util.handlers;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.tileentity.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import net.zetamc.toomanychests.blocks.tileentity.TileEntityCustomChest;
import net.zetamc.toomanychests.items.gui.PadlockGUI;
import net.zetamc.toomanychests.util.Reference;

public class GUIHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			default:
				return null;
			case Reference.GUI_CUSTOM_CHEST:
				return new ContainerChest (player.inventory, (TileEntityLockableLoot)world.getTileEntity (new BlockPos (x, y, z)), player);
		}
	}

	@Override
	public Object getClientGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			default:
				return null;
			case Reference.GUI_CUSTOM_CHEST:
				return new GuiChest (player.inventory, (TileEntityLockableLoot)world.getTileEntity (new BlockPos (x, y, z)));
			case Reference.GUI_PADLOCK:
				return new PadlockGUI ((TileEntityLockable)world.getTileEntity (new BlockPos (x, y, z)), player, world);
		}
	}
}
