package net.zetamc.toomanychests.util.handlers;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import net.zetamc.toomanychests.blocks.tileentity.TileEntityCustomChest;
import net.zetamc.toomanychests.util.Reference;

public class TileEntityHandler {
	public static void registerTileEntities () {
		GameRegistry.registerTileEntity (TileEntityCustomChest.class, new ResourceLocation (Reference.MOD_ID + ":custom_chest"));
	}
}
