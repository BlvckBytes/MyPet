/*
 * This file is part of MyPet
 *
 * Copyright (C) 2011-2016 Keyle
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

package de.Keyle.MyPet.compat.v1_9_R1.entity.types;

import de.Keyle.MyPet.api.entity.ActiveMyPet;
import de.Keyle.MyPet.api.entity.EntitySize;
import de.Keyle.MyPet.compat.v1_9_R1.entity.EntityMyPet;
import net.minecraft.server.v1_9_R1.*;

@EntitySize(width = 0.6F, height = 1.62F)
public class EntityMyWitch extends EntityMyPet {
    private static final DataWatcherObject<Boolean> watcher = DataWatcher.a(EntityWitch.class, DataWatcherRegistry.h);

    public EntityMyWitch(World world, ActiveMyPet myPet) {
        super(world, myPet);
    }

    @Override
    protected String getDeathSound() {
        return "entity.witch.death";
    }

    @Override
    protected String getHurtSound() {
        return "entity.witch.hurt";
    }

    protected String getLivingSound() {
        return "entity.witch.idle";
    }

    protected void initDatawatcher() {
        super.initDatawatcher();
        getDataWatcher().register(watcher, false); // N/A
    }
}