package de.jxson.armorstand.utils;

/**
 * For further information see:
 * - Legacy: <a href="https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-legacy/">...</a>
 * - 1.10+ : <a href="https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-1-10-1-15/">...</a>
 * - 1.16+ : <a href="https://www.spigotmc.org/wiki/spigot-nms-and-minecraft-versions-1-16/">...</a>
 */
public enum ServerVersion
{

    V_1_12_R1("v1_12_R1"),
    V_1_20_R1("v1_20_R1"),
    UNKNOWN("UNKNOWN");

    private String version;

    ServerVersion(String versionStr) {
        this.version = versionStr;
    }

    public String getVersion() {
        return version;
    }
}
