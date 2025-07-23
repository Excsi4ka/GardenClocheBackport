package excsi.gardencloche.api;

import excsi.gardencloche.api.handlers.*;
import excsi.gardencloche.common.registry.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GardenClocheRegistry {

    public static List<IFertilizerHandler> fertilizers = new ArrayList<>();

    public static HashMap<String,SoilTextureWrapper> soilTextures = new HashMap<>();

    public static HashMap<String,AbstractPlantHandler> plantHandlers = new HashMap<>();

    public static void registerPlantHandler(String ID,AbstractPlantHandler handler) {
        plantHandlers.put(ID,handler);
    }

    public static void registerFertilizer(IFertilizerHandler handler) {
        fertilizers.add(handler);
    }

    public static void mapSoilToTexture(String id,ItemStack stack, ResourceLocation rl) {
        soilTextures.put(id,new SoilTextureWrapper(rl,stack));
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

    public static boolean isSoilRegistered(ItemStack soil) {
        for(SoilTextureWrapper wrapper : soilTextures.values()) {
            if(wrapper.matches(soil))
                return true;
        }
        return false;
    }

    public static boolean isValidFertilizer(ItemStack item) {
        for (IFertilizerHandler handler : fertilizers) {
            if(handler.isValid(item))
                return true;
        }
        return false;
    }

    public static IFertilizerHandler getFertilizerHandlerFromStack(ItemStack item) {
        for (IFertilizerHandler handler : fertilizers) {
            if(handler.isValid(item)) {
                return handler;
            }
        }
        return null;
    }

    public static SoilTextureWrapper getSoilTextureFromStack(ItemStack soil) {
        if(soil == null)
            return null;
        for (SoilTextureWrapper handler : soilTextures.values()) {
            if(handler.matches(soil))
                return handler;
        }
        return null;
    }

    public static AbstractPlantHandler getPlantHandlerFromStack(ItemStack seed) {
        if(seed == null)
            return null;
        for (AbstractPlantHandler handler : plantHandlers.values()) {
            if(handler.isValidSeed(seed))
                return handler;
        }
        return null;
    }

    public static void registerDefaultCropHandler(String id, ItemStack seed, ItemStack soil, ItemStack[] drops, Block renderBlock, int maxMeta) {
        registerPlantHandler(id,
                new StandardCropHandler(seed,soil,drops)
                .setEmulationCrop(renderBlock)
                .setMaxMeta(maxMeta));
    }

    public static void registerRenderlessCropHandler(String id, ItemStack seed, ItemStack soil, ItemStack[] drops) {
        registerPlantHandler(id, new RenderlessCropHandler(seed,soil,drops));
    }

    public static void registerStemHandler(String id, ItemStack seed, ItemStack soil, ItemStack[] drops, Block renderBlock, Block product) {
        registerPlantHandler(id,
                new StemHandler(seed,soil,drops)
                .setEmulationCrop(renderBlock)
                .setProductBlock(product));
    }

    public static void registerReedHandler(String id, ItemStack seed,ItemStack soil, ItemStack[] drops, Block renderBlock) {
        registerPlantHandler(id,
                new ReedCropHandler(seed, soil, drops)
                .setEmulationCrop(renderBlock));
    }

    public static void registerCactusHandler(String id, ItemStack seed,ItemStack soil, ItemStack[] drops, Block renderBlock) {
        registerPlantHandler(id,
                new CactusCropHandler(seed,soil,drops)
                .setEmulationCrop(renderBlock));
    }
}
