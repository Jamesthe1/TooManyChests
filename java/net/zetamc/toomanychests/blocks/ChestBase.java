package net.zetamc.toomanychests.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.zetamc.toomanychests.Main;
import net.zetamc.toomanychests.blocks.tileentity.TileEntityCustomChest;
import net.zetamc.toomanychests.init.*;
import net.zetamc.toomanychests.util.IModelLoad;
import net.zetamc.toomanychests.util.Reference;

public class ChestBase extends BlockContainer implements IModelLoad {
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    protected static final AxisAlignedBB NORTH_CHEST_AABB = new AxisAlignedBB (0.0625D, 0.0D, 0.0D, 0.9375D, 0.9375D, 0.9375D);
    protected static final AxisAlignedBB SOUTH_CHEST_AABB = new AxisAlignedBB (0.0625D, 0.0D, 0.0625D, 0.9375D, 0.9375D, 1.0D);
    protected static final AxisAlignedBB WEST_CHEST_AABB = new AxisAlignedBB (0.0D, 0.0D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);
    protected static final AxisAlignedBB EAST_CHEST_AABB = new AxisAlignedBB (0.0625D, 0.0D, 0.0625D, 1.0D, 0.9375D, 0.9375D);
    protected static final AxisAlignedBB NOT_CONNECTED_AABB = new AxisAlignedBB (0.0625D, 0.0D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);

    public final BlockChest.Type chestType;
	public final boolean canBeDouble;
    
	final int guiId;
	final int invSize;
	final int stackSize;
	final String regName;
	
	public ChestBase (String name, SoundType soundType, Material material, int guiId, int invSize, int stackSize, boolean isTrapped, String tool, float hardness, float blastResistance, boolean canBeDouble) {
		super (material);
		this.guiId = guiId;
		this.invSize = invSize;
		this.regName = name;
		this.stackSize = stackSize;
		this.canBeDouble = canBeDouble;
		
		chestType = isTrapped ? BlockChest.Type.TRAP : BlockChest.Type.BASIC;
		
		setTranslationKey (name);
		setRegistryName (name);
		setCreativeTab (ModItems.TAB_CHESTS);
		setSoundType (soundType);
		
		setHardness (hardness);
		if (hardness >= 99) setBlockUnbreakable ();
		if (tool != "none") setHarvestLevel (tool, 0);
		setResistance (blastResistance);
		
		ModBlocks.BLOCKS.add (this);
		ModItems.ITEMS.add (new ItemBlock (this).setRegistryName (name));
	}
	
	// Cannot use switch-case as it does not support class comparisons
	public AxisAlignedBB getBoundingBox (IBlockState state, IBlockAccess source, BlockPos pos) {
		if (!canBeDouble) return NOT_CONNECTED_AABB;
		
        if (source.getBlockState (pos.north ()).getBlock () == this)
            return NORTH_CHEST_AABB;
        else if (source.getBlockState (pos.south ()).getBlock () == this)
            return SOUTH_CHEST_AABB;
        else if (source.getBlockState (pos.west ()).getBlock () == this)
            return WEST_CHEST_AABB;
        else
            return source.getBlockState (pos.east ()).getBlock () == this ? EAST_CHEST_AABB : NOT_CONNECTED_AABB;
    }

	@Override
	public TileEntity createNewTileEntity (World world, int meta) {
		return new TileEntityCustomChest (invSize, stackSize, regName, canBeDouble);
	}
	
	public void neighborChanged (IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
	    TileEntity tileentity = worldIn.getTileEntity (pos);

	    if (tileentity instanceof TileEntityCustomChest)
	    	tileentity.updateContainingBlockInfo ();
	}
	
	public boolean canPlaceBlockAt (World worldIn, BlockPos pos) {
		if (!canBeDouble) return true;
		
        int i = 0;
        BlockPos blockpos = pos.west ();
        BlockPos blockpos1 = pos.east ();
        BlockPos blockpos2 = pos.north ();
        BlockPos blockpos3 = pos.south ();

        if (worldIn.getBlockState (blockpos).getBlock () == this) {
            if (isDoubleChest (worldIn, blockpos))
                return false;

            ++i;
        }

        if (worldIn.getBlockState (blockpos1).getBlock () == this) {
            if (isDoubleChest (worldIn, blockpos1))
                return false;

            ++i;
        }

        if (worldIn.getBlockState (blockpos2).getBlock () == this) {
            if (isDoubleChest (worldIn, blockpos2))
                return false;

            ++i;
        }

        if (worldIn.getBlockState (blockpos3).getBlock () == this) {
            if (isDoubleChest (worldIn, blockpos3))
                return false;

            ++i;
        }

        return i <= 1;
    }

    private boolean isDoubleChest (World worldIn, BlockPos pos) {
        if (worldIn.getBlockState (pos).getBlock () != this)
            return false;
        else {
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                if (worldIn.getBlockState (pos.offset (enumfacing)).getBlock () == this && ((ChestBase)worldIn.getBlockState (pos.offset (enumfacing)).getBlock ()).canBeDouble)
                    return true;

            return false;
        }
    }
	
	public IBlockState checkForSurroundingChests (World worldIn, BlockPos pos, IBlockState state) {
        if (worldIn.isRemote || !canBeDouble)
            return state;
        else {
            IBlockState iblockstate = worldIn.getBlockState (pos.north ());
            IBlockState iblockstate1 = worldIn.getBlockState (pos.south ());
            IBlockState iblockstate2 = worldIn.getBlockState (pos.west ());
            IBlockState iblockstate3 = worldIn.getBlockState (pos.east ());
            EnumFacing enumfacing = (EnumFacing)state.getValue (FACING);

            if (iblockstate.getBlock () != this && iblockstate1.getBlock () != this) {
                boolean flag = iblockstate.isFullBlock ();
                boolean flag1 = iblockstate1.isFullBlock ();

                if (iblockstate2.getBlock () == this || iblockstate3.getBlock () == this) {
                    BlockPos blockpos1 = iblockstate2.getBlock () == this ? pos.west () : pos.east ();
                    IBlockState iblockstate7 = worldIn.getBlockState (blockpos1.north ());
                    IBlockState iblockstate6 = worldIn.getBlockState (blockpos1.south ());
                    enumfacing = EnumFacing.SOUTH;
                    EnumFacing enumfacing2;

                    if (iblockstate2.getBlock () == this)
                        enumfacing2 = (EnumFacing)iblockstate2.getValue (FACING);
                    else
                        enumfacing2 = (EnumFacing)iblockstate3.getValue (FACING);

                    if (enumfacing2 == EnumFacing.NORTH)
                        enumfacing = EnumFacing.NORTH;

                    if ((flag || iblockstate7.isFullBlock ()) && !flag1 && !iblockstate6.isFullBlock ())
                        enumfacing = EnumFacing.SOUTH;

                    if ((flag1 || iblockstate6.isFullBlock ()) && !flag && !iblockstate7.isFullBlock ())
                        enumfacing = EnumFacing.NORTH;
                }
            } else {
                BlockPos blockpos = iblockstate.getBlock () == this ? pos.north () : pos.south ();
                IBlockState iblockstate4 = worldIn.getBlockState (blockpos.west ());
                IBlockState iblockstate5 = worldIn.getBlockState (blockpos.east ());
                enumfacing = EnumFacing.EAST;
                EnumFacing enumfacing1;

                if (iblockstate.getBlock () == this)
                    enumfacing1 = (EnumFacing)iblockstate.getValue (FACING);
                else
                    enumfacing1 = (EnumFacing)iblockstate1.getValue (FACING);

                if (enumfacing1 == EnumFacing.WEST)
                    enumfacing = EnumFacing.WEST;

                if ((iblockstate2.isFullBlock () || iblockstate4.isFullBlock ()) && !iblockstate3.isFullBlock () && !iblockstate5.isFullBlock ())
                    enumfacing = EnumFacing.EAST;

                if ((iblockstate3.isFullBlock () || iblockstate5.isFullBlock ()) && !iblockstate2.isFullBlock () && !iblockstate4.isFullBlock ())
                    enumfacing = EnumFacing.WEST;
            }

            state = state.withProperty (FACING, enumfacing);
            worldIn.setBlockState (pos, state, 3);
            return state;
        }
    }
	
	public void onBlockAdded (World world, BlockPos pos, IBlockState state) {
		if (!canBeDouble) return;
		
        this.checkForSurroundingChests (world, pos, state);

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            BlockPos blockpos = pos.offset (enumfacing);
            IBlockState iblockstate = world.getBlockState (blockpos);

            if (iblockstate.getBlock () == this)
                this.checkForSurroundingChests (world, blockpos, iblockstate);
        }
    }
	
	@Override
	public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			ILockableContainer ilockablecontainer = this.getLockableContainer (world, pos);
			
			if (ilockablecontainer != null) {
				player.displayGUIChest (ilockablecontainer);
				//player.openGui (Main.instance, guiId, world, pos.getX (), pos.getY (), pos.getZ ());
				
				if (chestType == BlockChest.Type.BASIC)
					player.addStat (StatList.CHEST_OPENED);
				else
					player.addStat (StatList.TRAPPED_CHEST_TRIGGERED);
			}
		}
		
		return true;
	}
	
	@Override
	public void breakBlock (World world, BlockPos pos, IBlockState state) {
		TileEntityCustomChest tentity = (TileEntityCustomChest)world.getTileEntity (pos);
		InventoryHelper.dropInventoryItems (world, pos, tentity);
		super.breakBlock (world, pos, state);
	}
	
	@Override
	public void onBlockPlacedBy (World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		EnumFacing enumfacing = EnumFacing.byHorizontalIndex (MathHelper.floor ((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite ();
        state = state.withProperty (FACING, enumfacing);
        
		if (stack.hasDisplayName ()) {
			TileEntity tentity = world.getTileEntity (pos);
			if (tentity instanceof TileEntityCustomChest)
				((TileEntityCustomChest)tentity).setCustomName (stack.getDisplayName ());
		}
		
        BlockPos blockpos = pos.north ();
        BlockPos blockpos1 = pos.south ();
        BlockPos blockpos2 = pos.west ();
        BlockPos blockpos3 = pos.east ();
        boolean flag = this == world.getBlockState (blockpos).getBlock ();
        boolean flag1 = this == world.getBlockState (blockpos1).getBlock ();
        boolean flag2 = this == world.getBlockState (blockpos2).getBlock ();
        boolean flag3 = this == world.getBlockState (blockpos3).getBlock ();

        if (!flag && !flag1 && !flag2 && !flag3 || !canBeDouble)
            world.setBlockState (pos, state, 3);
        else if (enumfacing.getAxis () != EnumFacing.Axis.X || !flag && !flag1) {
            if (enumfacing.getAxis () == EnumFacing.Axis.Z && (flag2 || flag3)) {
                if (flag2)
                    world.setBlockState (blockpos2, state, 3);
                else
                    world.setBlockState (blockpos3, state, 3);

                world.setBlockState (pos, state, 3);
            }
        } else {
            if (flag)
                world.setBlockState (blockpos, state, 3);
            else
                world.setBlockState (blockpos1, state, 3);

            world.setBlockState (pos, state, 3);
        }
	}
	
	@Override
	public TileEntity createTileEntity (World world, IBlockState state) {
		return new TileEntityCustomChest (invSize, stackSize, regName, canBeDouble);
	}
	
	@Override
	public EnumBlockRenderType getRenderType (IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	@Override
	public boolean isFullBlock (IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube (IBlockState state) {
		return false;
	}
	
	@SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress (IBlockState state) {
        return true;
    }
	
	@Override
	public boolean isOpaqueCube (IBlockState state) {
		return false;
	}
	
    @Nullable
    public ILockableContainer getLockableContainer (World world, BlockPos pos) {
        return getContainer (world, pos, false);
    }
    
    @Nullable
    public ILockableContainer getContainer (World world, BlockPos pos, boolean allowBlocking) {
        TileEntity tileentity = world.getTileEntity (pos);

        if (!(tileentity instanceof TileEntityCustomChest))
            return null;
        else {
            ILockableContainer ilockablecontainer = (TileEntityCustomChest)tileentity;

            if (!allowBlocking && this.isBlocked (world, pos))
                return null;
            else {
            	if (canBeDouble) {
	                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
	                    BlockPos blockpos = pos.offset (enumfacing);
	                    Block block = world.getBlockState (blockpos).getBlock ();
	
	                    if (block == this) {
	                        if (!allowBlocking && this.isBlocked (world, blockpos)) // Forge: fix MC-99321
	                            return null;
	
	                        TileEntity tileentity1 = world.getTileEntity (blockpos);
	
	                        if (tileentity1 instanceof TileEntityCustomChest) {
	                            if (enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH)
	                                ilockablecontainer = new InventoryLargeChest ("container." + regName + "_large", ilockablecontainer, (TileEntityCustomChest)tileentity1);
	                            else
	                                ilockablecontainer = new InventoryLargeChest ("container." + regName + "_large", (TileEntityCustomChest)tileentity1, ilockablecontainer);
	                        }
	                    }
	                }
            	}

                return ilockablecontainer;
            }
        }
    }
    
    public boolean canProvidePower (IBlockState state) {
        return this.chestType == BlockChest.Type.TRAP;
    }
    
    public int getWeakPower (IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (!blockState.canProvidePower ())
            return 0;
        else {
            int i = 0;
            TileEntity tileentity = blockAccess.getTileEntity (pos);

            if (tileentity instanceof TileEntityCustomChest)
                i = ((TileEntityCustomChest)tileentity).numPlayersUsing;

            return MathHelper.clamp (i, 0, 15);
        }
    }
    
    public int getStrongPower (IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.UP ? blockState.getWeakPower (blockAccess, pos, side) : 0;
    }
    
    public IBlockState getStateForPlacement (World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState ().withProperty (FACING, placer.getHorizontalFacing ());
    }
    
    public IBlockState correctFacing (World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = null;

        for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
            IBlockState iblockstate = worldIn.getBlockState (pos.offset (enumfacing1));

            if (iblockstate.getBlock () == this)
                return state;

            if (iblockstate.isFullBlock ()) {
                if (enumfacing != null) {
                    enumfacing = null;
                    break;
                }

                enumfacing = enumfacing1;
            }
        }

        if (enumfacing != null)
            return state.withProperty (FACING, enumfacing.getOpposite ());
        else {
            EnumFacing enumfacing2 = (EnumFacing)state.getValue (FACING);

            if (worldIn.getBlockState (pos.offset (enumfacing2)).isFullBlock ())
                enumfacing2 = enumfacing2.getOpposite ();

            if (worldIn.getBlockState (pos.offset (enumfacing2)).isFullBlock ())
                enumfacing2 = enumfacing2.rotateY ();

            if (worldIn.getBlockState (pos.offset (enumfacing2)).isFullBlock ())
                enumfacing2 = enumfacing2.getOpposite ();

            return state.withProperty (FACING, enumfacing2);
        }
    }
    
    private boolean isBlocked (World worldIn, BlockPos pos) {
        return this.isBelowSolidBlock (worldIn, pos) || this.isOcelotSittingOnChest (worldIn, pos);
    }
    
    private boolean isBelowSolidBlock (World worldIn, BlockPos pos) {
        return worldIn.getBlockState (pos.up ()).doesSideBlockChestOpening (worldIn, pos.up (), EnumFacing.DOWN);
    }

    private boolean isOcelotSittingOnChest (World worldIn, BlockPos pos) {
        for (Entity entity : worldIn.getEntitiesWithinAABB (EntityOcelot.class, new AxisAlignedBB ((double)pos.getX (), (double)(pos.getY () + 1), (double)pos.getZ (), (double)(pos.getX () + 1), (double)(pos.getY () + 2), (double)(pos.getZ () + 1)))) {
            EntityOcelot entityocelot = (EntityOcelot)entity;

            if (entityocelot.isSitting ())
                return true;
        }

        return false;
    }
    
    public boolean hasComparatorInputOverride (IBlockState state) {
        return true;
    }
    
    public int getComparatorInputOverride (IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstoneFromInventory (this.getLockableContainer (worldIn, pos));
    }
    
    public IBlockState getStateFromMeta (int meta) {
        EnumFacing enumfacing = EnumFacing.byIndex (meta);

        if (enumfacing.getAxis () == EnumFacing.Axis.Y)
            enumfacing = EnumFacing.NORTH;

        return this.getDefaultState ().withProperty (FACING, enumfacing);
    }
    
    public int getMetaFromState (IBlockState state) {
        return ((EnumFacing)state.getValue (FACING)).getIndex ();
    }
    
    public IBlockState withRotation (IBlockState state, Rotation rot) {
        return state.withProperty (FACING, rot.rotate ((EnumFacing)state.getValue (FACING)));
    }
    
    public IBlockState withMirror (IBlockState state, Mirror mirrorIn) {
        return state.withRotation (mirrorIn.toRotation ((EnumFacing)state.getValue (FACING)));
    }

    protected BlockStateContainer createBlockState () {
        return new BlockStateContainer (this, new IProperty[] {FACING});
    }
    
    /* ======================================== FORGE START =====================================*/
    public boolean rotateBlock (World world, BlockPos pos, EnumFacing axis) {
        return !isDoubleChest (world, pos) && super.rotateBlock (world, pos, axis);
    }
    
    
	@Override
	public void registerModels () {
		Main.proxy.registerItemRenderer (Item.getItemFromBlock (this), 0, "inventory");
	}
}
