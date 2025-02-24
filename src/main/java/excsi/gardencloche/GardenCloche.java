package excsi.gardencloche;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import excsi.gardencloche.common.CommonProxy;

@Mod(modid = GardenCloche.MODID,version = "1.0",dependencies = "required-after:ImmersiveEngineering")
public class GardenCloche {

    public static final String MODID = "gardencloche";

    @Mod.Instance(GardenCloche.MODID)
    public static GardenCloche instance;

    @SidedProxy(clientSide = "excsi.gardencloche.client.ClientProxy", serverSide = "excsi.gardencloche.common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
