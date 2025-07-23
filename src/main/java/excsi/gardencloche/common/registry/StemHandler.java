package excsi.gardencloche.common.registry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import excsi.gardencloche.api.handlers.AbstractPlantHandler;
import excsi.gardencloche.common.tile.TileGardenCloche;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

public class StemHandler extends AbstractPlantHandler {

    public StemHandler(ItemStack seed, ItemStack soil, ItemStack[] drops) {
        super(seed,soil,drops);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(TileGardenCloche tile, double x, double y, double z,float growth, RenderBlocks blockRenderer) {
        Tessellator tessellator = Tessellator.instance;
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        //meta 7 is a fully grown melon/pumpkin stem
        int l = emulationCrop.getRenderColor(7);
        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(f, f1, f2);
        tessellator.setBrightness(tile.blockType.getMixedBrightnessForBlock(blockRenderer.blockAccess,tile.xCoord,tile.yCoord, tile.zCoord));
        int face = tile.facingSide == 5? 1 : tile.facingSide == 4 ? 0 : tile.facingSide;
        ForgeDirection offsetDir = ForgeDirection.getOrientation(tile.facingSide).getOpposite();
        renderBlockStem((BlockStem) emulationCrop, face, 1,x + (double) offsetDir.offsetX / 4,y + 1,z + (double) offsetDir.offsetZ / 4,0.75F);
        tessellator.setColorOpaque_F(1F, 1F, 1F);
        if(growth > 0)
            renderBlock(productBlock,x + 0.5 - (double) offsetDir.offsetX / 7,y+1.4,z + 0.5 - (double) offsetDir.offsetZ / 7,0.3f,growth);
        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public void renderBlock(Block block, double x, double y, double z, float scale, float growth) {
        Tessellator tessellator = Tessellator.instance;
        //2 for side texture of melons and pumpkins
        IIcon iicon = block.getIcon(2,0);
        double minU = iicon.getMinU();
        double minV = iicon.getMinV();
        double maxU = iicon.getMaxU();
        double maxV = iicon.getMaxV();
        double scaledGrowth = scale * growth;
        double x1 = x + 0.7 * scaledGrowth;
        double y1 = y + 0.5 * scale;
        double z1 = z + 0.7 * scaledGrowth;
        double x2 = x - 0.7 * scaledGrowth;
        double y2 = y - 0.7 * scaledGrowth;
        double z2 = z - 0.7 * scaledGrowth;
        //west
        tessellator.addVertexWithUV(x2,y2,z1,maxU,maxV);
        tessellator.addVertexWithUV(x2,y1,z1,maxU,minV);
        tessellator.addVertexWithUV(x2,y1,z2,minU,minV);
        tessellator.addVertexWithUV(x2,y2,z2,minU,maxV);

        //east
        tessellator.addVertexWithUV(x1,y2,z2,maxU,maxV);
        tessellator.addVertexWithUV(x1,y1,z2,maxU,minV);
        tessellator.addVertexWithUV(x1,y1,z1,minU,minV);
        tessellator.addVertexWithUV(x1,y2,z1,minU,maxV);

        //north
        tessellator.addVertexWithUV(x2,y2,z2,maxU,maxV);
        tessellator.addVertexWithUV(x2,y1,z2,maxU,minV);
        tessellator.addVertexWithUV(x1,y1,z2,minU,minV);
        tessellator.addVertexWithUV(x1,y2,z2,minU,maxV);

        //south
        tessellator.addVertexWithUV(x1,y2,z1,maxU,maxV);
        tessellator.addVertexWithUV(x1,y1,z1,maxU,minV);
        tessellator.addVertexWithUV(x2,y1,z1,minU,minV);
        tessellator.addVertexWithUV(x2,y2,z1,minU,maxV);

        iicon = block.getIcon(0,0);
        minU = iicon.getMinU();
        minV = iicon.getMinV();
        maxU = iicon.getMaxU();
        maxV = iicon.getMaxV();
        //top
        tessellator.addVertexWithUV(x1,y1,z2,maxU,maxV);
        tessellator.addVertexWithUV(x2,y1,z2,maxU,minV);
        tessellator.addVertexWithUV(x2,y1,z1,minU,minV);
        tessellator.addVertexWithUV(x1,y1,z1,minU,maxV);

        //bottom
        tessellator.addVertexWithUV(x1,y2,z2,maxU,maxV);
        tessellator.addVertexWithUV(x1,y2,z1,minU,maxV);
        tessellator.addVertexWithUV(x2,y2,z1,minU,minV);
        tessellator.addVertexWithUV(x2,y2,z2,maxU,minV);
    }

    @SideOnly(Side.CLIENT)
    public void renderBlockStem(BlockStem blockStem, int direction, double height, double x, double y, double z, float scale) {
        Tessellator tessellator = Tessellator.instance;
        IIcon iicon = blockStem.getStemIcon();
        double minU = iicon.getMinU();
        double minV = iicon.getMinV();
        double maxU = iicon.getMaxU();
        double maxV = iicon.getMaxV();
        double d8 = x + 0.5D - 0.5D * scale;
        double d9 = x + 0.5D + 0.5D * scale;
        double d10 = z + 0.5D - 0.5D * scale;
        double d11 = z + 0.5D + 0.5D * scale;
        double d12 = x + 0.5D;
        double d13 = z + 0.5D;
        if ((direction + 1) / 2 % 2 == 1) {
            double d14 = maxU;
            maxU = minU;
            minU = d14;
        }
        if (direction < 2) {
            tessellator.addVertexWithUV(d8, y + height, d13, minU, minV);
            tessellator.addVertexWithUV(d8, y + 0.0D, d13, minU, maxV);
            tessellator.addVertexWithUV(d9, y + 0.0D, d13, maxU, maxV);
            tessellator.addVertexWithUV(d9, y + height, d13, maxU, minV);
            tessellator.addVertexWithUV(d9, y + height, d13, maxU, minV);
            tessellator.addVertexWithUV(d9, y + 0.0D, d13, maxU, maxV);
            tessellator.addVertexWithUV(d8, y + 0.0D, d13, minU, maxV);
            tessellator.addVertexWithUV(d8, y + height, d13, minU, minV);
        } else {
            tessellator.addVertexWithUV(d12, y + height, d11, minU, minV);
            tessellator.addVertexWithUV(d12, y + 0.0D, d11, minU, maxV);
            tessellator.addVertexWithUV(d12, y + 0.0D, d10, maxU, maxV);
            tessellator.addVertexWithUV(d12, y + height, d10, maxU, minV);
            tessellator.addVertexWithUV(d12, y + height, d10, maxU, minV);
            tessellator.addVertexWithUV(d12, y + 0.0D, d10, maxU, maxV);
            tessellator.addVertexWithUV(d12, y + 0.0D, d11, minU, maxV);
            tessellator.addVertexWithUV(d12, y + height, d11, minU, minV);
        }
    }
}
