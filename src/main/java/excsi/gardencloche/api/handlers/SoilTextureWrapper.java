package excsi.gardencloche.api.handlers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

public class SoilTextureWrapper {

    public ResourceLocation texture;

    public ItemStack soil;

    public SoilTextureWrapper(ResourceLocation texture, ItemStack soil) {
        this.texture = texture;
        this.soil = soil;
    }

    public boolean matches(ItemStack compareSoil) {
        return OreDictionary.itemMatches(soil,compareSoil,true);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj instanceof SoilTextureWrapper) {
            return matches(((SoilTextureWrapper) obj).soil);
        }
        return false;
    }
}
