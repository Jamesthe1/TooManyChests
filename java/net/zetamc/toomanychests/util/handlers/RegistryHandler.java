package net.zetamc.toomanychests.util.handlers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.zetamc.toomanychests.Main;
import net.zetamc.toomanychests.blocks.tileentity.TileEntityCustomChest;
import net.zetamc.toomanychests.entity.render.RenderCustomChest;
import net.zetamc.toomanychests.init.ModBlocks;
import net.zetamc.toomanychests.init.ModItems;
import net.zetamc.toomanychests.util.IModelLoad;

@EventBusSubscriber
public class RegistryHandler {
	@SubscribeEvent
	public static void onItemRegister (RegistryEvent.Register<Item> event) {
		event.getRegistry ().registerAll (ModItems.ITEMS.toArray (new Item[0]));
	}
	
	@SubscribeEvent
	public static void OnBlockRegister (RegistryEvent.Register <Block> event) {
		event.getRegistry ().registerAll (ModBlocks.BLOCKS.toArray (new Block [0]));
		TileEntityHandler.registerTileEntities ();
		ClientRegistry.bindTileEntitySpecialRenderer (TileEntityCustomChest.class, new RenderCustomChest ());
	}
	
	@SubscribeEvent
	public static void onModelRegister (ModelRegistryEvent event) {
		for (Item item : ModItems.ITEMS)
			if (item instanceof IModelLoad)
				((IModelLoad)item).registerModels ();
		
		for (Block block : ModBlocks.BLOCKS)
			if (block instanceof IModelLoad)
				((IModelLoad)block).registerModels ();
	}
}
