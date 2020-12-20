package net.zetamc.toomanychests.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.zetamc.toomanychests.Main;
import net.zetamc.toomanychests.init.ModItems;
import net.zetamc.toomanychests.util.IModelLoad;

public class ItemBase extends Item implements IModelLoad {
	public ItemBase (String name, CreativeTabs tab) {
		setTranslationKey (name);
		setRegistryName (name);
		setCreativeTab (tab);
		
		ModItems.ITEMS.add (this);
	}
	
	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer (this, 0, "inventory");
	}
}
