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

package de.Keyle.MyPet.compat.v1_21_R2.entity.types;

import de.Keyle.MyPet.api.Configuration;
import de.Keyle.MyPet.api.entity.EntitySize;
import de.Keyle.MyPet.api.entity.MyPet;
import de.Keyle.MyPet.api.entity.types.MyBreeze;
import de.Keyle.MyPet.compat.v1_21_R2.entity.EntityMyPet;
import net.minecraft.world.level.Level;

//TODO Add jumping mechanic
@EntitySize(width = 0.6F, height = 1.7F)
public class EntityMyBreeze extends EntityMyPet {

	public EntityMyBreeze(Level world, MyPet myPet) {
		super(world, myPet);
	}

	@Override
	protected String getMyPetDeathSound() {
		return "entity.blaze.death";
	}

	@Override
	protected String getHurtSound() {
		return "entity.blaze.hurt";
	}

	@Override
	protected String getLivingSound() {
		return "entity.blaze.ambient";
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (Configuration.MyPet.Breeze.CAN_GLIDE) {
			if (!this.onGround && this.getDeltaMovement().y() < 0.0D) {
				this.setDeltaMovement(getDeltaMovement().multiply(1, 0.6D, 1));
			}
		}
	}

	@Override
	public MyBreeze getMyPet() {
		return (MyBreeze) myPet;
	}

	/**
	 * -> disable falldamage
	 */
	@Override
	public int calculateFallDamage(float f, float f1) {
		if (!Configuration.MyPet.Blaze.CAN_GLIDE) {
			super.calculateFallDamage(f, f1);
		}
		return 0;
	}

	@Override
	protected boolean checkInteractCooldown() {
		boolean val = super.checkInteractCooldown();
		this.interactCooldown = 5;
		return val;
	}
}
