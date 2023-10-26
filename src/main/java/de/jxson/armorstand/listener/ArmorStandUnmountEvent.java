package de.jxson.armorstand.listener;

import de.jxson.armorstand.api.ArmorStand;
import de.jxson.armorstand.api.ArmorStandImpl;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ArmorStandUnmountEvent implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event)
    {
        if(event.getItemDrop().getItemStack().getType() == Material.CLOCK)
        {
            for(ArmorStand existingArmorstands : ArmorStandImpl.ARMORSTAND_STORE.values())
                if(existingArmorstands.getPassengers().contains(event.getPlayer()))
                    existingArmorstands.removePassenger(event.getPlayer());
        }
    }

}
