package de.jxson.armorstand.api;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class ArmorStandImpl extends AbstractArmorStand {


    public static HashMap<Integer, ArmorStand> ARMORSTAND_STORE = new HashMap<>();

    public ArmorStandImpl()
    {
        super(true);
    }

    public void spawn(Location location) {
        if(!validateArmorStand()) return;
        setLocation(location);
        getAdapter().spawn(location);
        if(ARMORSTAND_STORE.containsKey(getId())) return;
        ARMORSTAND_STORE.put(getId(), this);
    }

    @Override
    public void addPassenger(Entity entity) {
        getAdapter().addPassenger(entity);
    }

    @Override
    public void removePassenger(Entity entity) {
        getAdapter().removePassenger(entity);
    }

    @Override
    public List<Entity> getPassengers() {
        return getAdapter().getPassengers();
    }

    @Override
    public void teleport(Location location) {
        getAdapter().teleport(location);
    }

    @Override
    public void setHeadRotation(float yaw, float pitch) {
        getAdapter().setHeadRotation(yaw, pitch);
    }

    @Override
    public void updateMetaData() {
        getAdapter().updateMetaData();
    }

    @Override
    public void hideFromPlayers(Player... players) {
        getAdapter().hideFromPlayers(players);
    }

    @Override
    public void exposeToPlayers(Player... players) {
        getAdapter().exposeToPlayers(players);
    }

    @Override
    public List<Player> getVisibiltyModifierPlayers() {
        return getAdapter().getVisibiltyModifierPlayers();
    }

    @Override
    public void refresh() {
        getAdapter().refresh();
    }

    @Override
    public void destroy() {
        getAdapter().destroy();
        ARMORSTAND_STORE.remove(getId());
    }
}
