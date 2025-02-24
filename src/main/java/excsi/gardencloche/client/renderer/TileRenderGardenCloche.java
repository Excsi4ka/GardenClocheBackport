package excsi.gardencloche.client.renderer;

import blusunrize.immersiveengineering.api.ComparableItemStack;
import blusunrize.immersiveengineering.client.models.ModelIEObj;
import blusunrize.immersiveengineering.client.render.TileRenderIE;
import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import excsi.gardencloche.api.handlers.AbstractPlantHandler;
import excsi.gardencloche.common.CommonProxy;
import excsi.gardencloche.api.GardenClocheRegistry;
import excsi.gardencloche.common.tile.TileGardenCloche;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class TileRenderGardenCloche extends TileRenderIE {

    ModelIEObj model = new ModelIEObj("gardencloche:models/belljar.obj") {
        public IIcon getBlockIcon(String groupName) {
            return CommonProxy.gardenCloche.getIcon(0, 0);
        }
    };

    public RenderBlocks renderBlocks;

    public IModelCustom modelSoil;

    public TileRenderGardenCloche() {
        modelSoil = AdvancedModelLoader.loadModel(new ResourceLocation("gardencloche:models/soil.obj"));
    }

    @Override
    public void renderDynamic(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        TileGardenCloche tile = (TileGardenCloche) tileEntity;
        if(tile.isDummy)
            return;
        if(Minecraft.getMinecraft().thePlayer.getDistance(tile.xCoord,tile.yCoord,tile.zCoord)>64)
            return;
        if(tile.inventory[2] != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.5f, 0.95f, 0.5f);
            GL11.glTranslated(x, y, z);
            ResourceLocation rl = GardenClocheRegistry.soilTextureMap.get(new ComparableItemStack(tile.inventory[2]));
            Minecraft.getMinecraft().renderEngine.bindTexture(rl);
            modelSoil.renderAll();
            GL11.glPopMatrix();
        }
        AbstractPlantHandler handler = GardenClocheRegistry.getPlantHandler(tile.inventory[1]);
        if(handler==null)
            return;
        handler.render(tile,x,y,z, tile.growth,renderBlocks);
    }

    @Override
    public void renderStatic(TileEntity tileEntity, Tessellator tessellator, Matrix4 translationMatrix, Matrix4 rotMatrix) {
        TileGardenCloche tile = (TileGardenCloche) tileEntity;
        switch (tile.facingSide) {
            default:
                break;
            case 2:
                rotMatrix.rotate(Math.toRadians(180),0.0,1.0,0.0);
                translationMatrix.translate(1,0,1);
                break;
            case 5:
                rotMatrix.rotate(Math.toRadians(90), 0.0, 1.0, 0.0);
                translationMatrix.translate(0,0,1);
                break;
            case 4:
                rotMatrix.rotate(Math.toRadians(-90), 0.0, 1.0, 0.0);
                translationMatrix.translate(1,0,0);
                break;

        }
        if (BlockRendererGardenCloche.renderPass == 1) {
            this.model.render(tileEntity, tessellator, translationMatrix, rotMatrix, 0, false, new String[]{"glass"});
        } else {
            this.model.render(tileEntity, tessellator, translationMatrix, rotMatrix, 0, false, new String[]{"base"});
        }
    }
}
