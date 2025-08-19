package me.johnnyht.race.Pads.PadTypes.Run.Vehicles;

import me.johnnyht.race.CommandManager.commands.SendPlayersMessages;
import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import me.johnnyht.race.Sound.PadSound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Strider;
import org.bukkit.inventory.ItemStack;

public class StriderPad implements PadAction {
    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {
        if (isJump) return; // Strider pad only triggers on walk-over
        HtRacePads.playerCoolDownPad(player.getUniqueId(), 5, HtRacePads.plugin);

        if (player.getVehicle() instanceof Strider) {
            if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "You are already riding a strider!");
            }
            return;
        }

        double speed;
        try {
            speed = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            HtRacePads.plugin.getLogger().severe("Failed to parse strider double speed value for strider pad at location " + loc + ". Input: " + args[1]);
            return;
        }

        Strider strider = player.getWorld().spawn(player.getLocation(), Strider.class);
        strider.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(speed);
        strider.setSaddle(true);
        strider.addPassenger(player);
        PadSound.playSoundAtPlayer(player, "minecraft:entity.strider.ambient",1.0f,1.0f);

        player.getInventory().addItem(new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK));
        if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.YELLOW + "Spawned a strider and gave you a warped fungus on a stick!");
        }
    }
}
