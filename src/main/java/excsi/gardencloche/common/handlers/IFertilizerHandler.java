package excsi.gardencloche.common.handlers;

import net.minecraft.item.ItemStack;

public interface IFertilizerHandler {

    boolean isValid(ItemStack fertilizer);

    float getGrowthMultiplier();

}