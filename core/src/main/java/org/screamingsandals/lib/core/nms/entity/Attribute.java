package org.screamingsandals.lib.core.nms.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Attribute {
	MAX_HEALTH("MAX_HEALTH,field_111267_a"),
	FOLLOW_RANGE("FOLLOW_RANGE,field_111265_b"),
	KNOCKBACK_RESISTANCE("KNOCKBACK_RESISTANCE,field_111266_c"),
	MOVEMENT_SPEED("MOVEMENT_SPEED,field_111263_d"),
	FLYING_SPEED("FLYING_SPEED,field_193334_e"),
	ATTACK_DAMAGE("ATTACK_DAMAGE,field_111264_e"),
	ATTACK_KNOCKBACK("ATTACK_KNOCKBACK,field_221120_g"),
	ATTACK_SPEED("ATTACK_SPEED,field_188790_f"),
	ARMOR("ARMOR,field_188791_g"),
	ARMOR_TOUGHNESS("ARMOR_TOUGHNESS,field_189429_h"),
	LUCK("LUCK,field_188792_h");
	
	private String keys;
}
