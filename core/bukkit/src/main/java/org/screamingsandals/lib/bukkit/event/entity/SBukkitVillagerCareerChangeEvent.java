package org.screamingsandals.lib.bukkit.event.entity;

import lombok.*;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SVillagerCareerChangeEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitVillagerCareerChangeEvent implements SVillagerCareerChangeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final VillagerCareerChangeEvent event;

    // Internal cache
    private EntityBasic entity;
    private ChangeReason reason;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public Profession getProfession() {
        return Profession.valueOf(event.getProfession().name());
    }

    @Override
    public void setProfession(Profession profession) {
        event.setProfession(Villager.Profession.valueOf(profession.name()));
    }

    @Override
    public ChangeReason getReason() {
        if (reason == null) {
            reason = ChangeReason.valueOf(event.getReason().name());
        }
        return reason;
    }
}
