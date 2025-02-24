package excsi.gardencloche.common;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {

    public static int clocheFluidConsumption;

    public static int clocheEnergyConsumption;

    public static int fertilizerAmount;

    public static Configuration config;

    public static void init(File file) {
        config = new Configuration(file);
        syncConfig();
    }

    public static void syncConfig() {
        clocheEnergyConsumption = config.getInt("energy consumption","cloche",15,1,16000,"How much rf per tick is consumed");
        clocheFluidConsumption = config.getInt("fluid consumption","cloche",250,1,4000,"How much fluid is consumed");
        fertilizerAmount = config.getInt("fertilizer amount","cloche",1500,1,Integer.MAX_VALUE,"How much fertilizer is made");
        config.save();
    }
}
