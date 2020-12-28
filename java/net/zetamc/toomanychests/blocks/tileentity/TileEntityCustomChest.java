package net.zetamc.toomanychests.blocks.tileentity;

import java.lang.reflect.Constructor;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.items.*;

import net.zetamc.toomanychests.blocks.ChestBase;
import net.zetamc.toomanychests.util.Reference;
import net.zetamc.toomanychests.util.handlers.DoubleChestHandler;

// Copying data from TileEntityChest because we can't override the private inventory variable properly
public class TileEntityCustomChest extends TileEntityLockableLoot implements ITickable {
    private static final RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>> REGISTRY = new RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>> ();
    
	public int invSize = 27;
	private int stackLimit = 64;
	public String regName = "custom_chest";
	public boolean canBeDouble = true;
	
	private NonNullList<ItemStack> chestContents;
	
    /** Determines if the check for adjacent chests has taken place. */
    public boolean adjacentChestChecked;
    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityCustomChest adjacentChestZNeg;
    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityCustomChest adjacentChestXPos;
    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityCustomChest adjacentChestXNeg;
    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityCustomChest adjacentChestZPos;
    /** The current angle of the lid (between 0 and 1) */
    public float lidAngle;
    /** The angle of the lid last tick */
    public float prevLidAngle;
    /** The number of players currently using this chest */
    public int numPlayersUsing;
    /** Server sync counter (once per 20 ticks) */
    private int ticksSinceSync;
    private BlockChest.Type cachedChestType;
    
    // Needed to prevent NoSuchMethodException
    public TileEntityCustomChest () {
    	
    }
    
    private void getTraces () {
    	StackTraceElement[] stackTraces = new Throwable ().getStackTrace ();
		String traceStr = "getTraces() called; investigating:";
		int traces = 0;
		for (StackTraceElement st : stackTraces) {
			traceStr += "\n\\ [" + st.getLineNumber () + "] ";
			traceStr += st.getClassName () + "." + st.getMethodName () + "()";
			traces++;
		}
		
		Reference.LOGGER.info (traceStr);
    }
	
	public TileEntityCustomChest (int invSize, int stackLimit, String regName, boolean canBeDouble) {
		this.invSize = invSize;
		this.stackLimit = stackLimit;
		this.regName = regName;
		this.canBeDouble = canBeDouble;
		
		chestContents = NonNullList.withSize (invSize, ItemStack.EMPTY);
	}

	@Override
	public int getSizeInventory () {
		return invSize;
	}

	@Override
	public boolean isEmpty () {
		for (ItemStack stack : chestContents)
			if (!stack.isEmpty ()) return false;
		
		return true;
	}

	@Override
	public int getInventoryStackLimit () {
		return stackLimit;
	}

	@Override
	public String getName () {
		return this.hasCustomName () ? this.customName : "container." + regName;
	}
	
    public static void registerFixesChest (DataFixer fixer) {
        fixer.registerWalker (FixTypes.BLOCK_ENTITY, new ItemStackDataLists (TileEntityChest.class, new String[] {"Items"}));
    }

	@Override
	public Container createContainer (InventoryPlayer playerInventory, EntityPlayer player) {
		return new ContainerChest (playerInventory, this, player);
	}

	@Override
	public String getGuiID () {
		return Reference.MOD_ID + ':' + regName;
	}
	
	public void updateContainingBlockInfo () {
        super.updateContainingBlockInfo ();
        adjacentChestChecked = false;
        dcHandler = null;
    }

    @SuppressWarnings ("incomplete-switch")
    private void setNeighbor (TileEntityCustomChest chestTe, EnumFacing side) {
        if (chestTe.isInvalid ())
            adjacentChestChecked = false;
        else if (adjacentChestChecked) {
            switch (side) {
                case NORTH:
                    if (adjacentChestZNeg != chestTe)
                        adjacentChestChecked = false;
                    
                    break;
                case SOUTH:
                    if (adjacentChestZPos != chestTe)
                        adjacentChestChecked = false;

                    break;
                case EAST:
                    if (adjacentChestXPos != chestTe)
                        adjacentChestChecked = false;

                    break;
                case WEST:
                    if (adjacentChestXNeg != chestTe)
                        adjacentChestChecked = false;
                    
                    break;
            }
        }
    }
	
	public void checkForAdjacentChests () {
        if (this.adjacentChestChecked || !canBeDouble) return;
        
        if (this.world == null || !this.world.isAreaLoaded (this.pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbors

    	Reference.LOGGER.info (regName + " is searching for new chests...");
    	
        this.adjacentChestChecked = true;
        this.adjacentChestXNeg = this.getAdjacentChest (EnumFacing.WEST);
        this.adjacentChestXPos = this.getAdjacentChest (EnumFacing.EAST);
        this.adjacentChestZNeg = this.getAdjacentChest (EnumFacing.NORTH);
        this.adjacentChestZPos = this.getAdjacentChest (EnumFacing.SOUTH);
    }

    @Nullable
    protected TileEntityCustomChest getAdjacentChest (EnumFacing side) {
        BlockPos blockpos = this.pos.offset (side);

        if (this.isChestAt (blockpos)) {
            TileEntity tentity1 = this.world.getTileEntity (blockpos);

            if (tentity1 instanceof TileEntityCustomChest && ((TileEntityCustomChest)tentity1).regName == regName) {
                TileEntityCustomChest tentity2 = (TileEntityCustomChest)tentity1;
                tentity2.setNeighbor (this, side.getOpposite ());
                
            	Reference.LOGGER.info (regName + " found a new chest in the " + side + " direction! (" + tentity2.regName + ")");
                return tentity2;
            }
        }

        return null;
    }

    private boolean isChestAt (BlockPos pos) {
        if (this.world == null)
            return false;
        
        Block block = this.world.getBlockState (pos).getBlock ();
        return block instanceof ChestBase && ((ChestBase)block).chestType == getChestType ();
    }

	@Override
	public void update () {
		checkForAdjacentChests ();
		
        int i = pos.getX ();
        int j = pos.getY ();
        int k = pos.getZ ();
        ++this.ticksSinceSync;

        if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0) {
            this.numPlayersUsing = 0;
            float f = 5.0F;

            for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB (EntityPlayer.class, new AxisAlignedBB ((double)((float)i - 5.0F), (double)((float)j - 5.0F), (double)((float)k - 5.0F), (double)((float)(i + 1) + 5.0F), (double)((float)(j + 1) + 5.0F), (double)((float)(k + 1) + 5.0F)))) {
                if (entityplayer.openContainer instanceof ContainerChest) {
                    IInventory iinventory = ((ContainerChest)entityplayer.openContainer).getLowerChestInventory ();

                    if (iinventory == this || iinventory instanceof InventoryLargeChest && ((InventoryLargeChest)iinventory).isPartOfLargeChest (this))
                        ++this.numPlayersUsing;
                }
            }
        }

        this.prevLidAngle = this.lidAngle;
        float f1 = 0.1F;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
            double d1 = (double)i + 0.5D;
            double d2 = (double)k + 0.5D;

            if (this.adjacentChestZPos != null)
                d2 += 0.5D;

            if (this.adjacentChestXPos != null)
                d1 += 0.5D;

            this.world.playSound ((EntityPlayer)null, d1, (double)j + 0.5D, d2, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat () * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float f2 = this.lidAngle;

            if (this.numPlayersUsing > 0)
                this.lidAngle += 0.1F;
            else
                this.lidAngle -= 0.1F;

            if (this.lidAngle > 1.0F)
                this.lidAngle = 1.0F;

            float f3 = 0.5F;

            if (this.lidAngle < 0.5F && f2 >= 0.5F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
                double d3 = (double)i + 0.5D;
                double d0 = (double)k + 0.5D;

                if (this.adjacentChestZPos != null)
                    d0 += 0.5D;

                if (this.adjacentChestXPos != null)
                    d3 += 0.5D;

                this.world.playSound ((EntityPlayer)null, d3, (double)j + 0.5D, d0, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat () * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
                this.lidAngle = 0.0F;
        }
	}
	
	/**
     * See {@link Block#eventReceived} for more information. This must return true serverside before it is called
     * clientside.
     */
    public boolean receiveClientEvent (int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else
            return super.receiveClientEvent (id, type);
    }

    public void openInventory (EntityPlayer player) {
    	if (player.isSpectator ()) return;
    	
        if (this.numPlayersUsing < 0)
            this.numPlayersUsing = 0;

        ++this.numPlayersUsing;
        this.world.addBlockEvent (this.pos, this.getBlockType (), 1, this.numPlayersUsing);
        this.world.notifyNeighborsOfStateChange (this.pos, this.getBlockType (), false);

        if (this.getChestType () == BlockChest.Type.TRAP)
            this.world.notifyNeighborsOfStateChange (this.pos.down (), this.getBlockType (), false);
    }

    public void closeInventory (EntityPlayer player) {
        if (player.isSpectator () || !(this.getBlockType () instanceof ChestBase)) return;
        
        --this.numPlayersUsing;
        this.world.addBlockEvent (this.pos, this.getBlockType (), 1, this.numPlayersUsing);
        this.world.notifyNeighborsOfStateChange (this.pos, this.getBlockType (), false);

        if (this.getChestType() == BlockChest.Type.TRAP)
            this.world.notifyNeighborsOfStateChange (this.pos.down (), this.getBlockType (), false);
    }

    public DoubleChestHandler dcHandler;

    //@SuppressWarnings ("unchecked")
    @Override
    @Nullable
    public <T> T getCapability (Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if(dcHandler == null || dcHandler.needsRefresh ())
                dcHandler = dcHandler.get (this);
            if (dcHandler != null && dcHandler != dcHandler.NO_ADJACENT_CHESTS_INSTANCE)
                return (T) dcHandler;
        }
        return super.getCapability (capability, facing);
    }

    public IItemHandler getSingleChestHandler () {
        return super.getCapability (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    public void invalidate () {
        super.invalidate ();
        this.updateContainingBlockInfo ();
        this.checkForAdjacentChests ();
    }

    public BlockChest.Type getChestType () {
        if (this.cachedChestType == null) {
            if (this.world == null || !(this.getBlockType () instanceof ChestBase))
                return BlockChest.Type.BASIC;

            this.cachedChestType = ((ChestBase)this.getBlockType ()).chestType;
        }

        return this.cachedChestType;
    }

	@Override
	protected NonNullList<ItemStack> getItems () {
		return chestContents;
	}
	
	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT (compound);
		
		// invSize needed first for next statement
        invSize = compound.getInteger ("invSize");
		chestContents = NonNullList.<ItemStack>withSize (invSize, ItemStack.EMPTY);
		if (!this.checkLootAndRead (compound))
            ItemStackHelper.loadAllItems (compound, chestContents);

        if (compound.hasKey ("CustomName", 8))
            this.customName = compound.getString ("CustomName");
        
        stackLimit = compound.getInteger ("stackLimit");
        // If statement needed to avoid the blank chest error
        if (regName == "custom_chest") {
        	regName = compound.getString ("regName");
        	canBeDouble = compound.getBoolean ("canBeDouble");
        }
	}
	
	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
        super.writeToNBT (compound);

        if (!this.checkLootAndWrite (compound))
            ItemStackHelper.saveAllItems (compound, this.chestContents);

        if (this.hasCustomName ())
            compound.setString ("CustomName", this.customName);
        
        compound.setInteger ("invSize", invSize);
        compound.setInteger ("stackLimit", stackLimit);
        compound.setString ("regName", regName);
        compound.setBoolean ("canBeDouble", canBeDouble);

        return compound;
    }
	
	/*@Override
	public LockCode getLockCode () {
		getTraces ();
		return super.getLockCode ();
	}*/
}
