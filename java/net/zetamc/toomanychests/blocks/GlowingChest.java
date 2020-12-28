package net.zetamc.toomanychests.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class GlowingChest extends ChestBase {
	// Opacity goes from 0-1
	public GlowingChest (String name, SoundType soundType, Material material, int guiId, int invSize, int stackSize, boolean isTrapped, String tool, float hardness, float blastResistance, boolean canBeDouble, float opacity) {
		super (name, soundType, material, guiId, invSize, stackSize, isTrapped, tool, hardness, blastResistance, canBeDouble);
		
		opacity = Math.min (opacity, 1);
		setLightLevel (opacity);
	}
	
}
