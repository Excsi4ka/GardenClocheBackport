package excsi.gardencloche.common;

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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CommonProxy implements IGuiHandler {

    public static Block gardenCloche;

    public int clocheRenderID = -1;

    public void preInit(FMLPreInitializationEvent event) {
        Config.init(event.getSuggestedConfigurationFile());
    }

    public void init(FMLInitializationEvent event) {
        gardenCloche = new BlockGardenCloche();
        GameRegistry.registerTileEntity(TileGardenCloche.class,"gardenClocheTile");
        NetworkRegistry.INSTANCE.registerGuiHandler(GardenCloche.instance,this);
        GardenClocheRegistry.init();
        GameRegistry.addShapedRecipe(new ItemStack(gardenCloche)," a ","b b","cdc",'a',new ItemStack(IEContent.blockMetalDecoration,1,2),
                'b',new ItemStack(Blocks.glass),'c',new ItemStack(IEContent.blockTreatedWood),'d',new ItemStack(IEContent.blockMetalDecoration,1,7));
    }

    public void postInit(FMLPostInitializationEvent event) {
        if(Loader.isModLoaded("MineTweaker3")) {
            MineTweakerHelper.register();
        }
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
