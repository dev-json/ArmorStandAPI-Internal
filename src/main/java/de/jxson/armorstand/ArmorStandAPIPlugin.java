package de.jxson.armorstand;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import de.jxson.armorstand.api.ArmorStandImpl;
import de.jxson.armorstand.commands.ArmorStandCommand;
import de.jxson.armorstand.api.event.ArmorStandInteractEvent;
import de.jxson.armorstand.listener.InteractWithArmorStand;
import de.jxson.armorstand.utils.Version;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ArmorStandAPIPlugin extends JavaPlugin {

    private ArmorStandAPIPlugin javaPlugin;

    @Override
    public void onEnable() {
        javaPlugin = this;
        Bukkit.getConsoleSender().sendMessage("Started Plugin with Version: " + Version.getServerVersion());
        getCommand("as").setExecutor(new ArmorStandCommand());

        Bukkit.getPluginManager().registerEvents(new InteractWithArmorStand(), this);

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.USE_ENTITY) {
                    PacketContainer packetContainer = event.getPacket();
                    int actionType = 0;

                    if (packetContainer.getIntegers().read(0) == 1)
                        actionType = 1;
                    if (packetContainer.getIntegers().read(0) == 2)
                        actionType = 2;

                    ArmorStandInteractEvent armorStandInteractEvent = new ArmorStandInteractEvent(
                            event.getPlayer(),
                            ArmorStandImpl.ARMORSTAND_STORE.get(packetContainer.getIntegers().read(0)),
                            actionType);
                   Bukkit.getScheduler().runTask(getJavaPlugin(), () ->  Bukkit.getPluginManager().callEvent(armorStandInteractEvent));

                }
            }
        });

    }

    @Override
    public void onDisable() {

    }

    public ArmorStandAPIPlugin getJavaPlugin() {
        return javaPlugin;
    }
}
