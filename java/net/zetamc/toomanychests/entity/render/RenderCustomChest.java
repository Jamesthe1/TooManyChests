package net.zetamc.toomanychests.entity.render;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.zetamc.toomanychests.blocks.ChestBase;
import net.zetamc.toomanychests.blocks.tileentity.TileEntityCustomChest;
import net.zetamc.toomanychests.entity.model.ModelCustomChest;
import net.zetamc.toomanychests.entity.model.ModelLargeCustomChest;
import net.zetamc.toomanychests.util.Reference;

@SideOnly (Side.CLIENT)
public class RenderCustomChest extends TileEntitySpecialRenderer<TileEntityCustomChest> {
	private static final String TEXTURES_LOCATION = "textures/entity/chest/";
	
    private final ModelCustomChest simpleChest = new ModelCustomChest ();
    private final ModelCustomChest largeChest = new ModelLargeCustomChest ();
    
	public RenderCustomChest () {
		
	}
	
	private ResourceLocation getTexture (String regName, boolean isTrapped, boolean isDouble) {
		String baseName = regName.replace ("chest_", "").replace ("_trapped", "");
		String dblTag = isDouble ? "_double" : "";
		String trapTag = isTrapped ? "_trapped" : "";
		
		return new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + baseName + dblTag + trapTag + ".png");
	}
	
	public void render (TileEntityCustomChest te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GlStateManager.enableDepth ();
        GlStateManager.depthFunc (515);
        GlStateManager.depthMask (true);
        int i;

        if (te.hasWorld ()) {
            Block block = te.getBlockType ();
            i = te.getBlockMetadata ();

            if (block instanceof ChestBase && i == 0) {
                ((ChestBase)block).checkForSurroundingChests (te.getWorld (), te.getPos (), te.getWorld ().getBlockState (te.getPos ()));
                i = te.getBlockMetadata ();
            }

            te.checkForAdjacentChests ();
        } else i = 0;

        if (te.adjacentChestZNeg == null && te.adjacentChestXNeg == null) {
            ModelCustomChest modelchest;

            if (te.adjacentChestXPos == null && te.adjacentChestZPos == null) {
                modelchest = this.simpleChest;

                if (destroyStage >= 0) {
                    this.bindTexture (DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode (5890);
                    GlStateManager.pushMatrix ();
                    GlStateManager.scale (4.0F, 4.0F, 1.0F);
                    GlStateManager.translate (0.0625F, 0.0625F, 0.0625F);
                    GlStateManager.matrixMode (5888);
                } else
                	this.bindTexture (getTexture (te.regName, te.getChestType () == BlockChest.Type.TRAP, false));
            } else {
                modelchest = this.largeChest;

                if (destroyStage >= 0) {
                    this.bindTexture (DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode (5890);
                    GlStateManager.pushMatrix ();
                    GlStateManager.scale (8.0F, 4.0F, 1.0F);
                    GlStateManager.translate (0.0625F, 0.0625F, 0.0625F);
                    GlStateManager.matrixMode (5888);
                } else
                    this.bindTexture (getTexture (te.regName, te.getChestType () == BlockChest.Type.TRAP, true));
            }

            GlStateManager.pushMatrix ();
            GlStateManager.enableRescaleNormal ();

            if (destroyStage < 0)
                GlStateManager.color (1.0F, 1.0F, 1.0F, alpha);

            GlStateManager.translate ((float)x, (float)y + 1.0F, (float)z + 1.0F);
            GlStateManager.scale (1.0F, -1.0F, -1.0F);
            GlStateManager.translate (0.5F, 0.5F, 0.5F);
            int j = 0;

            switch (i) {
            	default:
            		j = 0;
            		break;
            	case 2:
            		j = 180;
            		break;
            	case 4:
            		j = 90;
            		break;
            	case 5:
            		j = -90;
            		break;
            }

            if (i == 2 && te.adjacentChestXPos != null)
                GlStateManager.translate (1.0F, 0.0F, 0.0F);

            if (i == 5 && te.adjacentChestZPos != null)
                GlStateManager.translate (0.0F, 0.0F, -1.0F);

            GlStateManager.rotate ((float)j, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate (-0.5F, 0, -0.5F);
            float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;

            if (te.adjacentChestZNeg != null) {
                float f1 = te.adjacentChestZNeg.prevLidAngle + (te.adjacentChestZNeg.lidAngle - te.adjacentChestZNeg.prevLidAngle) * partialTicks;

                if (f1 > f)
                    f = f1;
            }

            if (te.adjacentChestXNeg != null) {
                float f2 = te.adjacentChestXNeg.prevLidAngle + (te.adjacentChestXNeg.lidAngle - te.adjacentChestXNeg.prevLidAngle) * partialTicks;

                if (f2 > f)
                    f = f2;
            }

            f = 1.0F - f;
            f = 1.0F - f * f * f;
            modelchest.lid.rotateAngleX = -(f * ((float)Math.PI / 2F));
            modelchest.renderAll ();
            GlStateManager.disableRescaleNormal ();
            GlStateManager.popMatrix ();
            GlStateManager.color (1.0F, 1.0F, 1.0F, 1.0F);

            if (destroyStage >= 0) {
                GlStateManager.matrixMode (5890);
                GlStateManager.popMatrix ();
                GlStateManager.matrixMode (5888);
            }
        }
	}
}
