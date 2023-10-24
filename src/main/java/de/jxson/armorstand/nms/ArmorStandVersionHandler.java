package de.jxson.armorstand.nms;

import de.jxson.armorstand.api.ArmorStand;
import de.jxson.armorstand.api.adapter.ArmorStandVersionAdapter;
import de.jxson.armorstand.utils.Version;

public class ArmorStandVersionHandler {

    public static ArmorStandVersionAdapter getAdapter(ArmorStand armorStand)
    {
        switch (Version.getServerVersion())
        {
            case V_1_12_R1 -> {
                return new de.jxson.armorstand.nms.v1_12_R1.ArmorStandAdapterImpl(armorStand);
            }
            case V_1_20_R1 -> {
                return new de.jxson.armorstand.nms.v1_20_R1.ArmorStandAdapterImpl(armorStand);
            }
            case UNKNOWN -> throw new RuntimeException("Unable to create adapter! Version not supported!");
        }
        throw new RuntimeException("Unable to create adapter! Version not supported!");
    }
}
