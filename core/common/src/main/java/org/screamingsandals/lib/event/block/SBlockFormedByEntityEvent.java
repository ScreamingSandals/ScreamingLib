package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.entity.EntityBasic;

public interface SBlockFormedByEntityEvent extends SBlockFormEvent {
    EntityBasic getProducer();
}
