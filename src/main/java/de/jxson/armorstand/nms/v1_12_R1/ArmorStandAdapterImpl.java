package de.jxson.armorstand.nms.v1_12_R1;

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

    }

    @Override
    public void removePassenger(Entity entity) {

    }

    @Override
    public List<Entity> getPassengers() {
        return null;
    }

    @Override
    public void teleport(Location location) {

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
