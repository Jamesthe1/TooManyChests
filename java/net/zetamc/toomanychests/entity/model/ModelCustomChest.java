package net.zetamc.toomanychests.entity.model;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelCustomChest extends ModelBase {
	public ModelRenderer chestBody;
	public ModelRenderer lid;

	public ModelCustomChest () {
		this.textureWidth = 64;
		this.textureHeight = 64;

		chestBody = new ModelRenderer (this);
		chestBody.setRotationPoint (0.0F, 16.0F, 0.0F);
		chestBody.cubeList.add (new ModelBox (chestBody, 0, 19, 1.0F, -18.0F, 1.0F, 14, 10, 14, 0.0F, false));

		lid = new ModelRenderer (this);
		lid.setRotationPoint (8.0F, -2.0F, 15.0F);
		lid.cubeList.add (new ModelBox (lid, 0, 0, -7.0F, -5.0F, -14.0F, 14, 5, 14, 0.0F, false));
		lid.cubeList.add (new ModelBox (lid, 0, 0, -1.0F, -2.0F, -15.0F, 2, 4, 1, 0.0F, false));
	}
	
	public void renderAll () {
		float scale = 0.0625f;
		
		chestBody.render (scale);
		lid.render (scale);
	}
}