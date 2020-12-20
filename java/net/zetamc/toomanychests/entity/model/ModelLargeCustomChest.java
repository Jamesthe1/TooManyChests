package net.zetamc.toomanychests.entity.model;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelLargeCustomChest extends ModelCustomChest {
	public ModelLargeCustomChest () {
		this.textureWidth = 128;
		this.textureHeight = 64;

		chestBody = new ModelRenderer (this);
		chestBody.setRotationPoint (0.0F, 16.0F, 0.0F);
		chestBody.cubeList.add (new ModelBox (chestBody, 0, 19, 1.0F, -18.0F, 1.0F, 30, 10, 14, 0.0F, false));

		lid = new ModelRenderer (this);
		lid.setRotationPoint (16.0F, -2.0F, 15.0F);
		lid.cubeList.add (new ModelBox (lid, 0, 0, -15.0F, -5.0F, -14.0F, 30, 5, 14, 0.0F, false));
		lid.cubeList.add (new ModelBox (lid, 0, 0, -1.0F, -2.0F, -15.0F, 2, 4, 1, 0.0F, false));
	}
}
