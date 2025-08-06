package me.johnnyht.race.Pads.PadTypes.Run.Vehicles;

import me.johnnyht.race.CommandManager.commands.SendPlayersMessages;
import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PigPad implements PadAction {
    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {
        if (isJump) return; // Pig pad only triggers on walk-over
        HtRacePads.playerCoolDownPad(player.getUniqueId(), 5, HtRacePads.plugin);

        double speed;

        try {
            speed = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            HtRacePads.plugin.getLogger().severe("Failed to parse double pig speed value for pig pad at location " + loc + ". Input: " + args[1]);
            return;
        }

        if (player.getVehicle() instanceof Pig) {
            player.sendMessage(ChatColor.RED + "You are already riding a pig!");
            return;
        }

        Pig pig = player.getWorld().spawn(player.getLocation(), Pig.class);
        pig.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(speed);
        pig.setSaddle(true);
        pig.addPassenger(player);

        player.getInventory().addItem(new ItemStack(Material.CARROT_ON_A_STICK));
        if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.YELLOW + "Spawned a pig and gave you a carrot on a stick!");
        }
    }
}
