package de.jxson.armorstand.api.properties;

import org.bukkit.entity.Player;

import java.util.List;

public interface VisibilityModifier {

    void hideFromPlayers(Player... players);
    void exposeToPlayers(Player... players);

    List<Player> getVisibiltyModifierPlayers();
}
