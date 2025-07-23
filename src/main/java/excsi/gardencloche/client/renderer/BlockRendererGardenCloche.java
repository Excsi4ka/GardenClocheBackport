package excsi.gardencloche.client.renderer;

import blusunrize.immersiveengineering.client.ClientUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import excsi.gardencloche.GardenCloche;
import excsi.gardencloche.client.ClientProxy;
import excsi.gardencloche.common.tile.TileGardenCloche;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BlockRendererGardenCloche implements ISimpleBlockRenderingHandler {

    public static int renderPass = 0;

    public static final TileGardenCloche dummyTile = new TileGardenCloche();

    public BlockRendererGardenCloche(){
        dummyTile.facingSide = 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glPushMatrix();
        Tessellator.instance.startDrawingQuads();
        GL11.glRotatef(-180.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glScaled(0.5,0.5,0.5);
        renderPass = 0;
        ClientUtils.handleStaticTileRenderer(dummyTile);
        renderPass = 1;
        ClientUtils.handleStaticTileRenderer(dummyTile);
        Tessellator.instance.draw();
        GL11.glPopMatrix();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        TileGardenCloche tile = (TileGardenCloche) world.getTileEntity(x, y, z);
        if (tile != null && !tile.isDummy) {
            ClientUtils.handleStaticTileRenderer(tile);
            ClientProxy.rendererReference.renderBlocks = renderer;
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return GardenCloche.proxy.clocheRenderID;
    }
}
