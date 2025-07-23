package excsi.gardencloche.common.registry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import excsi.gardencloche.api.handlers.AbstractPlantHandler;
import excsi.gardencloche.common.tile.TileGardenCloche;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;

public class RenderlessCropHandler extends AbstractPlantHandler {

    public RenderlessCropHandler(ItemStack seed, ItemStack soil, ItemStack[] drops) {
        super(seed, soil, drops);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void render(TileGardenCloche tile, double x, double y, double z, float growth, RenderBlocks blockRenderer) {}
}
