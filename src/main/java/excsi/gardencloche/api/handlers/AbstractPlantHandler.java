package excsi.gardencloche.api.handlers;

import blusunrize.immersiveengineering.api.ComparableItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import excsi.gardencloche.common.tile.TileGardenCloche;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;

public abstract class AbstractPlantHandler {

    public ItemStack handlerSeed;

    public ItemStack seedSoil;

    public ItemStack[] drops;

    public AbstractPlantHandler(ItemStack seed,ItemStack soil,ItemStack[] drops) {
        this.handlerSeed = seed;
        this.seedSoil = soil;
        this.drops = drops;
    }

    public abstract boolean isValidSeed(ItemStack seed);

    public abstract boolean isValidSoil(ItemStack stack);

    public abstract ItemStack[] getOutputs(TileGardenCloche cloche,ItemStack fertilizer);

    public abstract float getGrowthStep(TileGardenCloche cloche,float boost,float growth);

    @SideOnly(Side.CLIENT)
    public abstract void render(TileGardenCloche tile, double x, double y, double z,float growth, RenderBlocks blockRenderer);

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AbstractPlantHandler)) {
            return false;
        }
        AbstractPlantHandler handler = (AbstractPlantHandler) obj;
        return new ComparableItemStack(handlerSeed).equals(new ComparableItemStack(handler.handlerSeed));
    }
}