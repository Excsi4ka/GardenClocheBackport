package excsi.gardencloche.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import excsi.gardencloche.client.gui.GUIGardenCloche;
import excsi.gardencloche.client.renderer.BlockRendererGardenCloche;
import excsi.gardencloche.client.renderer.TileRenderGardenCloche;
import excsi.gardencloche.common.CommonProxy;
import excsi.gardencloche.common.tile.TileGardenCloche;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy {

    public static TileRenderGardenCloche rendererReference;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void initHandlers(FMLInitializationEvent event) {
        super.initHandlers(event);
        clocheRenderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(new BlockRendererGardenCloche());
        rendererReference = new TileRenderGardenCloche();
        ClientRegistry.bindTileEntitySpecialRenderer(TileGardenCloche.class,rendererReference);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (ID == 0 && te instanceof TileGardenCloche) {
            return new GUIGardenCloche(player.inventory, (TileGardenCloche) te);
        }
        return null;
    }
}
