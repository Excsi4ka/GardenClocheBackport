package excsi.gardencloche.api;

import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.common.IEContent;
import excsi.gardencloche.api.handlers.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.HashSet;

public class GardenClocheRegistry {

    public static HashSet<IFertilizerHandler> fertilizers = new HashSet<IFertilizerHandler>();

    public static HashMap<ComparableItemStack,ResourceLocation> soilTextureMap = new HashMap<ComparableItemStack, ResourceLocation>();

    public static HashSet<AbstractPlantHandler> plantHandlers = new HashSet<AbstractPlantHandler>();

    public static void init() {
        mapSoilToTexture(new ComparableItemStack(new ItemStack(Item.getItemFromBlock(Blocks.dirt))), new ResourceLocation("textures/blocks/farmland_wet.png"));
        mapSoilToTexture(new ComparableItemStack(new ItemStack(Item.getItemFromBlock(Blocks.sand))), new ResourceLocation("textures/blocks/sand.png"));
        mapSoilToTexture(new ComparableItemStack(new ItemStack(Item.getItemFromBlock(Blocks.soul_sand))), new ResourceLocation("textures/blocks/soul_sand.png"));
        mapSoilToTexture(new ComparableItemStack(new ItemStack(Items.water_bucket)), new ResourceLocation("textures/blocks/water_still.png"));

        registerBasicFertilizer(new ItemStack(Items.dye,1,15),0.25f);

        registerDefaultCropHandler(new ItemStack(Items.wheat_seeds),new ItemStack(Item.getItemFromBlock(Blocks.dirt)),
                new ItemStack[]{new ItemStack(Items.wheat,2),new ItemStack(Items.wheat_seeds)},
                Blocks.wheat,8,false);

        registerDefaultCropHandler(new ItemStack(Items.potato),new ItemStack(Item.getItemFromBlock(Blocks.dirt)),
                new ItemStack[]{new ItemStack(Items.potato,2)}, Blocks.potatoes,8,false);

        registerDefaultCropHandler(new ItemStack(Items.carrot),new ItemStack(Item.getItemFromBlock(Blocks.dirt)),
                new ItemStack[]{new ItemStack(Items.carrot,2)}, Blocks.carrots,8,false);

        registerDefaultCropHandler(new ItemStack(Items.nether_wart),new ItemStack(Item.getItemFromBlock(Blocks.soul_sand)),
                new ItemStack[]{new ItemStack(Items.nether_wart,2)},Blocks.nether_wart,5,false);

        registerDefaultCropHandler(new ItemStack(IEContent.itemSeeds),new ItemStack(Item.getItemFromBlock(Blocks.dirt)),
                new ItemStack[]{new ItemStack(IEContent.itemMaterial,2,3),new ItemStack(IEContent.itemSeeds)},IEContent.blockCrop,4,false);

        registerRenderlessCropHandler(new ItemStack(Items.melon_seeds),new ItemStack(Item.getItemFromBlock(Blocks.dirt)),
                new ItemStack[]{new ItemStack(Items.melon,2)},false);

        registerRenderlessCropHandler(new ItemStack(Items.pumpkin_seeds),new ItemStack(Item.getItemFromBlock(Blocks.dirt)),
                new ItemStack[]{new ItemStack(Item.getItemFromBlock(Blocks.pumpkin),2)},false);

        registerRenderlessCropHandler(new ItemStack(Item.getItemFromBlock(Blocks.cactus)),new ItemStack(Item.getItemFromBlock(Blocks.sand)),
                new ItemStack[]{new ItemStack(Item.getItemFromBlock(Blocks.cactus),2)},false);

        registerRenderlessCropHandler(new ItemStack(Items.reeds),new ItemStack(Item.getItemFromBlock(Blocks.sand)),
                new ItemStack[]{new ItemStack(Items.reeds,2)},false);
    }

    public static void registerFertilizer(IFertilizerHandler handler) {
        fertilizers.add(handler);
    }

    public static void registerBasicFertilizer(final ItemStack stack, final float boost) {
        registerFertilizer(new IFertilizerHandler() {
            @Override
            public boolean isValid(ItemStack fertilizer) {
                return OreDictionary.itemMatches(stack,fertilizer,true);
            }

            @Override
            public float getGrowthMultiplier() {
                return 1+boost;
            }
        });
    }

    public static boolean isValidFertilizer(ItemStack item) {
        for (IFertilizerHandler handler:fertilizers) {
            if(handler.isValid(item)) {
                return true;
            }
        }
        return false;
    }

    public static IFertilizerHandler getFertilizerHandler(ItemStack item) {
        for (IFertilizerHandler handler:fertilizers) {
            if(handler.isValid(item)) {
                return handler;
            }
        }
        return null;
    }

    public static void mapSoilToTexture(ComparableItemStack stack, ResourceLocation rl) {
        soilTextureMap.put(stack,rl);
    }

    public static AbstractPlantHandler getPlantHandler(ItemStack seed) {
        if(seed == null)
            return null;
        for (AbstractPlantHandler handler:plantHandlers) {
            if(handler.isValidSeed(seed))
                return handler;
        }
        return null;
    }

    public static void registerHandler(AbstractPlantHandler handler) {
        plantHandlers.add(handler);
    }

    public static void registerDefaultCropHandler(ItemStack seed, ItemStack soil, ItemStack[] drops, Block renderBlock, int maxMeta, boolean compareNBT) {
        StandardCropHandler handler = new StandardCropHandler(seed,soil,drops);
        handler.setCompareNBTS(compareNBT);
        handler.setRenderData(renderBlock,maxMeta);
        registerHandler(handler);
    }

    public static void registerRenderlessCropHandler(ItemStack seed, ItemStack soil, ItemStack[] drops,boolean compareNBT) {
        RenderlessCropHandler handler = new RenderlessCropHandler(seed,soil,drops);
        handler.setCompareNBTS(compareNBT);
        registerHandler(handler);
    }

    public static void registerStemHandler(ItemStack seed, ItemStack soil, ItemStack[] drops, Block renderBlock, Block product, boolean compareNBT) {
        StemHandler handler = new StemHandler(seed,soil,drops);
        handler.setCompareNBTS(compareNBT);
        handler.setRenderData(renderBlock,product);
        registerHandler(handler);
    }
}
