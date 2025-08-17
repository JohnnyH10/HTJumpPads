package me.johnnyht.race.Pads.PadTypes.Run.Vehicles;

import me.johnnyht.race.CommandManager.commands.SendPlayersMessages;
import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import me.johnnyht.race.Sound.PadSound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class IceBoatPad implements PadAction {
    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {
        if (isJump) return; // Iceboat pad only triggers on walk-over
        HtRacePads.playerCoolDownPad(player.getUniqueId(), 5, HtRacePads.plugin);

        if (player.getVehicle() instanceof Boat) {
            player.sendMessage(ChatColor.RED + "You are already in a boat!");
            return;
        }

        Location spawnLoc = player.getLocation().clone();
        spawnLoc.setY(spawnLoc.getY() + 1); // Spawn slightly above player to prevent immediate collision issues

        Entity entity = player.getWorld().spawnEntity(spawnLoc, EntityType.ACACIA_BOAT); // You can choose any boat type
        if (entity instanceof Boat boat) {
            boat.setInvulnerable(true);
            boat.setCustomName(ChatColor.AQUA + player.getName() + "'s Ice Boat");
            boat.setCustomNameVisible(true);
            boat.addPassenger(player);
            PadSound.playSoundAtPlayer(player, "entity.item.pickup");
            if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.AQUA + "Spawned an ice boat!");
            }
        } else {
            if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Failed to spawn boat.");
            }
        }
    }
}
