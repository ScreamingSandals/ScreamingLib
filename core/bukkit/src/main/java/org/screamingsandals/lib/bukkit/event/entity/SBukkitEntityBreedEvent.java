package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.event.entity.EntityBreedEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityBreedEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.item.builder.ItemFactory;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityBreedEvent implements SEntityBreedEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityBreedEvent event;

    // Internal cache
    private EntityBasic entity;
    private EntityBasic mother;
    private EntityBasic father;
    @Nullable
    private EntityBasic breeder;
    private boolean breederCached;
    @Nullable
    private Item bredWith;
    private boolean bredWithCached;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public EntityBasic getMother() {
        if (mother == null) {
            mother = EntityMapper.wrapEntity(event.getMother()).orElseThrow();
        }
        return mother;
    }

    @Override
    public EntityBasic getFather() {
        if (father == null) {
            father = EntityMapper.wrapEntity(event.getFather()).orElseThrow();
        }
        return father;
    }

    @Override
    @Nullable
    public EntityBasic getBreeder() {
        if (!breederCached) {
            if (event.getBreeder() != null) {
                breeder = EntityMapper.wrapEntity(event.getBreeder()).orElseThrow();
            }
            breederCached = true;
        }
        return breeder;
    }

    @Override
    @Nullable
    public Item getBredWith() {
        if (!bredWithCached) {
            if (event.getBredWith() != null) {
                bredWith = ItemFactory.build(event.getBredWith()).orElseThrow();
            }
            bredWithCached = true;
        }
        return bredWith;
    }

    @Override
    public int getExperience() {
        return event.getExperience();
    }

    @Override
    public void setExperience(int experience) {
        event.setExperience(experience);
    }
}