package de.jxson.armorstand.nms.v1_20_R1;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import de.jxson.armorstand.ArmorStandAPIPlugin;
import de.jxson.armorstand.api.ArmorStand;
import de.jxson.armorstand.api.ArmorStandImpl;
import de.jxson.armorstand.api.enums.EquipmentSlot;
import de.jxson.armorstand.nms.AbstractVersionAdapter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArmorStandAdapterImpl extends AbstractVersionAdapter {

    public ArmorStandAdapterImpl(ArmorStand armorStand) {
        super(armorStand);
    }

    @Override
    public void spawn(Location location) {
        PacketContainer entitySpawnPacketContainer = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        entitySpawnPacketContainer.getModifier().writeDefaults();
        entitySpawnPacketContainer.getIntegers().write(0, getArmorStand().getId());
        entitySpawnPacketContainer.getUUIDs().write(0, getArmorStand().getUniqueId());
        entitySpawnPacketContainer.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
        entitySpawnPacketContainer.getDoubles()
                .write(0, getArmorStand().getLocation().getX())
                .write(1, getArmorStand().getLocation().getY())
                .write(2, getArmorStand().getLocation().getZ());
        entitySpawnPacketContainer.getBytes()
                .write(0, (byte) (getArmorStand().getLocation().getYaw() * 256.0F / 360.0F))
                .write(1, (byte) (getArmorStand().getLocation().getPitch() * 256.0F / 360.0F));
        getVisibiltyModifierPlayers().forEach(player -> ProtocolLibrary.getProtocolManager().sendServerPacket(player, entitySpawnPacketContainer));
        updateMetaData();
    }

    @Override
    public void destroy() {
        //In case we still have some passengers, we remove them first.
        getPassengers().forEach(this::removePassenger);
        PacketContainer destroyEntityPacketContainer = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        destroyEntityPacketContainer.getModifier().writeDefaults();
        destroyEntityPacketContainer.getIntegerArrays().write(0, new int[] {getArmorStand().getId() });
        getVisibiltyModifierPlayers().forEach(player -> ProtocolLibrary.getProtocolManager().sendServerPacket(player, destroyEntityPacketContainer));
    }

    @Override
    public void addPassenger(Entity entity) {
        getPassengers().add(entity);
        PacketContainer passengerContainer = new PacketContainer(PacketType.Play.Server.MOUNT);
        passengerContainer.getIntegers().write(0, getArmorStand().getId());
        passengerContainer.getIntegerArrays().write(0, new int[]{entity.getEntityId()});
        getVisibiltyModifierPlayers().forEach(player -> ProtocolLibrary.getProtocolManager().sendServerPacket(player, passengerContainer));
    }

    @Override
    public void removePassenger(Entity entity) {
        getPassengers().remove(entity);
        PacketContainer passengerContainer = new PacketContainer(PacketType.Play.Server.MOUNT);
        passengerContainer.getIntegers().write(0, getArmorStand().getId());
        passengerContainer.getIntegerArrays().write(0, new int[]{0});
        getVisibiltyModifierPlayers().forEach(player -> ProtocolLibrary.getProtocolManager().sendServerPacket(player, passengerContainer));
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
            teleportationPacketContainer.getModifier().writeDefaults();
            teleportationPacketContainer.getDoubles()
                .write(0, targetLocation.getX())
                .write(1, targetLocation.getY())
                .write(2, targetLocation.getZ());
        }
        else
        {
            teleportationPacketContainer = new PacketContainer(PacketType.Play.Server.POSITION);
            teleportationPacketContainer.getModifier().writeDefaults();
            teleportationPacketContainer.getIntegers().write(0, getArmorStand().getId());
            teleportationPacketContainer.getDoubles()
                    .write(0, targetLocation.getX())
                    .write(1, targetLocation.getY())
                    .write(2, targetLocation.getZ());
        }
        //Set the headrotation afterward
        setHeadRotation(getArmorStand().getYaw(), targetLocation.getPitch());
    }

    @Override
    public void setHeadRotation(float yaw, float pitch) {
        headRotation = new float[]{yaw, pitch};
        PacketContainer headRotationPacketContainer = new PacketContainer(PacketType.Play.Server.ENTITY_HEAD_ROTATION);
        headRotationPacketContainer.getModifier().writeDefaults();
        headRotationPacketContainer.getIntegers().write(0, getArmorStand().getId());
        headRotationPacketContainer.getBytes()
                .write(0, (byte) (yaw * 256.0F / 360.0F));

        PacketContainer headAnglePacketContainer = new PacketContainer(PacketType.Play.Server.ENTITY_LOOK);
        headAnglePacketContainer.getModifier().writeDefaults();
        headAnglePacketContainer.getIntegers().write(0, getArmorStand().getId());
        headAnglePacketContainer.getBytes()
                .write(0, (byte) (yaw * 256.0F / 360.0F))
                .write(1, (byte) (pitch * 256.0D / 360.0D));

        getVisibiltyModifierPlayers().forEach(player -> {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, headRotationPacketContainer);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, headAnglePacketContainer);
        });
    }

    @Override
    public void updateMetaData() {

        PacketContainer equipmentContainer = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
        equipmentContainer.getModifier().writeDefaults();
        equipmentContainer.getIntegers().write(0, getArmorStand().getId());

        List<Pair<EnumWrappers.ItemSlot, ItemStack>> equipmentList = new ArrayList<>();
        equipmentList.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, getArmorStand().getEquipment(EquipmentSlot.HELMET)));
        equipmentList.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, getArmorStand().getEquipment(EquipmentSlot.CHESTPLATE)));
        equipmentList.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, getArmorStand().getEquipment(EquipmentSlot.LEGGINGS)));
        equipmentList.add(new Pair<>(EnumWrappers.ItemSlot.FEET, getArmorStand().getEquipment(EquipmentSlot.BOOTS)));
        equipmentList.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, getArmorStand().getEquipment(EquipmentSlot.MAIN_HAND)));
        equipmentList.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, getArmorStand().getEquipment(EquipmentSlot.OFF_HAND)));

        equipmentContainer.getSlotStackPairLists().write(0, equipmentList);


        PacketContainer entityMetadataContainer = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        entityMetadataContainer.getModifier().writeDefaults();
        entityMetadataContainer.getIntegers().write(0, getArmorStand().getId());
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();
        watcher.setObject(0, WrappedDataWatcher.Registry.get(Byte.class), ((ArmorStandImpl) getArmorStand()).getBitMask());

        entityMetadataContainer.getDataValueCollectionModifier().write(0, wrappedDataValueList);
        getVisibiltyModifierPlayers().forEach(player ->
        {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, entityMetadataContainer);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, equipmentContainer);
        });
    }

    @Override
    public void refresh() {
        destroy();
        spawn(getArmorStand().getLocation());
    }
}
