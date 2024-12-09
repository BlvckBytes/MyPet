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

package de.Keyle.MyPet.compat.v1_21_R3.entity.types;

import de.Keyle.MyPet.api.entity.EntitySize;
import de.Keyle.MyPet.api.entity.MyPet;
import de.Keyle.MyPet.compat.v1_21_R3.entity.EntityMyPet;
import net.minecraft.world.level.Level;

@EntitySize(width = 1.4F, height = 0.9F)
public class EntityMySpider extends EntityMyPet {

	public EntityMySpider(Level world, MyPet myPet) {
		super(world, myPet);
	}

	@Override
	protected String getMyPetDeathSound() {
		return "entity.spider.death";
	}

	@Override
	protected String getHurtSound() {
		return "entity.spider.hurt";
	}

	@Override
	protected String getLivingSound() {
		return "entity.spider.ambient";
	}

	@Override
	public void playPetStepSound() {
		makeSound("entity.spider.step", 0.15F, 1.0F);
	}
}
