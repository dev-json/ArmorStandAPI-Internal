package de.jxson.armorstand.utils;

import org.bukkit.Bukkit;

public class Version {

    public static String getServerVersionString()
    {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public static ServerVersion getServerVersion()
    {
        for(ServerVersion serverVersion : ServerVersion.values())
            if(serverVersion.getVersion().equals(getServerVersionString()))
                return serverVersion;
        return ServerVersion.UNKNOWN;
    }

}
