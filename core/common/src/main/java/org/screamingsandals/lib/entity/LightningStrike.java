package org.screamingsandals.lib.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class LightningStrike {
    private final EntityBasic entity;
    private boolean effect;
}
