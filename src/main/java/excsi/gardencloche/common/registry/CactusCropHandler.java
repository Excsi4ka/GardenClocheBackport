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

public class CactusCropHandler extends AbstractPlantHandler {

    public CactusCropHandler(ItemStack seed, ItemStack soil, ItemStack[] drops) {
        super(seed,soil,drops);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(TileGardenCloche tile, double x, double y, double z,float growth, RenderBlocks blockRenderer) {
        Tessellator tessellator = Tessellator.instance;
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(1F, 1F, 1F);
        tessellator.setBrightness(tile.blockType.getMixedBrightnessForBlock(blockRenderer.blockAccess,tile.xCoord,tile.yCoord, tile.zCoord));
        renderBlockCactus(emulationCrop,x + 0.2,y + 1,z + 0.2,0.6f, 1F);
        renderBlockCactus(emulationCrop,x + 0.2,y + 1.6,z + 0.2,0.6f, growth);
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public void renderBlockCactus(Block block, double x, double y, double z, float scale, float growth) {
        Tessellator tessellator = Tessellator.instance;
        IIcon iicon = block.getIcon(2,0);
        double minU = iicon.getMinU();
        double minV = iicon.getMinV();
        double maxU = iicon.getMaxU();
        double maxV = iicon.getMaxV();
        //interpolate maxV with growth
        maxV = minV + (maxV - minV) * growth;
        double x1 = x + 1 * scale;
        double y1 = y + 1 * scale * growth;
        double z1 = z + 1 * scale;
        // 2 / 16 / 2 = 0.0625. 2 pixels from each side because cactus thorns and divide by 2 because the cactus is thinner than regular blocks
        double offset = 0.0625 * scale;
        //west
        tessellator.addVertexWithUV(x + offset,y,z1,maxU,maxV);
        tessellator.addVertexWithUV(x + offset,y1,z1,maxU,minV);
        tessellator.addVertexWithUV(x + offset,y1,z,minU,minV);
        tessellator.addVertexWithUV(x + offset,y,z,minU,maxV);

        //east
        tessellator.addVertexWithUV(x1 - offset,y,z,maxU,maxV);
        tessellator.addVertexWithUV(x1 - offset,y1,z,maxU,minV);
        tessellator.addVertexWithUV(x1 - offset,y1,z1,minU,minV);
        tessellator.addVertexWithUV(x1 - offset,y,z1,minU,maxV);

        //north
        tessellator.addVertexWithUV(x,y,z + offset,maxU,maxV);
        tessellator.addVertexWithUV(x,y1,z + offset,maxU,minV);
        tessellator.addVertexWithUV(x1,y1,z + offset,minU,minV);
        tessellator.addVertexWithUV(x1,y,z + offset,minU,maxV);

        //south
        tessellator.addVertexWithUV(x1,y,z1 - offset,maxU,maxV);
        tessellator.addVertexWithUV(x1,y1,z1 - offset,maxU,minV);
        tessellator.addVertexWithUV(x,y1,z1 - offset,minU,minV);
        tessellator.addVertexWithUV(x,y,z1 - offset,minU,maxV);

        //cactus top texture
        iicon = block.getIcon(1,0);
        minU = iicon.getMinU();
        minV = iicon.getMinV();
        maxU = iicon.getMaxU();
        maxV = iicon.getMaxV();

        //top
        tessellator.addVertexWithUV(x1, y1, z, maxU, maxV);
        tessellator.addVertexWithUV(x, y1, z, maxU, minV);
        tessellator.addVertexWithUV(x, y1, z1, minU, minV);
        tessellator.addVertexWithUV(x1, y1, z1, minU, maxV);


        //bottom
        //tessellator.addVertexWithUV(x1,y,z,maxU,maxV);
        //tessellator.addVertexWithUV(x1,y,z1,minU,maxV);
        //tessellator.addVertexWithUV(x,y,z1,minU,minV);
        //tessellator.addVertexWithUV(x,y,z,maxU,minV);
    }
}
