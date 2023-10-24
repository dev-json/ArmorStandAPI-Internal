package de.jxson.armorstand.commands;

import de.jxson.armorstand.api.ArmorStand;
import de.jxson.armorstand.api.ArmorStandImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ArmorStandCommand implements CommandExecutor {

    private static ArmorStand armorStand;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(commandSender instanceof Player))
            return false;

        Player player = (Player) commandSender;

        if(armorStand == null)
        {
            armorStand = new ArmorStandImpl();
            System.out.printf("Created a new armorstand with the id %d%n", armorStand.getId());
        }

        if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("spawn"))
            {
                armorStand.spawn(player.getLocation());
                player.sendMessage("Spawned!");
                return true;
            }
            else if (args[0].equalsIgnoreCase("update"))
            {
                armorStand.refresh();
                player.sendMessage("Refreshed!");
            }
        }
        else if(args.length == 2)
        {
            if(args[0].equalsIgnoreCase("expose"))
            {
                armorStand.exposeToPlayers(Bukkit.getPlayer(args[1]));
                player.sendMessage("Please update with /as update");
            }
            else if(args[0].equalsIgnoreCase("hide"))
            {
                armorStand.hideFromPlayers(Bukkit.getPlayer(args[1]));
                player.sendMessage("Please update with /as update");
            }
        }

        return true;
    }
}
