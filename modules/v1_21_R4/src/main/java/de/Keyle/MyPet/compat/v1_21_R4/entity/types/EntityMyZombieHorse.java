/*
 * This file is part of MyPet
 *
 * Copyright © 2011-2020 Keyle
 * MyPet is licensed under the GNU Lesser General Public License.
 *
 * MyPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.compat.v1_21_R4.entity.types;

import de.Keyle.MyPet.api.Configuration;
import de.Keyle.MyPet.api.entity.EntitySize;
import de.Keyle.MyPet.api.entity.MyPet;
import de.Keyle.MyPet.api.entity.types.MyZombieHorse;
import de.Keyle.MyPet.compat.v1_21_R4.entity.EntityMyPet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.craftbukkit.v1_21_R4.inventory.CraftItemStack;

@EntitySize(width = 1.4F, height = 1.6F)
public class EntityMyZombieHorse extends EntityMyPet {

	protected static final EntityDataAccessor<Boolean> AGE_WATCHER = SynchedEntityData.defineId(EntityMyZombieHorse.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Byte> SADDLE_CHEST_WATCHER = SynchedEntityData.defineId(EntityMyZombieHorse.class, EntityDataSerializers.BYTE);

	int soundCounter = 0;
	int rearCounter = -1;

	public EntityMyZombieHorse(Level world, MyPet myPet) {
		super(world, myPet);
		indirectRiding = true;
	}

	/**
	 * Possible visual horse effects:
	 * 4 saddle
	 * 8 chest
	 * 32 head down
	 * 64 rear
	 * 128 mouth open
	 */
	protected void applyVisual(int value, boolean flag) {
		int i = this.getEntityData().get(SADDLE_CHEST_WATCHER);
		if (flag) {
			this.getEntityData().set(SADDLE_CHEST_WATCHER, (byte) (i | value));
		} else {
			this.getEntityData().set(SADDLE_CHEST_WATCHER, (byte) (i & (~value)));
		}
	}

	@Override
	public boolean attack(Entity entity) {
		boolean flag = false;
		try {
			flag = super.attack(entity);
			if (flag) {
				applyVisual(64, true);
				rearCounter = 10;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	protected String getMyPetDeathSound() {
		return "entity.zombie_horse.death";
	}

	@Override
	protected String getHurtSound() {
		return "entity.zombie_horse.hurt";
	}

	@Override
	protected String getLivingSound() {
		return "entity.zombie_horse.ambient";
	}


	@Override
	public InteractionResult handlePlayerInteraction(Player entityhuman, InteractionHand enumhand, ItemStack itemStack) {
		if (super.handlePlayerInteraction(entityhuman, enumhand, itemStack).consumesAction()) {
			return InteractionResult.CONSUME;
		}

		if (itemStack != null && canUseItem()) {
			if (itemStack.getItem() == Items.SADDLE && !getMyPet().hasSaddle() && getOwner().getPlayer().isSneaking() && canEquip()) {
				getMyPet().setSaddle(CraftItemStack.asBukkitCopy(itemStack));
				if (itemStack != ItemStack.EMPTY && !entityhuman.getAbilities().instabuild) {
					itemStack.shrink(1);
					if (itemStack.getCount() <= 0) {
						entityhuman.getInventory().setItem(entityhuman.getInventory().getSelectedSlot(), ItemStack.EMPTY);
					}
				}
				return InteractionResult.CONSUME;
			} else if (itemStack.getItem() == Items.SHEARS && getOwner().getPlayer().isSneaking() && canEquip()) {
				if (getMyPet().hasSaddle()) {
					ItemEntity entityitem = new ItemEntity(this.level(), this.getX(), this.getY() + 1, this.getZ(), CraftItemStack.asNMSCopy(getMyPet().getSaddle()));
					entityitem.pickupDelay = 10;
					entityitem.setDeltaMovement(entityitem.getDeltaMovement().add(0, this.random.nextFloat() * 0.05F, 0));
					this.level().addFreshEntity(entityitem);
				}

				makeSound("entity.sheep.shear", 1.0F, 1.0F);
				getMyPet().setSaddle(null);
				if (itemStack != ItemStack.EMPTY && !entityhuman.getAbilities().instabuild) {
					try {
						itemStack.hurtAndBreak(1, entityhuman, getSlotForHand(enumhand));
					} catch (Error e) {
						// TODO REMOVE
					}
				}

				return InteractionResult.CONSUME;
			} else if (Configuration.MyPet.ZombieHorse.GROW_UP_ITEM.compare(itemStack) && getMyPet().isBaby() && getOwner().getPlayer().isSneaking()) {
				if (itemStack != ItemStack.EMPTY && !entityhuman.getAbilities().instabuild) {
					itemStack.shrink(1);
					if (itemStack.getCount() <= 0) {
						entityhuman.getInventory().setItem(entityhuman.getInventory().getSelectedSlot(), ItemStack.EMPTY);
					}
				}
				getMyPet().setBaby(false);
				return InteractionResult.CONSUME;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(AGE_WATCHER, false);
		builder.define(SADDLE_CHEST_WATCHER, (byte) 0);
	}

	@Override
	public void updateVisuals() {
		this.getEntityData().set(AGE_WATCHER, getMyPet().isBaby());
		applyVisual(4, getMyPet().hasSaddle());
	}

	@Override
	public void onLivingUpdate() {
		boolean oldRiding = hasRider;
		super.onLivingUpdate();
		if (!hasRider) {
			if (rearCounter > -1 && rearCounter-- == 0) {
				applyVisual(64, false);
				rearCounter = -1;
			}
		}
		if (oldRiding != hasRider) {
			if (hasRider) {
				applyVisual(4, true);
			} else {
				applyVisual(4, getMyPet().hasSaddle());
			}
		}
	}

	@Override
	public void playStepSound(BlockPos blockposition, BlockState blockdata) {
		if (!blockdata.liquid()) {
			BlockState blockdataUp = this.level().getBlockState(blockposition.above());
			SoundType soundeffecttype = blockdata.getSoundType();
			if (blockdataUp.getBlock() == Blocks.SNOW) {
				soundeffecttype = blockdata.getSoundType();
			}
			if (this.isVehicle()) {
				++this.soundCounter;
				if (this.soundCounter > 5 && this.soundCounter % 3 == 0) {
					this.playSound(SoundEvents.HORSE_GALLOP, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
				} else if (this.soundCounter <= 5) {
					this.playSound(SoundEvents.HORSE_STEP_WOOD, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
				}
			} else if (!blockdata.liquid()) {
				this.soundCounter += 1;
				playSound(SoundEvents.HORSE_STEP_WOOD, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
			} else {
				playSound(SoundEvents.HORSE_STEP, soundeffecttype.getVolume() * 0.15F, soundeffecttype.getPitch());
			}
		}
	}

	@Override
	public MyZombieHorse getMyPet() {
		return (MyZombieHorse) myPet;
	}
}
