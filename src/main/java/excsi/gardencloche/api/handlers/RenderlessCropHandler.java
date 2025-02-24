package excsi.gardencloche.api.handlers;

import blusunrize.immersiveengineering.api.ComparableItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import excsi.gardencloche.common.tile.TileGardenCloche;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RenderlessCropHandler extends AbstractPlantHandler {

    boolean compareNBTS = false;

    public RenderlessCropHandler(ItemStack seed, ItemStack soil, ItemStack[] drops) {
        super(seed, soil, drops);
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

    @SideOnly(Side.CLIENT)
    @Override
    public void render(TileGardenCloche tile, double x, double y, double z, float growth, RenderBlocks blockRenderer) {

    }
}
