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
	
	private static final String NAME_STONE = "stone";
	private static final String NAME_NETHER = "netherbrick";
	private static final String NAME_OBSIDIAN = "obsidian";
	private static final String NAME_BONE = "bone";
	private static final String NAME_BEDROCK = "bedrock";
	private static final String NAME_BOOKS = "bookcase";
	private static final String NAME_BRICK = "brick";
	private static final String NAME_CACTUS = "cactus";
	private static final String NAME_CAKE = "cake";
	private static final String NAME_CLAY = "clay";
	private static final String NAME_COAL = "coal";
	private static final String NAME_EMERALD = "emerald";
	private static final String NAME_ENDBRICK = "end_brick";
	private static final String NAME_GLASS = "glass";
	private static final String NAME_GLOWSTONE = "glowstone";
	private static final String NAME_GRASS = "grass";
	private static final String NAME_ICE = "ice";
	private static final String NAME_PODZOL = "podzol";

    private static final ResourceLocation TEXTURE_NORMAL = new ResourceLocation (TEXTURES_LOCATION + "normal.png");
    private static final ResourceLocation TEXTURE_NORMAL_DOUBLE = new ResourceLocation (TEXTURES_LOCATION + "normal_double.png");
    private static final ResourceLocation TEXTURE_TRAPPED = new ResourceLocation (TEXTURES_LOCATION + "trapped.png");
    private static final ResourceLocation TEXTURE_TRAPPED_DOUBLE = new ResourceLocation (TEXTURES_LOCATION + "trapped_double.png");
    
	private static final ResourceLocation TEXTURE_STONE = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_STONE + ".png");
	private static final ResourceLocation TEXTURE_STONE_DBL = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_STONE + "_double.png");
	private static final ResourceLocation TEXTURE_STONE_TRAP = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_STONE + "_trapped.png");
	private static final ResourceLocation TEXTURE_STONE_DBL_TRAP = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_STONE + "_double_trapped.png");
	
	private static final ResourceLocation TEXTURE_NETHER = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_NETHER + ".png");
	private static final ResourceLocation TEXTURE_NETHER_DBL = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_NETHER + "_double.png");
	private static final ResourceLocation TEXTURE_NETHER_TRAP = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_NETHER + "_trapped.png");
	private static final ResourceLocation TEXTURE_NETHER_DBL_TRAP = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_NETHER + "_double_trapped.png");

	private static final ResourceLocation TEXTURE_OBSIDIAN = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_OBSIDIAN + ".png");
	private static final ResourceLocation TEXTURE_OBSIDIAN_DBL = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_OBSIDIAN + "_double.png");

	private static final ResourceLocation TEXTURE_BONE = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_BONE + ".png");
	private static final ResourceLocation TEXTURE_BONE_DBL = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_BONE + "_double.png");
	
	// Only one version of bedrock chest
	private static final ResourceLocation TEXTURE_BEDROCK = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_BEDROCK + ".png");
	
	private static final ResourceLocation TEXTURE_BOOKS = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_BOOKS + ".png");
	private static final ResourceLocation TEXTURE_BOOKS_DBL = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_BOOKS + "_double.png");
	private static final ResourceLocation TEXTURE_BOOKS_TRAP = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_BOOKS + "_trapped.png");
	private static final ResourceLocation TEXTURE_BOOKS_DBL_TRAP = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_BOOKS + "_double_trapped.png");
	
	private static final ResourceLocation TEXTURE_BRICK = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_BRICK + ".png");

	private static final ResourceLocation TEXTURE_CACTUS = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_CACTUS + ".png");
	private static final ResourceLocation TEXTURE_CACTUS_DBL = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_CACTUS + "_double.png");
	
	private static final ResourceLocation TEXTURE_CAKE = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_CAKE + ".png");

	private static final ResourceLocation TEXTURE_CLAY = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_CLAY + ".png");

	private static final ResourceLocation TEXTURE_COAL = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_COAL + ".png");
	private static final ResourceLocation TEXTURE_COAL_DBL = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_COAL + "_double.png");
	
	private static final ResourceLocation TEXTURE_EMERALD = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_EMERALD + ".png");

	private static final ResourceLocation TEXTURE_ENDBRICK = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_ENDBRICK + ".png");
	private static final ResourceLocation TEXTURE_ENDBRICK_DBL = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_ENDBRICK + "_double.png");
	
	private static final ResourceLocation TEXTURE_GLASS = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_GLASS + ".png");

	private static final ResourceLocation TEXTURE_GLOWSTONE = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_GLOWSTONE + ".png");

	private static final ResourceLocation TEXTURE_GRASS = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_GRASS + ".png");

	private static final ResourceLocation TEXTURE_ICE = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_ICE + ".png");
	private static final ResourceLocation TEXTURE_ICE_DBL = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_ICE + "_double.png");

	private static final ResourceLocation TEXTURE_PODZOL = new ResourceLocation (Reference.MOD_ID + ':' + TEXTURES_LOCATION + NAME_PODZOL + ".png");
	
    private final ModelCustomChest simpleChest = new ModelCustomChest ();
    private final ModelCustomChest largeChest = new ModelLargeCustomChest ();
    
	public RenderCustomChest () {
		
	}
	
	private ResourceLocation getTexture (String regName, boolean isTrapped, boolean isDouble) {
		int chestScale = isDouble ? 1 : 0;
		int trapOffset = isTrapped ? 2 : 0;
		
		ResourceLocation[] norms = {TEXTURE_NORMAL, TEXTURE_NORMAL_DOUBLE, TEXTURE_TRAPPED, TEXTURE_TRAPPED_DOUBLE};
		ResourceLocation[] stone = {TEXTURE_STONE, TEXTURE_STONE_DBL, TEXTURE_STONE_TRAP, TEXTURE_STONE_DBL_TRAP};
		ResourceLocation[] nether = {TEXTURE_NETHER, TEXTURE_NETHER_DBL, TEXTURE_NETHER_TRAP, TEXTURE_NETHER_DBL_TRAP};
		ResourceLocation[] obsidian = {TEXTURE_OBSIDIAN, TEXTURE_OBSIDIAN_DBL};
		ResourceLocation[] bone = {TEXTURE_BONE, TEXTURE_BONE_DBL};
		ResourceLocation[] books = {TEXTURE_BOOKS, TEXTURE_BOOKS_DBL, TEXTURE_BOOKS_TRAP, TEXTURE_BOOKS_DBL_TRAP};
		ResourceLocation[] cactus = {TEXTURE_CACTUS, TEXTURE_CACTUS_DBL};
		ResourceLocation[] coal = {TEXTURE_COAL, TEXTURE_COAL_DBL};
		ResourceLocation[] endbrick = {TEXTURE_ENDBRICK, TEXTURE_ENDBRICK_DBL};
		ResourceLocation[] ice = {TEXTURE_ICE, TEXTURE_ICE_DBL};
		
		switch (regName.replace ("chest_", "").replace ("_trapped", "")) {
			default:
				return norms[chestScale + trapOffset];
			case NAME_STONE:
				return stone[chestScale + trapOffset];
			case NAME_NETHER:
				return nether[chestScale + trapOffset];
			case NAME_OBSIDIAN:
				return obsidian[chestScale];
			case NAME_BONE:
				return bone[chestScale];
			case NAME_BEDROCK:
				return TEXTURE_BEDROCK;
			case NAME_BOOKS:
				return books[chestScale + trapOffset];
			case NAME_BRICK:
				return TEXTURE_BRICK;
			case NAME_CACTUS:
				return cactus[chestScale];
			case NAME_CAKE:
				return TEXTURE_CAKE;
			case NAME_CLAY:
				return TEXTURE_CLAY;
			case NAME_COAL:
				return coal[chestScale];
			case NAME_EMERALD:
				return TEXTURE_EMERALD;
			case NAME_ENDBRICK:
				return endbrick[chestScale];
			case NAME_GLASS:
				return TEXTURE_GLASS;
			case NAME_GLOWSTONE:
				return TEXTURE_GLOWSTONE;
			case NAME_GRASS:
				return TEXTURE_GRASS;
			case NAME_ICE:
				return ice[chestScale];
			case NAME_PODZOL:
				return TEXTURE_PODZOL;
		}
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
        } else
            i = 0;

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
