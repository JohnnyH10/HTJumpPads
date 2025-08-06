package me.johnnyht.race.Pads.PadTypes.Run.Vehicles;

import me.johnnyht.race.CommandManager.commands.SendPlayersMessages;
import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Camel;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CamelPad implements PadAction {
    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {
        if (isJump) return; // Camel pad only triggers on walk-over
        HtRacePads.playerCoolDownPad(player.getUniqueId(), 5, HtRacePads.plugin);

        double speed, jump;

        try {
            speed = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            HtRacePads.plugin.getLogger().severe("Failed to parse double camel speed value for camel pad at location " + loc + ". Input: " + args[1]);
            return;
        }

        try {
            jump = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            HtRacePads.plugin.getLogger().severe("Failed to parse double camel jump value for camel pad at location " + loc + ". Input: " + args[2]);
            return;
        }

        if (player.getVehicle() instanceof Camel) {
            player.sendMessage(ChatColor.RED + "You are already riding a camel!");
            return;
        }

        Camel camel = player.getWorld().spawn(player.getLocation(), Camel.class);
        camel.setTamed(true);
        camel.setAdult();
        camel.setCustomName(ChatColor.GOLD + player.getName() + "'s Camel");
        camel.setCustomNameVisible(true);
        camel.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        camel.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(speed);
        camel.getAttribute(Attribute.JUMP_STRENGTH).setBaseValue(jump);
        camel.addPassenger(player);

        if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.YELLOW + "Spawned a camel!");
        }
    }
}
