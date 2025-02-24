package excsi.gardencloche.common.compat;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;

public class MineTweakerHelper {

    public static void register() {
        MineTweakerAPI.registerClass(MTGardenCloche.class);
    }

    public static ItemStack getStack(IItemStack iItemStack){
        return MineTweakerMC.getItemStack(iItemStack);
    }

    public static ItemStack[] getStacks(IItemStack[] stacks) {
        ItemStack[] items = new ItemStack[stacks.length];
        for(int i = 0; i < stacks.length; i++) {
            items[i] = getStack(stacks[i]);
        }
        return items;
    }
}
