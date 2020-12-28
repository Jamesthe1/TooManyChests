package net.zetamc.toomanychests.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.zetamc.toomanychests.util.Reference;

@SideOnly(Side.CLIENT)
public class PadlockButton extends GuiButton {
	private static final ResourceLocation TEXTURE = new ResourceLocation (Reference.MOD_ID + ":textures/gui/container/padlock.png");
	
	private boolean clicked = false;
	
	public static enum ColorID {
		WHITE, GREEN, RED
	}
	
	public static enum ButtonID {
		BLOCK, BAR
	}
	
	ColorID color = ColorID.WHITE;
	ButtonID style = ButtonID.BLOCK;
	
	public PadlockButton (int buttonId, int x, int y, String buttonText) {
		super (buttonId, x, y, 18, 18, buttonText);
	}
	
	public PadlockButton (int buttonId, int x, int y, String buttonText, ColorID color) {
		super (buttonId, x, y, 18, 18, buttonText);
		this.color = color;
	}
	
	public PadlockButton (int buttonId, int x, int y, String buttonText, ButtonID style) {
		super (buttonId, x, y, style == ButtonID.BLOCK ? 18 : 89, 18, buttonText);
		this.style = style;
	}
	
	protected int boolToInt (boolean bool) {
		return bool ? 1 : 0;
	}
	
	protected int getColorInt () {
		switch (color) {
			default:
				return 0;
			case RED:
				return 1;
			case GREEN:
				return 2;
		}
	}
	
	@Override
	public void drawButton (Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) return;
        
        FontRenderer fontrenderer = mc.fontRenderer;
        mc.getTextureManager ().bindTexture (TEXTURE);
        GlStateManager.color (1.0F, 1.0F, 1.0F, 1.0F);
        
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        int i = boolToInt (style == ButtonID.BLOCK ? clicked : hovered);
        int colorOffset = getColorInt ();
        
        GlStateManager.enableBlend ();
        GlStateManager.tryBlendFuncSeparate (GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc (GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.drawTexturedModalRect (this.x, this.y, 0 + colorOffset*18, 151 - i*18, this.width, this.height);
        this.mouseDragged (mc, mouseX, mouseY);
        int j = 14737632;

        if (color == ColorID.WHITE)
        	j = 0;
        if (hovered)
            j = 16777120;
        
        int xCenter = (this.x + this.width / 2) - fontrenderer.getStringWidth (this.displayString) / 2;
        fontrenderer.drawString (this.displayString, xCenter, this.y + (this.height - 8) / 2, j);
    }
	
	@Override
	public boolean mousePressed (Minecraft mc, int mouseX, int mouseY) {
		boolean mouseInBounds = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
		if (this.visible && mouseInBounds) clicked = true;
        return this.visible && mouseInBounds;
    }
	
	@Override
	public void mouseReleased (int mouseX, int mouseY) {
		clicked = false;
	}
	
	@Override
	public void playPressSound (SoundHandler soundHandler) {
        soundHandler.playSound (PositionedSoundRecord.getMasterRecord (SoundEvents.UI_BUTTON_CLICK, 1.5F));
    }
}
