package net.zetamc.toomanychests.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.zetamc.toomanychests.items.ItemBase;

public class ModItems {
	public static final List<Item> ITEMS = new ArrayList<Item> ();
	
	//public static final ItemBase TESTITEM = new ItemBase ("testitem", CreativeTabs.MISC);
	
	public static final CreativeTabs TAB_CHESTS = new CreativeTabs ("tab_tmchests") {
		@Override
		public ItemStack createIcon () {
			return new ItemStack (Blocks.ENDER_CHEST);
		}
	};
}