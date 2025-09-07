package excsi.gardencloche.common;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {

    public static int clocheFluidConsumption;

    public static int clocheEnergyConsumption;

    public static int fertilizerAmount;

    public static float defaultGrowthSpeed;

    public static Configuration config;

    public static void init(File file) {
        config = new Configuration(file);
        syncConfig();
    }

    public static void syncConfig() {
        clocheEnergyConsumption = config.getInt("energy consumption","cloche",15,1,16000,"How much rf per tick is consumed");
        clocheFluidConsumption = config.getInt("fluid consumption","cloche",250,1,4000,"How much fluid is consumed");
        fertilizerAmount = config.getInt("fertilizer amount","cloche",1500,1,Integer.MAX_VALUE,"How much fertilizer is made");
        defaultGrowthSpeed = config.getFloat("growth speed","cloche",0.01f,0f,1f,
                "The percentage of growth done per tick.\nFor example 0.01f means the plants grows by 1% per tick and 20% per second.\nThis is before any fertilizer boost is applied");
        config.save();
    }
}
