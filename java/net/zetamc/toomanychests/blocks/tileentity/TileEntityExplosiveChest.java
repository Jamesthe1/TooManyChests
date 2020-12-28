package net.zetamc.toomanychests.blocks.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.zetamc.toomanychests.blocks.ExplosiveChest;

public class TileEntityExplosiveChest extends TileEntityCustomChest {
	public int maxTime;
	private boolean ignited = false;
	private int cd;
	
	public TileEntityExplosiveChest () {
		
	}

	// maxTime is in ticks
	public TileEntityExplosiveChest (int invSize, int stackLimit, String regName, boolean canBeDouble, int maxTime) {
		super (invSize, stackLimit, regName, canBeDouble);
		this.maxTime = maxTime;
		this.cd = maxTime;
	}
	
	public void defuse () {
		ignited = false;
		cd = maxTime;
	}
	
	public boolean isFused () {
		return ignited;
	}
	
	public int getFuseTimer () {
		return cd;
	}
	
	public void fuse () {
		ignited = true;
	}
	
	@Override
	public void update () {
		super.update ();
		if (ignited) {
			cd -= 1;
			
			int fullSecondMark = (int)Math.ceil ((float)cd/20)*20;
			if (cd <= 5*20 && cd > 0 && fullSecondMark == cd)
				world.playSound ((EntityPlayer)null, pos, SoundEvents.BLOCK_NOTE_HAT, SoundCategory.BLOCKS, 1, 1);
			
			if (cd <= 0)
				explode ();
		}
	}
	
	public void explode () {
		ExplosiveChest block = (ExplosiveChest)getBlockType ();
		IBlockState state = world.getBlockState (pos);
		block.explode (world, pos, state, (EntityLivingBase)null);
	}
	
	@Override
	public NBTTagCompound writeToNBT (NBTTagCompound compound) {
        super.writeToNBT (compound);
        
        compound.setInteger ("maxTime", maxTime);
        compound.setBoolean ("ignited", ignited);
        compound.setInteger ("cd", cd);
        
        return compound;
	}
	
	@Override
	public void readFromNBT (NBTTagCompound compound) {
		super.readFromNBT (compound);
		
		//if (maxTime > 0) return;
		maxTime = compound.getInteger ("maxTime");
		ignited = compound.getBoolean ("ignited");
		cd = compound.getInteger ("cd");
	}
}
