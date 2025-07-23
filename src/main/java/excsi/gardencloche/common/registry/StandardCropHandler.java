package excsi.gardencloche.common.registry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import excsi.gardencloche.api.handlers.AbstractPlantHandler;
import excsi.gardencloche.common.tile.TileGardenCloche;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class StandardCropHandler extends AbstractPlantHandler {

    public StandardCropHandler(ItemStack seed,ItemStack soil,ItemStack[] drops) {
        super(seed,soil,drops);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(TileGardenCloche tile, double x, double y, double z, float growth, RenderBlocks blockRenderer) {
        Tessellator tessellator = Tessellator.instance;
        int meta = (int)(growth*maxMeta);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        tessellator.startDrawingQuads();
        tessellator.setBrightness(tile.blockType.getMixedBrightnessForBlock(blockRenderer.blockAccess,tile.xCoord,tile.yCoord, tile.zCoord));
        renderBlockCrops(blockRenderer,emulationCrop,meta,x,y + 1.05, z, 0.8F);
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public void renderBlockCrops(RenderBlocks renderBlocks, Block p_147795_1_, int meta, double x, double y, double z, float scale) {
        Tessellator tessellator = Tessellator.instance;
        IIcon iicon = renderBlocks.getBlockIconFromSideAndMetadata(p_147795_1_, 0, meta);
        double d3 = iicon.getMinU();
        double d4 = iicon.getMinV();
        double d5 = iicon.getMaxU();
        double d6 = iicon.getMaxV();
        double d7 = x + 0.5D - 0.25D * scale;
        double d8 = x + 0.5D + 0.25D * scale;
        double d9 = z + 0.5D - 0.5D * scale;
        double d10 = z + 0.5D + 0.5D * scale;
        double maxHeight = 1 * scale;
        tessellator.addVertexWithUV(d7, y + maxHeight, d9, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d7, y + maxHeight, d10, d5, d4);
        tessellator.addVertexWithUV(d7, y + maxHeight, d10, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d7, y + maxHeight, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + maxHeight, d10, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d8, y + maxHeight, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + maxHeight, d9, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d8, y + maxHeight, d10, d5, d4);
        d7 = x + 0.5D - 0.5D * scale;
        d8 = x + 0.5D + 0.5D * scale;
        d9 = z + 0.5D - 0.25D * scale;
        d10 = z + 0.5D + 0.25D * scale;
        tessellator.addVertexWithUV(d7, y + maxHeight, d9, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d8, y + maxHeight, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + maxHeight, d9, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d9, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d9, d5, d6);
        tessellator.addVertexWithUV(d7, y + maxHeight, d9, d5, d4);
        tessellator.addVertexWithUV(d8, y + maxHeight, d10, d3, d4);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d7, y + maxHeight, d10, d5, d4);
        tessellator.addVertexWithUV(d7, y + maxHeight, d10, d3, d4);
        tessellator.addVertexWithUV(d7, y + 0.0D, d10, d3, d6);
        tessellator.addVertexWithUV(d8, y + 0.0D, d10, d5, d6);
        tessellator.addVertexWithUV(d8, y + maxHeight, d10, d5, d4);
    }
}
