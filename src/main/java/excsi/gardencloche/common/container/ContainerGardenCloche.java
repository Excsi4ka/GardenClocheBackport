package excsi.gardencloche.common.container;

import excsi.gardencloche.api.GardenClocheRegistry;
import excsi.gardencloche.common.tile.TileGardenCloche;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerGardenCloche extends Container {

    public TileGardenCloche tile;

    public ContainerGardenCloche(InventoryPlayer inventoryPlayer, TileGardenCloche cloche) {
        this.tile = cloche;
        this.addSlotToContainer(new FertilizerSlot(tile,0,8,59));
        this.addSlotToContainer(new PlantSlot(tile,1,62,34));
        this.addSlotToContainer(new SoilSlot(tile,2,62,54));
        this.addSlotToContainer(new OutputSlot(tile,3,116,34));
        this.addSlotToContainer(new OutputSlot(tile,4,134,34));
        this.addSlotToContainer(new OutputSlot(tile,5,116,52));
        this.addSlotToContainer(new OutputSlot(tile,6,134,52));
        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 85 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 143));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tile.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        return null;
    }

    public static class SoilSlot extends Slot {

        public SoilSlot(IInventory inv, int id, int x, int y) {
            super(inv, id, x, y);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return GardenClocheRegistry.isSoilRegistered(stack);
        }
    }

    public static class PlantSlot extends Slot {

        public PlantSlot(IInventory inv, int id, int x, int y) {
            super(inv, id, x, y);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }

    public static class OutputSlot extends Slot {

        public OutputSlot(IInventory inv, int id, int x, int y) {
            super(inv, id, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack p_75214_1_) {
            return false;
        }
    }

    public static class FertilizerSlot extends Slot {

        public FertilizerSlot(IInventory inv, int id, int x, int y) {
            super(inv, id, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return GardenClocheRegistry.isValidFertilizer(stack);
        }
    }
}
