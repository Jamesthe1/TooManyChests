package net.zetamc.toomanychests.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.zetamc.toomanychests.Main;

import net.zetamc.toomanychests.init.*;
import net.zetamc.toomanychests.util.IModelLoad;

public class BlockBase extends Block implements IModelLoad {
	public BlockBase (String name, Material material) {
		super (material);
		setTranslationKey (name);
		setRegistryName (name);
		setCreativeTab (CreativeTabs.MISC);
		
		ModBlocks.BLOCKS.add (this);
		ModItems.ITEMS.add (new ItemBlock (this).setRegistryName (this.getRegistryName ()));
	}

	@Override
	public void registerModels () {
		Main.proxy.registerItemRenderer (Item.getItemFromBlock (this), 0, "inventory");
	}
}
