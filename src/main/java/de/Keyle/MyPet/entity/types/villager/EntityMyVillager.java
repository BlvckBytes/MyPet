/*
 * This file is part of MyPet
 *
 * Copyright (C) 2011-2013 Keyle
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

package de.Keyle.MyPet.entity.types.villager;

import de.Keyle.MyPet.entity.EntitySize;
import de.Keyle.MyPet.entity.types.EntityMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import net.minecraft.server.v1_6_R1.EntityHuman;
import net.minecraft.server.v1_6_R1.ItemStack;
import net.minecraft.server.v1_6_R1.World;
import org.bukkit.Material;

@EntitySize(width = 0.6F, height = 0.8F)
public class EntityMyVillager extends EntityMyPet
{
    public static int GROW_UP_ITEM = Material.POTION.getId();

    public EntityMyVillager(World world, MyPet myPet)
    {
        super(world, myPet);
    }

    public void setMyPet(MyPet myPet)
    {
        if (myPet != null)
        {
            super.setMyPet(myPet);

            this.setProfession(((MyVillager) myPet).getProfession());
            this.setBaby(((MyVillager) myPet).isBaby());
        }
    }

    public int getProfession()
    {
        return ((MyVillager) myPet).profession;
    }

    public void setProfession(int value)
    {
        this.datawatcher.watch(16, value);
        ((MyVillager) myPet).profession = value;
    }

    public boolean isBaby()
    {
        return ((MyVillager) myPet).isBaby;
    }

    @SuppressWarnings("boxing")
    public void setBaby(boolean flag)
    {
        if (flag)
        {
            this.datawatcher.watch(12, Integer.valueOf(Integer.MIN_VALUE));
        }
        else
        {
            this.datawatcher.watch(12, new Integer(0));
        }
        ((MyVillager) myPet).isBaby = flag;
    }

    // Obfuscated Methods -------------------------------------------------------------------------------------------

    protected void a()
    {
        super.a();
        this.datawatcher.a(12, new Integer(0)); // age
        this.datawatcher.a(16, new Integer(0)); // profession
    }

    public boolean a(EntityHuman entityhuman)
    {
        try
        {
            if (super.a(entityhuman))
            {
                return true;
            }

            ItemStack itemStack = entityhuman.inventory.getItemInHand();

            if (getOwner().equals(entityhuman) && itemStack != null)
            {
                if (itemStack.id == GROW_UP_ITEM)
                {
                    if (isBaby())
                    {
                        if (!entityhuman.abilities.canInstantlyBuild)
                        {
                            if (--itemStack.count <= 0)
                            {
                                entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, null);
                            }
                        }
                        this.setBaby(false);
                        return true;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns the sound that is played when the MyPet get hurt
     */
    protected String aK()
    {
        return "mob.villager.defaulthurt";
    }

    /**
     * Returns the sound that is played when the MyPet dies
     */
    protected String aL()
    {
        return "mob.villager.defaultdeath";
    }

    /**
     * Returns the default sound of the MyPet
     */
    protected String r()
    {
        return !playIdleSound() ? "" : "mob.villager.default";
    }
}