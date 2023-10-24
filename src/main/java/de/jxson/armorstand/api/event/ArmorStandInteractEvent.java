package de.jxson.armorstand.api.event;

import de.jxson.armorstand.api.ArmorStand;
import de.jxson.armorstand.api.ArmorStandImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArmorStandInteractEvent extends Event {

    private ArmorStand armorStand;
    private Player player;

    /**
     * 0: interact
     * 1: attack
     * 2: interact at
     */
    private int interactionType;

    private static final HandlerList HANDLERS = new HandlerList();

    public ArmorStandInteractEvent(Player player, ArmorStand armorStand, int interactionType)
    {
        this.player = player;
        this.armorStand = armorStand;
        this.interactionType = interactionType;
    }

    public int getInteractionType() {
        return interactionType;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public Player getPlayer() {
        return player;
    }
}