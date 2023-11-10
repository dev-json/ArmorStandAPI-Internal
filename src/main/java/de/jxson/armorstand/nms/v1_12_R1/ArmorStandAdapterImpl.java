package de.jxson.armorstand.nms.v1_12_R1;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import de.jxson.armorstand.ArmorStandAPIPlugin;
import de.jxson.armorstand.api.ArmorStand;
import de.jxson.armorstand.api.adapter.ArmorStandVersionAdapter;
import de.jxson.armorstand.nms.AbstractVersionAdapter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

public class ArmorStandAdapterImpl extends AbstractVersionAdapter {

    public ArmorStandAdapterImpl(ArmorStand armorStand) {
        super(armorStand);
    }

    @Override
    public void spawn(Location location) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void addPassenger(Entity entity) {
        getPassengers().add(entity);

    }

    @Override
    public void removePassenger(Entity entity) {
        getPassengers().remove(entity);
    }

    @Override
    public List<Entity> getPassengers() {
        return getPassengers();
    }

    @Override
    public void teleport(Location targetLocation) {
        Location currentLocation = getArmorStand().getLocation();

        //Load the chunk, if its not loaded yet
        if(!getArmorStand().getLocation().getWorld().getChunkAt(targetLocation).isLoaded())
            targetLocation.getChunk().addPluginChunkTicket(ArmorStandAPIPlugin.getPlugin(ArmorStandAPIPlugin.class));

        PacketContainer teleportationPacketContainer;
        //If the location is further away then 8 blocks, send a different packet

        if(targetLocation.distance(currentLocation) > 8)
        {
            teleportationPacketContainer = new PacketContainer(PacketType.Play.Server.ENTITY_TELEPORT);
        }
        else
        {
            teleportationPacketContainer = new PacketContainer(PacketType.Play.Server.POSITION);
        }

        //Set the headrotation afterward
        setHeadRotation(getArmorStand().getYaw(), targetLocation.getPitch());

        //Send teleportation
        getVisibiltyModifierPlayers().forEach(player -> ProtocolLibrary.getProtocolManager().sendServerPacket(player, teleportationPacketContainer));

    }

    @Override
    public void setHeadRotation(float yaw, float pitch) {

    }

    @Override
    public void updateMetaData() {

    }

    @Override
    public void refresh() {
        destroy();
        spawn(getArmorStand().getLocation());
    }
}
