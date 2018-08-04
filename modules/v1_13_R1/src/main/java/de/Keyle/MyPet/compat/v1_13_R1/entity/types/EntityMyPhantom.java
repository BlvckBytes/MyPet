/*
 * This file is part of MyPet
 *
 * Copyright © 2011-2018 Keyle
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

package de.Keyle.MyPet.compat.v1_13_R1.entity.types;

import de.Keyle.MyPet.api.entity.EntitySize;
import de.Keyle.MyPet.api.entity.MyPet;
import de.Keyle.MyPet.api.entity.types.MyPhantom;
import de.Keyle.MyPet.compat.v1_13_R1.entity.EntityMyPet;
import de.Keyle.MyPet.compat.v1_13_R1.entity.ai.attack.MeleeAttack;
import net.minecraft.server.v1_13_R1.*;

@EntitySize(width = 0.51F, height = 0.51F)
public class EntityMyPhantom extends EntityMyPet {

    private static final DataWatcherObject<Integer> sizeWatcher = DataWatcher.a(EntityMyPhantom.class, DataWatcherRegistry.b);

    public EntityMyPhantom(World world, MyPet myPet) {
        super(EntityTypes.PHANTOM, world, myPet);
    }

    @Override
    protected String getDeathSound() {
        return "entity.phantom.death";
    }

    @Override
    protected String getHurtSound() {
        return "entity.phantom.hurt";
    }

    protected String getLivingSound() {
        return "entity.phantom.ambient";
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();

        this.datawatcher.register(sizeWatcher, 0);
    }

    @Override
    public void updateVisuals() {
        int size = Math.max(1, getMyPet().getSize());
        this.datawatcher.set(sizeWatcher, size);
        EntitySize es = EntityMyPhantom.class.getAnnotation(EntitySize.class);
        if (es != null) {
            this.setSize(es.width() * size, es.width() * size);
        }
        if (petPathfinderSelector != null && petPathfinderSelector.hasGoal("MeleeAttack")) {
            petPathfinderSelector.replaceGoal("MeleeAttack", new MeleeAttack(this, 0.1F, 3 + (getMyPet().getSize() * 0.2), 20));
        }
    }

    public MyPhantom getMyPet() {
        return (MyPhantom) myPet;
    }
}