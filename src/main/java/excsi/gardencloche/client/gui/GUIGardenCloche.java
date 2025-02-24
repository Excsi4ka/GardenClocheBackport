package excsi.gardencloche.client.gui;

import blusunrize.immersiveengineering.client.ClientUtils;
import excsi.gardencloche.common.Config;
import excsi.gardencloche.common.container.ContainerGardenCloche;
import excsi.gardencloche.common.tile.TileGardenCloche;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class GUIGardenCloche extends GuiContainer {

    public TileGardenCloche tile;

    public GUIGardenCloche(InventoryPlayer inventoryPlayer, TileGardenCloche tile) {
        super(new ContainerGardenCloche(inventoryPlayer,tile));
        this.tile = tile;
    }

    @Override
    public void drawScreen(int mx, int my, float partial) {
        super.drawScreen(mx, my, partial);
        ArrayList<String> tooltip = new ArrayList<String>();
        ClientUtils.handleGuiTank(tile.tank, guiLeft+8,guiTop+8, 16,47, 176,30,20,51, mx,my, "gardencloche:textures/gui/belljar.png", tooltip);
        if (mx > this.guiLeft + 157 && mx < this.guiLeft + 165 && my > this.guiTop + 22 && my < this.guiTop + 68) {
            tooltip.add(this.tile.energyStorage.getEnergyStored() + "/" + this.tile.energyStorage.getMaxEnergyStored() + " RF");
        }
        if (mx > this.guiLeft + 29 && mx < this.guiLeft + 37 && my > this.guiTop + 22 && my < this.guiTop + 68) {
            tooltip.add(StatCollector.translateToLocal("cloche.amount")+" "+String.format("%.2f", (double)this.tile.fertilizerAmount/Config.fertilizerAmount));
            tooltip.add(StatCollector.translateToLocal("cloche.boost")+" "+this.tile.fertilizerBoost);
        }
        if (!tooltip.isEmpty()) {
            ClientUtils.drawHoveringText(tooltip, mx, my, this.fontRendererObj, this.guiLeft + this.xSize, -1);
            RenderHelper.enableGUIStandardItemLighting();
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        ClientUtils.bindTexture("gardencloche:textures/gui/belljar.png");
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        GL11.glDisable(GL11.GL_BLEND);
        int stored = (int)(46.0F * ((float)this.tile.energyStorage.getEnergyStored() / (float)this.tile.energyStorage.getMaxEnergyStored()));
        int fertilizer = (int)(46.0F * ((float)this.tile.fertilizerAmount / (float) Config.fertilizerAmount));
        ClientUtils.drawGradientRect(this.guiLeft + 158, this.guiTop + 22 + (46 - stored), this.guiLeft + 165, this.guiTop + 68, -4909824, -10482944);
        ClientUtils.handleGuiTank(tile.tank, guiLeft+8,guiTop+8, 16,47, 176,30,20,51, mx,my, "gardencloche:textures/gui/belljar.png", null);
        ClientUtils.drawGradientRect(this.guiLeft + 30, this.guiTop + 22 + (46 - fertilizer), this.guiLeft + 37, this.guiTop + 68, -12213245, -9856509);
    }
}
