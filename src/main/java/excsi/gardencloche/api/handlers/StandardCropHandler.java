package excsi.gardencloche.api.handlers;

import blusunrize.immersiveengineering.api.ComparableItemStack;
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
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;

public class StandardCropHandler extends AbstractPlantHandler {

    boolean compareNBTS = false;

    int totalMetas;

    Block emulationCrop;

    public StandardCropHandler(ItemStack seed,ItemStack soil,ItemStack[] drops) {
        super(seed,soil,drops);
    }

    public void setRenderData(Block block,int meta) {
        totalMetas = meta;
        emulationCrop = block;
    }

    public void setCompareNBTS(boolean b) {
        compareNBTS = b;
    }

    @Override
    public boolean isValidSeed(ItemStack seed) {
        ComparableItemStack currentSeed = new ComparableItemStack(handlerSeed);
        currentSeed.setUseNBT(compareNBTS);
        return currentSeed.equals(new ComparableItemStack(seed));
    }

    @Override
    public boolean isValidSoil(ItemStack stack) {
        if(stack == null)
            return false;
        return OreDictionary.itemMatches(seedSoil,stack,true);
    }

    @Override
    public ItemStack[] getOutputs(TileGardenCloche cloche,ItemStack fertilizer) {
        ItemStack[] copyDrops = new ItemStack[drops.length];
        for(int i = 0; i < drops.length; i++) {
            copyDrops[i] = drops[i].copy();
        }
        return copyDrops;
    }

    @Override
    public float getGrowthStep(TileGardenCloche cloche, float boost, float growth) {
        return growth+0.01f*(1+boost);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(TileGardenCloche tile, double x, double y, double z,float growth, RenderBlocks blockRenderer) {
        Tessellator tessellator = Tessellator.instance;
        int meta = (int) (growth*totalMetas);
        GL11.glPushMatrix();
        GL11.glTranslated(x,y,z);
        GL11.glScaled(0.875,0.875,0.875);
        GL11.glTranslated(0.075,1.25,0.075);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        tessellator.startDrawingQuads();
        Block b = tile.getWorldObj().getBlock(tile.xCoord,tile.yCoord, tile.zCoord);
        int brightness = b.getMixedBrightnessForBlock(blockRenderer.blockAccess,tile.xCoord,tile.yCoord, tile.zCoord);
        tessellator.setBrightness(brightness);
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        blockRenderer.renderBlockCropsImpl(emulationCrop, meta, 0, 0, 0);
        tessellator.draw();
        GL11.glPopMatrix();
    }
}
