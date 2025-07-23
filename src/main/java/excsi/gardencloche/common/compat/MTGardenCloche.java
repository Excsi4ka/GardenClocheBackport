package excsi.gardencloche.common.compat;

import excsi.gardencloche.api.GardenClocheRegistry;
import excsi.gardencloche.api.handlers.AbstractPlantHandler;
import excsi.gardencloche.api.handlers.IFertilizerHandler;
import excsi.gardencloche.api.handlers.SoilTextureWrapper;
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
    public static void addDefaultCrop(String id,IItemStack seed,IItemStack soil,IItemStack[] drops,IItemStack renderBlock,int meta) {
        MineTweakerAPI.apply(new AddDefaultCrop(id,MineTweakerHelper.getStack(seed),MineTweakerHelper.getStack(soil),
                MineTweakerHelper.getStacks(drops),Block.getBlockFromItem(MineTweakerHelper.getStack(renderBlock).getItem()),meta));
    }

    @ZenMethod
    public static void addReedCrop(String id,IItemStack seed,IItemStack soil,IItemStack[] drops,IItemStack renderBlock) {
        MineTweakerAPI.apply(new AddReedHandler(id,MineTweakerHelper.getStack(seed),MineTweakerHelper.getStack(soil),
                MineTweakerHelper.getStacks(drops),Block.getBlockFromItem(MineTweakerHelper.getStack(renderBlock).getItem())));
    }

    @ZenMethod
    public static void addCactus(String id,IItemStack seed,IItemStack soil,IItemStack[] drops,IItemStack renderBlock) {
        MineTweakerAPI.apply(new AddCactusHandler(id,MineTweakerHelper.getStack(seed),MineTweakerHelper.getStack(soil),
                MineTweakerHelper.getStacks(drops),Block.getBlockFromItem(MineTweakerHelper.getStack(renderBlock).getItem())));
    }
    @ZenMethod
    public static void addStemCrop(String id,IItemStack seed,IItemStack soil,IItemStack[] drops,IItemStack renderBlock,IItemStack productBlock) {
        MineTweakerAPI.apply(new AddStemHandler(id,MineTweakerHelper.getStack(seed),MineTweakerHelper.getStack(soil),
                MineTweakerHelper.getStacks(drops),Block.getBlockFromItem(MineTweakerHelper.getStack(renderBlock).getItem()),
                Block.getBlockFromItem(MineTweakerHelper.getStack(productBlock).getItem())));
    }
    @ZenMethod
    public static void removeHandler(String id) {
        MineTweakerAPI.apply(new RemoveHandler(id));
    }

    @ZenMethod
    public static void addSoilTextureMapping(String ID,IItemStack stack,String resourceLocation) {
        MineTweakerAPI.apply(new AddSoilTextureMap(ID,MineTweakerHelper.getStack(stack),new ResourceLocation(resourceLocation)));
    }

    @ZenMethod
    public static void addRenderlessCrop(String id,IItemStack seed,IItemStack soil,IItemStack[] drops) {
        MineTweakerAPI.apply(new AddRenderlessHandler(id,MineTweakerHelper.getStack(seed),MineTweakerHelper.getStack(soil),
                MineTweakerHelper.getStacks(drops)));
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

        public String id;

        public AddDefaultCrop(String id, ItemStack seeds, ItemStack soil, ItemStack[] drops, Block renderBlock, int maxMeta) {
            this.seeds = seeds;
            this.soil = soil;
            this.drops = drops;
            this.renderBlock = renderBlock;
            this.maxMeta = maxMeta;
            this.id = id;
        }

        @Override
        public void apply() {
            GardenClocheRegistry.registerDefaultCropHandler(id,seeds,soil,drops,renderBlock,maxMeta);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            AbstractPlantHandler handler = GardenClocheRegistry.getPlantHandlerFromStack(seeds);
            GardenClocheRegistry.plantHandlers.remove(id);
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

        public String id;

        public AddRenderlessHandler(String id,ItemStack seeds, ItemStack soil, ItemStack[] drops) {
            this.seeds = seeds;
            this.soil = soil;
            this.drops = drops;
            this.id = id;
        }

        @Override
        public void apply() {
            GardenClocheRegistry.registerRenderlessCropHandler(id,seeds,soil,drops);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            GardenClocheRegistry.plantHandlers.remove(id);
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

        public SoilTextureWrapper wrapper;

        public String id;

        public AddSoilTextureMap(String ID, ItemStack soil, ResourceLocation texture) {
            this.wrapper = new SoilTextureWrapper(texture,soil);
            this.id = ID;
        }

        @Override
        public void apply() {
            GardenClocheRegistry.soilTextures.put(id,wrapper);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            GardenClocheRegistry.soilTextures.remove(id);
        }

        @Override
        public String describe() {
            return "Mapping block " + wrapper.soil.getUnlocalizedName() + " to texture " + wrapper.texture.toString();
        }

        @Override
        public String describeUndo() {
            return "Unmapping block " +  wrapper.soil.getUnlocalizedName() + " from texture " + wrapper.texture.toString();
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
            IFertilizerHandler handler = GardenClocheRegistry.getFertilizerHandlerFromStack(fertilizer);
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

    public static class AddCactusHandler implements IUndoableAction {

        public ItemStack seeds;

        public ItemStack soil;

        public ItemStack[] drops;

        public Block renderBlock;

        public String id;

        public AddCactusHandler(String id, ItemStack seeds, ItemStack soil, ItemStack[] drops, Block renderBlock) {
            this.seeds = seeds;
            this.soil = soil;
            this.drops = drops;
            this.renderBlock = renderBlock;
            this.id = id;
        }

        @Override
        public void apply() {
            GardenClocheRegistry.registerCactusHandler(id,seeds,soil,drops,renderBlock);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            GardenClocheRegistry.plantHandlers.remove(id);
        }

        @Override
        public String describe() {
            return "Registering cactus crop handler for crop: " + seeds.getUnlocalizedName();
        }

        @Override
        public String describeUndo() {
            return "Removing cactus plant handler for crop: " + seeds.getUnlocalizedName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    public static class AddReedHandler implements IUndoableAction {

        public ItemStack seeds;

        public ItemStack soil;

        public ItemStack[] drops;

        public Block renderBlock;

        public String id;

        public AddReedHandler(String id, ItemStack seeds, ItemStack soil, ItemStack[] drops, Block renderBlock) {
            this.seeds = seeds;
            this.soil = soil;
            this.drops = drops;
            this.renderBlock = renderBlock;
            this.id = id;
        }

        @Override
        public void apply() {
            GardenClocheRegistry.registerReedHandler(id,seeds,soil,drops,renderBlock);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            GardenClocheRegistry.plantHandlers.remove(id);
        }

        @Override
        public String describe() {
            return "Registering reed crop handler for crop: " + seeds.getUnlocalizedName();
        }

        @Override
        public String describeUndo() {
            return "Removing reed plant handler for crop: " + seeds.getUnlocalizedName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    public static class AddStemHandler implements IUndoableAction {

        public ItemStack seeds;

        public ItemStack soil;

        public ItemStack[] drops;

        public Block renderBlock, productBlock;


        public String id;

        public AddStemHandler(String id, ItemStack seeds, ItemStack soil, ItemStack[] drops, Block renderBlock, Block productBlock) {
            this.seeds = seeds;
            this.soil = soil;
            this.drops = drops;
            this.renderBlock = renderBlock;
            this.productBlock = productBlock;
            this.id = id;
        }

        @Override
        public void apply() {
            GardenClocheRegistry.registerStemHandler(id,seeds,soil,drops,renderBlock,productBlock);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            GardenClocheRegistry.plantHandlers.remove(id);
        }

        @Override
        public String describe() {
            return "Registering stem crop handler for crop: " + seeds.getUnlocalizedName();
        }

        @Override
        public String describeUndo() {
            return "Removing stem plant handler for crop: " + seeds.getUnlocalizedName();
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    public static class RemoveHandler implements IUndoableAction {

        public String id;

        public AbstractPlantHandler cachedHandler;

        public RemoveHandler(String id) {
            this.id = id;
            this.cachedHandler = GardenClocheRegistry.plantHandlers.get(id);
        }

        @Override
        public void apply() {
            GardenClocheRegistry.plantHandlers.remove(id);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            GardenClocheRegistry.registerPlantHandler(id,cachedHandler);
        }

        @Override
        public String describe() {
            return "Removing a plant handler with id " + id;
        }

        @Override
        public String describeUndo() {
            return "Returning a plant handler with id " + id;
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
}
