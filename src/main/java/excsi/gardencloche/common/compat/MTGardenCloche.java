package excsi.gardencloche.common.compat;

import blusunrize.immersiveengineering.api.ComparableItemStack;
import excsi.gardencloche.api.GardenClocheRegistry;
import excsi.gardencloche.api.handlers.AbstractPlantHandler;
import excsi.gardencloche.api.handlers.IFertilizerHandler;
import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.immersiveengineering.GardenCloche")
public class MTGardenCloche {

    @ZenMethod
    public static void addDefaultCrop(IItemStack seed,IItemStack soil,IItemStack[] drops,IItemStack renderBlock,int meta,boolean compareNBT) {
        MineTweakerAPI.apply(new AddDefaultCrop(MineTweakerHelper.getStack(seed),MineTweakerHelper.getStack(soil),
                MineTweakerHelper.getStacks(drops),Block.getBlockFromItem(MineTweakerHelper.getStack(renderBlock).getItem()),meta,compareNBT));
    }

    @ZenMethod
    public static void addSoilTextureMapping(IItemStack stack,String resourceLocation) {
        MineTweakerAPI.apply(new AddSoilTextureMap(MineTweakerHelper.getStack(stack),new ResourceLocation(resourceLocation)));
    }

    @ZenMethod
    public static void addRenderlessCrop(IItemStack seed,IItemStack soil,IItemStack[] drops,boolean compareNBT) {
        MineTweakerAPI.apply(new AddRenderlessHandler(MineTweakerHelper.getStack(seed),MineTweakerHelper.getStack(soil),
                MineTweakerHelper.getStacks(drops),compareNBT));
    }

    @ZenMethod
    public static void addFertilizerHandler(IItemStack stack,float boost) {
        MineTweakerAPI.apply(new AddFertilizerHandler(MineTweakerHelper.getStack(stack),boost));
    }

    public static class AddDefaultCrop implements IUndoableAction {

        public ItemStack seeds;

        public ItemStack soil;

        public ItemStack[] drops;

        public Block renderBlock;

        public int maxMeta;

        public boolean compareNBT;

        public AddDefaultCrop(ItemStack seeds, ItemStack soil, ItemStack[] drops, Block renderBlock, int maxMeta, boolean compareNBT) {
            this.seeds = seeds;
            this.soil = soil;
            this.drops = drops;
            this.renderBlock = renderBlock;
            this.maxMeta = maxMeta;
            this.compareNBT = compareNBT;
        }

        @Override
        public void apply() {
            GardenClocheRegistry.registerDefaultCropHandler(seeds,soil,drops,renderBlock,maxMeta,compareNBT);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            AbstractPlantHandler handler = GardenClocheRegistry.getPlantHandler(seeds);
            GardenClocheRegistry.plantHandlers.remove(handler);
        }

        @Override
        public String describe() {
            return "Registering default crop handler for crop: " + seeds.getUnlocalizedName();
        }

        @Override
        public String describeUndo() {
            return "Removing default plant handler for crop: " + seeds.getUnlocalizedName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    public static class AddRenderlessHandler implements IUndoableAction {

        public ItemStack seeds;

        public ItemStack soil;

        public ItemStack[] drops;

        public boolean compareNBT;

        public AddRenderlessHandler(ItemStack seeds, ItemStack soil, ItemStack[] drops, boolean compareNBT) {
            this.seeds = seeds;
            this.soil = soil;
            this.drops = drops;
            this.compareNBT = compareNBT;
        }

        @Override
        public void apply() {
            GardenClocheRegistry.registerRenderlessCropHandler(seeds,soil,drops,compareNBT);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            AbstractPlantHandler handler = GardenClocheRegistry.getPlantHandler(seeds);
            GardenClocheRegistry.plantHandlers.remove(handler);
        }

        @Override
        public String describe() {
            return "Registering renderless crop handler for crop: " + seeds.getUnlocalizedName();
        }

        @Override
        public String describeUndo() {
            return "Removing renderless plant handler for crop: " + seeds.getUnlocalizedName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    public static class AddSoilTextureMap implements IUndoableAction {

        public ItemStack soil;

        public ResourceLocation texture;

        public AddSoilTextureMap(ItemStack soil, ResourceLocation texture) {
            this.soil = soil;
            this.texture = texture;
        }

        @Override
        public void apply() {
            GardenClocheRegistry.mapSoilToTexture(new ComparableItemStack(soil),texture);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            GardenClocheRegistry.soilTextureMap.remove(new ComparableItemStack(soil));
        }

        @Override
        public String describe() {
            return "Mapping block " + soil.getUnlocalizedName() + " to texture "+texture.toString();
        }

        @Override
        public String describeUndo() {
            return "Unmapping block " + soil.getUnlocalizedName() + " from texture "+texture.toString();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }


    public static class AddFertilizerHandler implements IUndoableAction {

        public ItemStack fertilizer;

        public float boost;

        public AddFertilizerHandler(ItemStack fertilizer,float boost) {
            this.fertilizer = fertilizer;
            this.boost = boost;
        }


        @Override
        public void apply() {
            GardenClocheRegistry.registerBasicFertilizer(fertilizer,boost);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            IFertilizerHandler handler = GardenClocheRegistry.getFertilizerHandler(fertilizer);
            GardenClocheRegistry.fertilizers.remove(handler);
        }

        @Override
        public String describe() {
            return "Registering new fertilizer " + fertilizer.getUnlocalizedName()+ " with boost " + boost;
        }

        @Override
        public String describeUndo() {
            return "Removing fertilizer " + fertilizer.getUnlocalizedName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
}
