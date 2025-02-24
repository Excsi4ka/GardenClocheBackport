package excsi.gardencloche.api.handlers;

import net.minecraft.item.ItemStack;

public interface IFertilizerHandler {

    boolean isValid(ItemStack fertilizer);

    float getGrowthMultiplier();

}