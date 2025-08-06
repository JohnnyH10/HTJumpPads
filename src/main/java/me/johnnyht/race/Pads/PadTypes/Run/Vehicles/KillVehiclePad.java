package me.johnnyht.race.Pads.PadTypes.Run.Vehicles;

import me.johnnyht.race.CommandManager.commands.SendPlayersMessages;
import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KillVehiclePad implements PadAction {
    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {
        if (isJump) return;
        HtRacePads.playerCoolDownPad(player.getUniqueId(), 5, HtRacePads.plugin);

        Entity vehicle = player.getVehicle();
        if (vehicle != null) {
            vehicle.remove();
            if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Your vehicle has been removed!");
            }
        } else {
            if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.YELLOW + "You're not in a vehicle.");
            }
        }
    }
}
