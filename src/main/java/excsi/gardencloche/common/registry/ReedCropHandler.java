package excsi.gardencloche.common.registry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import excsi.gardencloche.api.handlers.AbstractPlantHandler;
import excsi.gardencloche.common.tile.TileGardenCloche;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ReedCropHandler extends AbstractPlantHandler {

    public ReedCropHandler(ItemStack seed, ItemStack soil, ItemStack[] drops) {
        super(seed,soil,drops);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(TileGardenCloche tile, double x, double y, double z, float growth, RenderBlocks blockRenderer) {
        Tessellator tessellator = Tessellator.instance;
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        tessellator.startDrawingQuads();
        tessellator.setBrightness(tile.blockType.getMixedBrightnessForBlock(blockRenderer.blockAccess,tile.xCoord,tile.yCoord, tile.zCoord));
        IIcon iicon = blockRenderer.getBlockIconFromSideAndMetadata(emulationCrop, 0, 0);
        drawReeds(iicon,x,y + 1,z,0.75F,1F);
        drawReeds(iicon,x,y + 1.75,z,0.75F,growth);
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public void drawReeds(IIcon iIcon, double x, double y, double z, float scale, float growth) {
        Tessellator tessellator = Tessellator.instance;
        double minU = iIcon.getMinU();
        double minV = iIcon.getMinV();
        double maxU = iIcon.getMaxU();
        double maxV = iIcon.getMaxV();
        //interpolate maxV with growth
        maxV = minV + (maxV - minV) * growth;
        double d7 = 0.45D * scale;
        double d8 = x + 0.5D - d7;
        double d9 = x + 0.5D + d7;
        double d10 = z + 0.5D - d7;
        double d11 = z + 0.5D + d7;
        double vertexHeightCap = scale * growth;
        tessellator.addVertexWithUV(d8, y + vertexHeightCap, d10, minU, minV);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, minU, maxV);
        tessellator.addVertexWithUV(d9, y + 0.0D, d11, maxU, maxV);
        tessellator.addVertexWithUV(d9, y + vertexHeightCap, d11, maxU, minV);
        tessellator.addVertexWithUV(d9, y + vertexHeightCap, d11, minU, minV);
        tessellator.addVertexWithUV(d9, y + 0.0D, d11, minU, maxV);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, maxU, maxV);
        tessellator.addVertexWithUV(d8, y + vertexHeightCap, d10, maxU, minV);
        tessellator.addVertexWithUV(d8, y + vertexHeightCap, d11, minU, minV);
        tessellator.addVertexWithUV(d8, y + 0.0D, d11, minU, maxV);
        tessellator.addVertexWithUV(d9, y + 0.0D, d10, maxU, maxV);
        tessellator.addVertexWithUV(d9, y + vertexHeightCap, d10, maxU, minV);
        tessellator.addVertexWithUV(d9, y + vertexHeightCap, d10, minU, minV);
        tessellator.addVertexWithUV(d9, y + 0.0D, d10, minU, maxV);
        tessellator.addVertexWithUV(d8, y + 0.0D, d11, maxU, maxV);
        tessellator.addVertexWithUV(d8, y + vertexHeightCap, d11, maxU, minV);
    }
}
