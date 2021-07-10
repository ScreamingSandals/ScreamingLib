package org.screamingsandals.lib.bukkit.attribute;

import lombok.Data;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

@Data
public class BukkitItemAttribute {
    private final Attribute attribute;
    private final AttributeModifier attributeModifier;
}
