package net.zetamc.toomanychests.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.zetamc.toomanychests.blocks.*;
import net.zetamc.toomanychests.util.Reference;

public class ModBlocks {
	public static final List<Block> BLOCKS = new ArrayList<Block> ();
	
	//public static final BlockBase TESTBLOCK = new BlockBase ("testblock", Material.AIR);
	
	// Normal chests
	public static final ChestBase STONE_CHEST = new ChestBase ("chest_stone", Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 64, false, "pickaxe", 2.5f, 6, true);
	public static final ChestBase NETHER_CHEST = new ChestBase ("chest_netherbrick", Material.ROCK, Reference.GUI_CUSTOM_CHEST, 36, 32, false, "pickaxe", 2, 2, true);
	public static final ChestBase OBSIDIAN_CHEST = new ChestBase ("chest_obsidian", Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 64, false, "pickaxe", 50, 1200, true);
	public static final ChestBase BONE_CHEST = new ChestBase ("chest_bone", Material.GOURD, Reference.GUI_CUSTOM_CHEST, 18, 64, false, "pickaxe", 2, 2, true);
	public static final ChestBase BEDROCK_CHEST = new ChestBase ("chest_bedrock", Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 64, false, "pickaxe", 99, 3600000, false);
	public static final ChestBase BOOK_CHEST = new ChestBase ("chest_bookcase", Material.WOOD, Reference.GUI_CUSTOM_CHEST, 18, 64, false, "axe", 1.5f, 1.5f, true);
	public static final ChestBase BRICK_CHEST = new ChestBase ("chest_brick", Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 64, false, "pickaxe", 2, 6, false);
	public static final ChestBase CACTUS_CHEST = new ChestBase ("chest_cactus", Material.CLOTH, Reference.GUI_CUSTOM_CHEST, 18, 32, false, "none", 0.4f, 0.4f, true);
	public static final ChestBase CAKE_CHEST = new ChestBase ("chest_cake", Material.CLOTH, Reference.GUI_CUSTOM_CHEST, 45, 128, false, "none", 0.5f, 0.5f, false);
	public static final ChestBase CLAY_CHEST = new ChestBase ("chest_clay", Material.CLAY, Reference.GUI_CUSTOM_CHEST, 27, 64, false, "none", 0.6f, 0.6f, false);
	public static final ChestBase COAL_CHEST = new ChestBase ("chest_coal", Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 64, false, "pickaxe", 5, 6, true);
	public static final ChestBase EMERALD_CHEST = new ChestBase ("chest_emerald", Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 96, false, "pickaxe", 5, 6, false);
	public static final ChestBase ENDBRICK_CHEST = new ChestBase ("chest_end_brick", Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 96, false, "pickaxe", 3, 9, true);
	public static final ChestBase GLASS_CHEST = new ChestBase ("chest_glass", Material.GLASS, Reference.GUI_CUSTOM_CHEST, 18, 64, false, "none", 0.3f, 0.3f, false);
	public static final ChestBase GLOWSTONE_CHEST = new GlowingChest ("chest_glowstone", Material.GLASS, Reference.GUI_CUSTOM_CHEST, 18, 64, false, "none", 0.3f, 0.3f, false, 1);
	public static final ChestBase GRASS_CHEST = new ChestBase ("chest_grass", Material.GRASS, Reference.GUI_CUSTOM_CHEST, 18, 64, false, "none", 0.6f, 0.6f, false);
	public static final ChestBase ICE_CHEST = new ChestBase ("chest_ice", Material.GLASS, Reference.GUI_CUSTOM_CHEST, 27, 64, false, "none", 5, 6, true);
	public static final ChestBase PODZOL_CHEST = new ChestBase ("chest_podzol", Material.GRASS, Reference.GUI_CUSTOM_CHEST, 18, 64, false, "none", 0.5f, 0.5f, false);
	
	// Trapped chests
	public static final ChestBase STONE_CHEST_TRAP = new ChestBase ("chest_stone_trapped", Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 64, true, "pickaxe", 2.5f, 6, true);
}
