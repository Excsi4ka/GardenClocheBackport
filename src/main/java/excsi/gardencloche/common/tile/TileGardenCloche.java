package excsi.gardencloche.common.tile;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.util.Utils;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import excsi.gardencloche.api.GardenClocheRegistry;
import excsi.gardencloche.api.handlers.AbstractPlantHandler;
import excsi.gardencloche.api.handlers.IFertilizerHandler;
import excsi.gardencloche.api.handlers.SoilTextureWrapper;
import excsi.gardencloche.common.Config;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import net.minecraftforge.oredict.OreDictionary;

public class TileGardenCloche extends TileEntityIEBase implements IEnergyReceiver, IFluidHandler, IInventory {

    public EnergyStorage energyStorage = new EnergyStorage(16000);

    public FluidTank tank = new FluidTank(4000);

    public boolean isDummy = true;

    public byte pos = 0, facingSide = 0;

    public ItemStack[] inventory = new ItemStack[7];

    public int fertilizerAmount = 0;

    public float growth = 0, fertilizerBoost = 1;

    public AbstractPlantHandler currentHandler;

    public SoilTextureWrapper currentTextureWrapper;

    public TileGardenCloche() {}

    public TileGardenCloche mainTile() {
        return (TileGardenCloche) worldObj.getTileEntity(xCoord,yCoord-pos,zCoord);
    }

    @Override
    public void updateEntity() {
        if(this.isDummy)
            return;
        boolean needsSync = false;
        boolean consume = false;

        if(currentHandler == null || !currentHandler.isValidSeed(inventory[1])) {
            currentHandler = GardenClocheRegistry.getPlantHandlerFromStack(inventory[1]);
        }

        if(currentTextureWrapper == null || !currentTextureWrapper.matches(inventory[2])) {
            currentTextureWrapper = GardenClocheRegistry.getSoilTextureFromStack(inventory[2]);
        }

        if(currentHandler == null)
            return;

        currentHandler.tick(this);

        if(worldObj.isRemote) {
            if(energyStorage.getEnergyStored()>Config.clocheEnergyConsumption && fertilizerAmount>0) {
                if (worldObj.rand.nextInt(8) == 0) {
                    double partX = xCoord + 0.5;
                    double partY = yCoord + 2.6875;
                    double partZ = zCoord + 0.5;
                    worldObj.spawnParticle("reddust", partX, partY, partZ, 0, 0, 0);
                }
            }
        }else {
            if(fertilizerAmount<=0 && tank.getFluidAmount() >= Config.clocheFluidConsumption) {
                tank.drain(Config.clocheFluidConsumption,true);
                needsSync = true;
                if(inventory[0] != null && GardenClocheRegistry.isValidFertilizer(inventory[0])) {
                    IFertilizerHandler fertilizerHandler = GardenClocheRegistry.getFertilizerHandlerFromStack(inventory[0]);
                    fertilizerBoost = fertilizerHandler.getGrowthMultiplier();
                    inventory[0].stackSize--;
                    if(inventory[0].stackSize<=0) {
                        inventory[0] = null;
                    }
                }
                fertilizerAmount = Config.fertilizerAmount;
            }
            if(currentHandler.isValidSoil(inventory[2]) && energyStorage.extractEnergy(Config.clocheEnergyConsumption,true) == Config.clocheEnergyConsumption && fertilizerAmount > 0) {
                if(growth>=1 && !outputSlotsFull()) {
                    ItemStack[] outputs = currentHandler.getOutputs(this,inventory[0]);
                    for (int j = 0; j < outputs.length; j++) {
                        for(int i = 3; i < 7; i++) {
                            if(inventory[i] == null) {
                                inventory[i] = outputs[j];
                                outputs[j] = null;
                            } else {
                                ItemStack invStack = inventory[i];
                                if(OreDictionary.itemMatches(invStack,outputs[j],true)) {
                                    int newSize = invStack.stackSize+outputs[j].stackSize;
                                    if(newSize>64) {
                                        inventory[i].stackSize = 64;
                                        outputs[j].stackSize = newSize-64;
                                    }else {
                                        inventory[i].stackSize = newSize;
                                        outputs[j] = null;
                                    }
                                }
                            }
                        }
                    }
                    growth = 0;
                }else {
                    if(!outputSlotsFull()) {
                        growth = currentHandler.getGrowthStep(this, fertilizerBoost, growth);
                        consume = true;
                        needsSync = true;
                    }
                }
            }
            if (worldObj.getTotalWorldTime() % 8L == 0L) {
                ForgeDirection dir = ForgeDirection.getOrientation(facingSide);
                TileEntity inventoryFront = this.worldObj.getTileEntity(xCoord+dir.offsetX,yCoord+1,zCoord+dir.offsetZ);

                ItemStack stack;
                for (int j = 3; j < 7; ++j) {
                    if (this.getStackInSlot(j) != null) {
                        stack = Utils.copyStackWithAmount(this.getStackInSlot(j), 1);
                        if (inventoryFront instanceof ISidedInventory && ((ISidedInventory) inventoryFront).getAccessibleSlotsFromSide(ForgeDirection.OPPOSITES[facingSide]).length > 0 || inventoryFront instanceof IInventory && ((IInventory) inventoryFront).getSizeInventory() > 0) {
                            stack = Utils.insertStackIntoInventory((IInventory) inventoryFront, stack, ForgeDirection.OPPOSITES[facingSide]);
                        }
                        if (stack == null) {
                            this.decrStackSize(j, 1);
                            break;
                        }
                    }
                }
            }
            if(consume) {
                fertilizerAmount--;
                energyStorage.extractEnergy(Config.clocheEnergyConsumption,false);
            }
            if(needsSync) {
                worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbtTagCompound, boolean dataPacket) {
        isDummy = nbtTagCompound.getBoolean("dummy");
        pos = nbtTagCompound.getByte("MultiblockPos");
        facingSide = nbtTagCompound.getByte("Facing");
        if (!isDummy) {
            fertilizerAmount = nbtTagCompound.getInteger("FertilizerAmount");
            fertilizerBoost = nbtTagCompound.getFloat("FertilizerBoost");
            growth = nbtTagCompound.getFloat("Growth");
            energyStorage.readFromNBT(nbtTagCompound);
            tank.readFromNBT(nbtTagCompound);
            inventory = Utils.readInventory(nbtTagCompound.getTagList("inventory", 10), 7);
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbtTagCompound, boolean dataPacket) {
        nbtTagCompound.setBoolean("dummy", isDummy);
        nbtTagCompound.setByte("MultiblockPos", pos);
        nbtTagCompound.setByte("Facing", facingSide);
        if (!isDummy) {
            nbtTagCompound.setInteger("FertilizerAmount", fertilizerAmount);
            nbtTagCompound.setFloat("FertilizerBoost", fertilizerBoost);
            nbtTagCompound.setFloat("Growth", growth);
            energyStorage.writeToNBT(nbtTagCompound);
            tank.writeToNBT(nbtTagCompound);
            nbtTagCompound.setTag("inventory", Utils.writeInventory(inventory));
        }
    }

    public boolean outputSlotsFull() {
        int total = 0;
        for(int i = 3; i < 7; i++) {
            if(inventory[i] != null) {
                ItemStack s = inventory[i];
                total+=s.stackSize;
            }
        }
        return total>=256;
    }

    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int i, boolean b) {
        if(this.isDummy) {
            return mainTile().receiveEnergy(forgeDirection,i, b);
        } else {
            int energy = energyStorage.receiveEnergy(i,b);
            if(energy > 0) {
                worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
            return energy;
        }
    }

    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        if(this.isDummy)
            return mainTile().energyStorage.getEnergyStored();
        else
            return energyStorage.getEnergyStored();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if(isDummy || worldObj.isRemote)
            return;
        for (ItemStack stack : inventory) {
            if (stack != null) {
                EntityItem item = new EntityItem(getWorldObj(), xCoord + Math.random(), yCoord + Math.random() + 0.1, zCoord + Math.random());
                item.setEntityItemStack(stack);
                item.delayBeforeCanPickup = 10;
                getWorldObj().spawnEntityInWorld(item);
            }
        }
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        if(this.isDummy)
            return mainTile().energyStorage.getMaxEnergyStored();
        else
            return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection forgeDirection) {
       return this.pos == 2 && forgeDirection == ForgeDirection.UP;
    }

    @Override
    public int getSizeInventory() {
        return mainTile().inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if(this.isDummy) {
            this.mainTile().getStackInSlot(slot);
        } else {
            return slot < inventory.length ? inventory[slot] : null;
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if(this.isDummy) {
            this.mainTile().decrStackSize(slot,amount);
        } else {
            ItemStack stack = this.getStackInSlot(slot);
            if (stack != null) {
                if (stack.stackSize <= amount) {
                    this.setInventorySlotContents(slot,null);
                } else {
                    stack = stack.splitStack(amount);
                    if (stack.stackSize == 0) {
                        this.setInventorySlotContents(slot,null);
                    }
                }
            }
            markDirty();
            return stack;
        }
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if(this.isDummy) {
            mainTile().setInventorySlotContents(slot,stack);
        } else {
            this.inventory[slot] = stack;
            if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
                stack.stackSize = this.getInventoryStackLimit();
            }
        }
    }

    @Override
    public String getInventoryName() {
        return "IEGardenCloche";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(this.xCoord,yCoord,this.zCoord) <= 36.0;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if(slot > 2 && slot < 7) {
            return false;
        }
        return slot != 0 || GardenClocheRegistry.isValidFertilizer(stack);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if(this.isDummy) {
            return 0;
        } else {
             int filled = tank.fill(resource, doFill);
            if(filled > 0) {
                worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
            return filled;
        }
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        if(this.isDummy) {
            return false;
        } else {
            if(fluid != FluidRegistry.WATER)
                return false;
            ForgeDirection dir = ForgeDirection.getOrientation(facingSide).getOpposite();
            return dir == from;
        }
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        if(this.isDummy) {
            return new FluidTankInfo[0];
        }else {
            if(from != ForgeDirection.getOrientation(facingSide).getOpposite())
                return new FluidTankInfo[0];
            return new FluidTankInfo[]{tank.getInfo()};
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        //so rendering of crops doesn't stop when the lowest tile block is out of sight
        if(!isDummy)
            return AxisAlignedBB.getBoundingBox(xCoord,yCoord,zCoord,xCoord + 1,yCoord + 2,zCoord + 1);
        return super.getRenderBoundingBox();
    }
}
