package excsi.gardencloche.common.block;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import excsi.gardencloche.client.renderer.BlockRendererGardenCloche;
import excsi.gardencloche.GardenCloche;
import excsi.gardencloche.common.item.ItemGardenCloche;
import excsi.gardencloche.common.tile.TileGardenCloche;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGardenCloche extends BlockContainer {

    @SideOnly(Side.CLIENT)
    public IIcon icon;

    public BlockGardenCloche() {
        super(Material.wood);
        this.setBlockName("ImmersiveEngineering.gardenCloche");
        this.setCreativeTab(ImmersiveEngineering.creativeTab);
        this.setHardness(5f);
        GameRegistry.registerBlock(this, ItemGardenCloche.class,"gardenCloche");
    }


    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileEntity curr = world.getTileEntity(x, y, z);
        if(curr instanceof TileGardenCloche) {
            TileGardenCloche te = ((TileGardenCloche) curr).mainTile();
            if (!world.isRemote && !player.isSneaking()) {
                player.openGui(GardenCloche.instance, 0, world, te.xCoord, te.yCoord, te.zCoord);
            }
            return true;
        }
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_) {
        TileGardenCloche thisTile = (TileGardenCloche) world.getTileEntity(x,y,z);
        if(thisTile != null && !world.isRemote) {
            TileGardenCloche main = thisTile.mainTile();
            if (!world.isAirBlock(x, y + thisTile.partOneOffset, z))
                world.setBlockToAir(x, y + thisTile.partOneOffset, z);
            if (!world.isAirBlock(x, y + thisTile.partTwoOffset, z))
                world.setBlockToAir(x, y + thisTile.partTwoOffset, z);

            for(int i = 0; i < main.inventory.length; i++) {
                if(main.inventory[i]!=null) {
                    EntityItem item = new EntityItem(main.getWorldObj(),main.xCoord+Math.random(),main.yCoord+Math.random()+0.1,main.zCoord+Math.random());
                    item.setEntityItemStack(main.inventory[i]);
                    item.delayBeforeCanPickup = 10;
                    main.getWorldObj().spawnEntityInWorld(item);
                }
            }
        }
        super.breakBlock(world, x, y, z, block, p_149749_6_);
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        BlockRendererGardenCloche.renderPass = pass;
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        icon = iconRegister.registerIcon("gardencloche:textureCloche");
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return icon;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return icon;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileGardenCloche();
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return null;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderType() {
        return GardenCloche.proxy.clocheRenderID;
    }
}
