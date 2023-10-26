package de.jxson.armorstand.listener;

import de.jxson.armorstand.api.ArmorStand;
import de.jxson.armorstand.api.enums.EquipmentSlot;
import de.jxson.armorstand.api.event.ArmorStandInteractEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class InteractWithArmorStand implements Listener {

    @EventHandler
    public void onNMSInteract(ArmorStandInteractEvent event)
    {
        ArmorStand armorStand = event.getArmorStand();
        Player player = event.getPlayer();
        ItemStack interactedItem = event.getPlayer().getInventory().getItemInMainHand();
        System.out.println(interactedItem);
        if(interactedItem.getType() == Material.STICK)
        {
            Location location = armorStand.getLocation().clone();
            location.setDirection(player.getLocation().subtract(location).toVector());
            armorStand.setHeadRotation(location.getYaw(), location.getPitch());
        }

        if(interactedItem.getType() == Material.CLOCK)
        {
            if(!armorStand.getPassengers().contains(event.getPlayer()))
                armorStand.addPassenger(event.getPlayer());
            else armorStand.removePassenger(event.getPlayer());
        }

        if(interactedItem.getType() == Material.IRON_AXE)
        {
            armorStand.setEquipment(player.getInventory().getHelmet(), EquipmentSlot.HELMET);
            armorStand.setEquipment(player.getInventory().getChestplate(), EquipmentSlot.CHESTPLATE);
            armorStand.setEquipment(player.getInventory().getLeggings(), EquipmentSlot.LEGGINGS);
            armorStand.setEquipment(player.getInventory().getBoots(), EquipmentSlot.BOOTS);
            armorStand.setEquipment(player.getInventory().getItemInMainHand(), EquipmentSlot.MAIN_HAND);
            armorStand.setEquipment(player.getInventory().getItemInOffHand(), EquipmentSlot.OFF_HAND);
        }

    }

}
