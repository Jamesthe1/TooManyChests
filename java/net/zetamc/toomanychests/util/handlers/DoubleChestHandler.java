package net.zetamc.toomanychests.util.handlers;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.base.Objects;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.VanillaDoubleChestItemHandler;

import net.zetamc.toomanychests.blocks.tileentity.TileEntityCustomChest;

public class DoubleChestHandler extends WeakReference<TileEntityCustomChest> implements IItemHandlerModifiable {
    // Dummy cache value to signify that we have checked and definitely found no adjacent chests
    public static final DoubleChestHandler NO_ADJACENT_CHESTS_INSTANCE = new DoubleChestHandler (null, null, false, 0);
    private final boolean mainChestIsUpper;
    private final TileEntityCustomChest mainChest;
    private final int hashCode;
    private final int invSize;

	public DoubleChestHandler (@Nullable TileEntityCustomChest mainChest, @Nullable TileEntityCustomChest other, boolean mainChestIsUpper, int invSize) {
		super (other);
        this.mainChest = mainChest;
        this.mainChestIsUpper = mainChestIsUpper;
        hashCode = Objects.hashCode (mainChestIsUpper ? mainChest : other) * 31 + Objects.hashCode(!mainChestIsUpper ? mainChest : other);
        this.invSize = invSize;
	}
	
	@Nullable
    public static DoubleChestHandler get (TileEntityCustomChest chest) {
        World world = chest.getWorld ();
        BlockPos pos = chest.getPos ();
        if (world == null || pos == null || !world.isBlockLoaded (pos))
            return null; // Still loading

        Block blockType = chest.getBlockType ();

        EnumFacing[] horizontals = EnumFacing.HORIZONTALS;
        for (int i = horizontals.length - 1; i >= 0; i--) { // Use reverse order so we can return early
            EnumFacing enumfacing = horizontals[i];
            BlockPos blockpos = pos.offset(enumfacing);
            Block block = world.getBlockState(blockpos).getBlock ();

            if (block == blockType) {
                TileEntity otherTE = world.getTileEntity(blockpos);

                if (otherTE instanceof TileEntityCustomChest) {
                    TileEntityCustomChest otherChest = (TileEntityCustomChest) otherTE;
                    return new DoubleChestHandler (chest, otherChest, enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH, chest.invSize);
                }
            }
        }
        return NO_ADJACENT_CHESTS_INSTANCE; //All alone
    }

    @Nullable
    public TileEntityCustomChest getChest (boolean accessingUpper) {
        if (accessingUpper == mainChestIsUpper)
            return mainChest;
        else
            return getOtherChest ();
    }

    @Nullable
    private TileEntityCustomChest getOtherChest () {
        TileEntityCustomChest teCChest = get ();
        return teCChest != null && !teCChest.isInvalid() ? teCChest : null;
    }

    @Override
    public int getSlots () {
        return invSize * 2;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot (int slot) {
        boolean accessingUpperChest = slot < invSize;
        int targetSlot = accessingUpperChest ? slot : slot - invSize;
        TileEntityCustomChest chest = getChest (accessingUpperChest);
        return chest != null ? chest.getStackInSlot(targetSlot) : ItemStack.EMPTY;
    }

    @Override
    public void setStackInSlot (int slot, @Nonnull ItemStack stack) {
        boolean accessingUpperChest = slot < invSize;
        int targetSlot = accessingUpperChest ? slot : slot - invSize;
        TileEntityCustomChest chest = getChest (accessingUpperChest);
        if (chest != null) {
            IItemHandler singleHandler = chest.getSingleChestHandler();
            if (singleHandler instanceof IItemHandlerModifiable)
                ((IItemHandlerModifiable) singleHandler).setStackInSlot (targetSlot, stack);
        }

        chest = getChest (!accessingUpperChest);
        if (chest != null)
            chest.markDirty ();
    }

    @Override
    @Nonnull
    public ItemStack insertItem (int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        boolean accessingUpperChest = slot < invSize;
        int targetSlot = accessingUpperChest ? slot : slot - invSize;
        TileEntityCustomChest chest = getChest (accessingUpperChest);
        if (chest == null)
            return stack;

        int starting = stack.getCount ();
        ItemStack ret = chest.getSingleChestHandler().insertItem (targetSlot, stack, simulate);
        if (ret.getCount() != starting && !simulate) {
            chest = getChest(!accessingUpperChest);
            if (chest != null)
                chest.markDirty();
        }

        return ret;
    }

    @Override
    @Nonnull
    public ItemStack extractItem (int slot, int amount, boolean simulate) {
        boolean accessingUpperChest = slot < invSize;
        int targetSlot = accessingUpperChest ? slot : slot - invSize;
        TileEntityCustomChest chest = getChest (accessingUpperChest);
        if (chest == null)
            return ItemStack.EMPTY;

        ItemStack ret = chest.getSingleChestHandler().extractItem (targetSlot, amount, simulate);
        if (!ret.isEmpty() && !simulate) {
            chest = getChest (!accessingUpperChest);
            if (chest != null)
                chest.markDirty ();
        }

        return ret;
    }

    @Override
    public int getSlotLimit (int slot) {
        boolean accessingUpperChest = slot < invSize;
        return getChest(accessingUpperChest).getInventoryStackLimit ();
    }

    @Override
    public boolean isItemValid (int slot, @Nonnull ItemStack stack) {
        boolean accessingUpperChest = slot < invSize;
        int targetSlot = accessingUpperChest ? slot : slot - invSize;
        TileEntityCustomChest chest = getChest (accessingUpperChest);
        if (chest != null)
            return chest.getSingleChestHandler ().isItemValid (targetSlot, stack);
        
        return true;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o)
            return true;
        if (o == null || getClass () != o.getClass ())
            return false;

        DoubleChestHandler that = (DoubleChestHandler) o;

        if (hashCode != that.hashCode)
            return false;

        final TileEntityCustomChest otherChest = getOtherChest ();
        if (mainChestIsUpper == that.mainChestIsUpper)
            return Objects.equal(mainChest, that.mainChest) && Objects.equal (otherChest, that.getOtherChest());
        else
            return Objects.equal(mainChest, that.getOtherChest()) && Objects.equal (otherChest, that.mainChest);
    }

    @Override
    public int hashCode () {
        return hashCode;
    }

    public boolean needsRefresh () {
        if (this == NO_ADJACENT_CHESTS_INSTANCE)
            return false;
        TileEntityCustomChest tileEntityChest = get ();
        return tileEntityChest == null || tileEntityChest.isInvalid ();
    }
}
