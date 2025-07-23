package excsi.gardencloche.api.handlers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import excsi.gardencloche.common.tile.TileGardenCloche;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public abstract class AbstractPlantHandler {

    public ItemStack handlerSeed;

    public ItemStack seedSoil;

    public ItemStack[] drops;

    public Block emulationCrop;

    public int maxMeta;

    public Block productBlock;


    public AbstractPlantHandler(ItemStack seed,ItemStack soil,ItemStack[] drops) {
        this.handlerSeed = seed;
        this.seedSoil = soil;
        this.drops = drops;
    }

    public AbstractPlantHandler setEmulationCrop(Block crop) {
        this.emulationCrop = crop;
        return this;
    }

    public AbstractPlantHandler setMaxMeta(int meta) {
        this.maxMeta = meta;
        return this;
    }

    public AbstractPlantHandler setProductBlock(Block productBlock) {
        this.productBlock = productBlock;
        return this;
    }

    public void tick(TileGardenCloche tile) {}

    public boolean isValidSeed(ItemStack seed) {
        return OreDictionary.itemMatches(handlerSeed,seed,true);
    }

    public boolean isValidSoil(ItemStack stack) {
        if(stack == null)
            return false;
        return OreDictionary.itemMatches(seedSoil,stack,true);
    }

    // Fertilizer is passed in case you want the fertilizer to affect the output
    public ItemStack[] getOutputs(TileGardenCloche cloche, ItemStack fertilizer) {
        ItemStack[] copyDrops = new ItemStack[drops.length];
        for(int i = 0; i < drops.length; i++) {
            copyDrops[i] = drops[i].copy();
        }
        return copyDrops;
    }

    public float getGrowthStep(TileGardenCloche cloche, float boost, float growth) {
        return growth+0.01f*(1+boost);
    }

    @SideOnly(Side.CLIENT)
    public abstract void render(TileGardenCloche tile, double x, double y, double z,float growth, RenderBlocks blockRenderer);

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(!(obj instanceof AbstractPlantHandler)) {
            return false;
        }
        AbstractPlantHandler handler = (AbstractPlantHandler) obj;
        return isValidSeed(handler.handlerSeed);
    }
}