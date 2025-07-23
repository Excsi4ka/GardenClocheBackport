package excsi.gardencloche.common.item;

import excsi.gardencloche.common.CommonProxy;
import excsi.gardencloche.common.tile.TileGardenCloche;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemGardenCloche extends ItemBlock {

    public ItemGardenCloche(Block b) {
        super(b);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        int playerViewQuarter = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5) & 3;
        byte f = (byte) (playerViewQuarter == 0 ? 2 : (playerViewQuarter == 1 ? 5 : (playerViewQuarter == 2 ? 3 : 4)));
        if (!world.isAirBlock(x, y + 1, z) || !world.isAirBlock(x, y + 2, z)) {
            return false;
        }
        boolean ret = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, meta);
        if (!ret) {
            return false;
        } else {
            TileGardenCloche tileEntity = (TileGardenCloche) world.getTileEntity(x, y, z);
            tileEntity.isDummy = false;
            tileEntity.pos = 0;
            tileEntity.facingSide = f;
            world.setBlock(x,y+1,z, CommonProxy.gardenCloche,0,3);
            tileEntity = (TileGardenCloche) world.getTileEntity(x, y+1, z);
            tileEntity.pos = 1;
            tileEntity.facingSide = f;
            world.setBlock(x,y+2,z, CommonProxy.gardenCloche,0,3);
            tileEntity = (TileGardenCloche) world.getTileEntity(x, y+2, z);
            tileEntity.pos = 2;
            tileEntity.facingSide = f;
            return true;
        }
    }
}
