package net.zetamc.toomanychests.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.zetamc.toomanychests.blocks.*;
import net.zetamc.toomanychests.util.Reference;

public class ModBlocks {
	public static final List<Block> BLOCKS = new ArrayList<Block> ();
	
	//public static final BlockBase TESTBLOCK = new BlockBase ("testblock", Material.AIR);
	
	// Normal chests
	public static final ChestBase BONE_CHEST = new ChestBase ("chest_bone", SoundType.STONE, Material.GOURD, Reference.GUI_CUSTOM_CHEST, 18, 64, false, "pickaxe", 2, 2, true);
	public static final ChestBase BEDROCK_CHEST = new ChestBase ("chest_bedrock", SoundType.STONE, Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 64, false, "pickaxe", 99, 3600000, false);
	public static final ChestBase BOOK_CHEST = new ChestBase ("chest_bookcase", SoundType.WOOD, Material.WOOD, Reference.GUI_CUSTOM_CHEST, 18, 64, false, "axe", 1.5f, 1.5f, true);
	public static final ChestBase BRICK_CHEST = new ChestBase ("chest_brick", SoundType.STONE, Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 64, false, "pickaxe", 2, 6, false);
	public static final ChestBase CACTUS_CHEST = new ChestBase ("chest_cactus", SoundType.CLOTH, Material.CLOTH, Reference.GUI_CUSTOM_CHEST, 18, 32, false, "none", 0.4f, 0.4f, true);
	public static final ChestBase CAKE_CHEST = new ChestBase ("chest_cake", SoundType.CLOTH, Material.CLOTH, Reference.GUI_CUSTOM_CHEST, 45, 128, false, "none", 0.5f, 0.5f, false);
	public static final ChestBase CLAY_CHEST = new ChestBase ("chest_clay", SoundType.GROUND, Material.CLAY, Reference.GUI_CUSTOM_CHEST, 27, 64, false, "none", 0.6f, 0.6f, false);
	public static final ChestBase COAL_CHEST = new ChestBase ("chest_coal", SoundType.STONE, Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 64, false, "pickaxe", 5, 6, true);
	public static final ChestBase COCOA_CHEST = new ChestBase ("chest_cocoa", SoundType.WOOD, Material.WOOD, Reference.GUI_CUSTOM_CHEST, 18, 64, false, "none", 0.2f, 3, false);
	public static final ChestBase DIAMOND_CHEST = new ChestBase ("chest_diamond", SoundType.METAL, Material.IRON, Reference.GUI_CUSTOM_CHEST, 54, 64, false, "pickaxe", 5, 6, false);
	public static final ChestBase EMERALD_CHEST = new ChestBase ("chest_emerald", SoundType.METAL, Material.IRON, Reference.GUI_CUSTOM_CHEST, 27, 96, false, "pickaxe", 5, 6, false);
	public static final ChestBase ENCHANTED_CHEST = new ChestBase ("chest_enchanted", SoundType.STONE, Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 1, false, "pickaxe", 5, 1200, true);
	public static final ChestBase ENDBRICK_CHEST = new ChestBase ("chest_end_brick", SoundType.STONE, Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 96, false, "pickaxe", 3, 9, true);
	public static final ChestBase GLASS_CHEST = new ChestBase ("chest_glass", SoundType.GLASS, Material.GLASS, Reference.GUI_CUSTOM_CHEST, 18, 64, false, "none", 0.3f, 0.3f, false);
	public static final ChestBase GLOWSTONE_CHEST = new GlowingChest ("chest_glowstone", SoundType.GLASS, Material.GLASS, Reference.GUI_CUSTOM_CHEST, 36, 64, false, "none", 0.3f, 0.3f, false, 1);
	public static final ChestBase GRASS_CHEST = new ChestBase ("chest_grass", SoundType.PLANT, Material.GRASS, Reference.GUI_CUSTOM_CHEST, 18, 64, false, "none", 0.6f, 0.6f, false);
	public static final ChestBase ICE_CHEST = new ChestBase ("chest_ice", SoundType.METAL, Material.GLASS, Reference.GUI_CUSTOM_CHEST, 27, 64, false, "none", 5, 6, true);
	public static final ChestBase IRON_CHEST = new ChestBase ("chest_iron", SoundType.METAL, Material.IRON, Reference.GUI_CUSTOM_CHEST, 36, 64, false, "pickaxe", 5, 6, false);
	public static final ChestBase JUKEBOX_CHEST = new ChestBase ("chest_jukebox", SoundType.WOOD, Material.WOOD, Reference.GUI_CUSTOM_CHEST, 27, 1, false, "axe", 2, 6, false);
	public static final ChestBase MELON_CHEST = new ChestBase ("chest_melon", SoundType.WOOD, Material.GOURD, Reference.GUI_CUSTOM_CHEST, 27, 32, false, "axe", 1, 1, false);
	public static final ChestBase MYCELIUM_CHEST = new ChestBase ("chest_mycelium", SoundType.PLANT, Material.GRASS, Reference.GUI_CUSTOM_CHEST, 18, 64, false, "none", 0.6f, 0.5f, false);
	public static final ChestBase NETHER_CHEST = new ChestBase ("chest_netherbrick", SoundType.STONE, Material.ROCK, Reference.GUI_CUSTOM_CHEST, 36, 32, false, "pickaxe", 2, 2, true);
	public static final ChestBase OBSIDIAN_CHEST = new ChestBase ("chest_obsidian", SoundType.STONE, Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 64, false, "pickaxe", 50, 1200, true);
	public static final ChestBase PODZOL_CHEST = new ChestBase ("chest_podzol", SoundType.GROUND, Material.GRASS, Reference.GUI_CUSTOM_CHEST, 18, 64, false, "none", 0.5f, 0.5f, false);
	public static final ChestBase STONE_CHEST = new ChestBase ("chest_stone", SoundType.STONE, Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 64, false, "pickaxe", 2.5f, 6, true);
	public static final ChestBase TNT_CHEST = new ExplosiveChest ("chest_tnt", SoundType.PLANT, Material.TNT, Reference.GUI_CUSTOM_CHEST, 27, 64, false, "none", 0.5f, 0, false, 10);
	
	// Trapped chests
	public static final ChestBase NETHER_CHEST_TRAP = new ChestBase ("chest_netherbrick_trapped", SoundType.STONE, Material.ROCK, Reference.GUI_CUSTOM_CHEST, 36, 32, true, "pickaxe", 2, 2, true);
	public static final ChestBase STONE_CHEST_TRAP = new ChestBase ("chest_stone_trapped", SoundType.STONE, Material.ROCK, Reference.GUI_CUSTOM_CHEST, 27, 64, true, "pickaxe", 2.5f, 6, true);
}
