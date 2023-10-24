package de.jxson.armorstand.api.enums;

public enum ArmorStandMetaData {

    IS_SMALL(0x01),
    HAS_ARMS(0x04),
    HAS_BASE_PLATE(0x08),
    IS_VISIBLE(0x10);

    ArmorStandMetaData(int bit) {}

}
