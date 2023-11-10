package de.jxson.armorstand.nms;

import de.jxson.armorstand.api.ArmorStand;
import de.jxson.armorstand.api.adapter.ArmorStandVersionAdapter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractVersionAdapter implements ArmorStandVersionAdapter {

    private ArmorStand armorStand;
    private List<Entity> passengers;
    private List<Player> exposedForPlayers;

    protected float[] headRotation;

    public AbstractVersionAdapter(ArmorStand armorStand)
    {
        this.armorStand = armorStand;
        this.passengers = new ArrayList<>();
        this.exposedForPlayers = new ArrayList<>();
        this.headRotation = new float[]{0f, 0f};
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    @Override
    public void exposeToPlayers(Player... players) {
        exposedForPlayers.addAll(Arrays.asList(players));
    }

    @Override
    public void hideFromPlayers(Player... players) {
        exposedForPlayers.removeAll(Arrays.asList(players));
    }

    @Override
    public List<Player> getVisibiltyModifierPlayers() {
        return exposedForPlayers;
    }

    @Override
    public float[] getHeadRotation() {
        return headRotation;
    }

    @Override
    public List<Entity> getPassengers() {
        return passengers;
    }

}
