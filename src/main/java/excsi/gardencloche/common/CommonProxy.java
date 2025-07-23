package excsi.gardencloche.common;

import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.common.IEContent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import excsi.gardencloche.GardenCloche;
import excsi.gardencloche.api.GardenClocheRegistry;
import excsi.gardencloche.common.block.BlockGardenCloche;
import excsi.gardencloche.common.compat.MineTweakerHelper;
import excsi.gardencloche.common.container.ContainerGardenCloche;
import excsi.gardencloche.common.tile.TileGardenCloche;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler {

    public static Block gardenCloche;

    public int clocheRenderID = -1;

    public void preInit(FMLPreInitializationEvent event) {
        Config.init(event.getSuggestedConfigurationFile());
    }

    public void initHandlers(FMLInitializationEvent event) {
        gardenCloche = new BlockGardenCloche();
        GameRegistry.registerTileEntity(TileGardenCloche.class,"gardenClocheTile");
        NetworkRegistry.INSTANCE.registerGuiHandler(GardenCloche.instance,this);
        initHandlers();
        GameRegistry.addShapedRecipe(new ItemStack(gardenCloche)," a ","b b","cdc",'a',new ItemStack(IEContent.blockMetalDecoration,1,2),
                'b',new ItemStack(Blocks.glass),'c',new ItemStack(IEContent.blockTreatedWood),'d',new ItemStack(IEContent.blockMetalDecoration,1,7));
    }

    public void postInit(FMLPostInitializationEvent event) {
        if(Loader.isModLoaded("MineTweaker3")) {
            MineTweakerHelper.register();
        }
    }

    public void initHandlers() {
        GardenClocheRegistry.mapSoilToTexture("dirtTex",new ItemStack(Item.getItemFromBlock(Blocks.dirt)), new ResourceLocation("textures/blocks/farmland_wet.png"));
        GardenClocheRegistry.mapSoilToTexture("sandTex",new ItemStack(Item.getItemFromBlock(Blocks.sand)), new ResourceLocation("textures/blocks/sand.png"));
        GardenClocheRegistry.mapSoilToTexture("soulsandTex",new ItemStack(Item.getItemFromBlock(Blocks.soul_sand)), new ResourceLocation("textures/blocks/soul_sand.png"));
        GardenClocheRegistry.mapSoilToTexture("waterTex",new ItemStack(Items.water_bucket), new ResourceLocation("gardencloche:textures/blocks/water.png"));

        GardenClocheRegistry.registerBasicFertilizer(new ItemStack(Items.dye,1,15),0.25f);

        GardenClocheRegistry.registerDefaultCropHandler("wheatHandler",new ItemStack(Items.wheat_seeds),new ItemStack(Item.getItemFromBlock(Blocks.dirt)),
                new ItemStack[]{new ItemStack(Items.wheat,2),new ItemStack(Items.wheat_seeds)},
                Blocks.wheat,8);

        GardenClocheRegistry.registerDefaultCropHandler("potatoHandler",new ItemStack(Items.potato),new ItemStack(Item.getItemFromBlock(Blocks.dirt)),
                new ItemStack[]{new ItemStack(Items.potato,2)}, Blocks.potatoes,8);

        GardenClocheRegistry.registerDefaultCropHandler("carrotHandler",new ItemStack(Items.carrot),new ItemStack(Item.getItemFromBlock(Blocks.dirt)),
                new ItemStack[]{new ItemStack(Items.carrot,2)}, Blocks.carrots,8);

        GardenClocheRegistry.registerDefaultCropHandler("netherwartHandler",new ItemStack(Items.nether_wart),new ItemStack(Item.getItemFromBlock(Blocks.soul_sand)),
                new ItemStack[]{new ItemStack(Items.nether_wart,2)},Blocks.nether_wart,5);

        GardenClocheRegistry.registerDefaultCropHandler("hempHandler",new ItemStack(IEContent.itemSeeds),new ItemStack(Item.getItemFromBlock(Blocks.dirt)),
                new ItemStack[]{new ItemStack(IEContent.itemMaterial,2,3),new ItemStack(IEContent.itemSeeds)},IEContent.blockCrop,4);

        GardenClocheRegistry.registerStemHandler("melonHandler",new ItemStack(Items.melon_seeds),new ItemStack(Item.getItemFromBlock(Blocks.dirt)),
                new ItemStack[]{new ItemStack(Items.melon,2)},Blocks.melon_stem,Blocks.melon_block);

        GardenClocheRegistry.registerStemHandler("pumpkinHandler",new ItemStack(Items.pumpkin_seeds),new ItemStack(Item.getItemFromBlock(Blocks.dirt)),
                new ItemStack[]{new ItemStack(Item.getItemFromBlock(Blocks.pumpkin),2)},Blocks.pumpkin_stem,Blocks.pumpkin);

        GardenClocheRegistry.registerCactusHandler("cactusHandler",new ItemStack(Item.getItemFromBlock(Blocks.cactus)),new ItemStack(Item.getItemFromBlock(Blocks.sand)),
                new ItemStack[]{new ItemStack(Item.getItemFromBlock(Blocks.cactus),2)},Blocks.cactus);

        GardenClocheRegistry.registerReedHandler("caneHandler",new ItemStack(Items.reeds),new ItemStack(Item.getItemFromBlock(Blocks.sand)),
                new ItemStack[]{new ItemStack(Items.reeds,2)},Blocks.reeds);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x,y,z);
        if(tile instanceof TileGardenCloche && ID == 0){
            return new ContainerGardenCloche(player.inventory, (TileGardenCloche) tile);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
