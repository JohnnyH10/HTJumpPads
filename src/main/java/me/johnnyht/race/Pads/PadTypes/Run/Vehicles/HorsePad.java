package me.johnnyht.race.Pads.PadTypes.Run.Vehicles;

import me.johnnyht.race.CommandManager.commands.SendPlayersMessages;
import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import me.johnnyht.race.Sound.PadSound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HorsePad implements PadAction {
    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {
        if (isJump) return; // Horse pad only triggers on walk-over
        HtRacePads.playerCoolDownPad(player.getUniqueId(), 5, HtRacePads.plugin);

        double speed, jump;

        try {
            speed = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            HtRacePads.plugin.getLogger().severe("Failed to parse double horse speed value for horse pad at location " + loc + ". Input: " + args[1]);
            return;
        }

        try {
            jump = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            HtRacePads.plugin.getLogger().severe("Failed to parse double horse jump value for horse pad at location " + loc + ". Input: " + args[2]);
            return;
        }

        if (player.getVehicle() instanceof Horse) {
            player.sendMessage(ChatColor.RED + "You are already riding a horse!");
            return;
        }

        Horse horse = player.getWorld().spawn(player.getLocation(), Horse.class);
        horse.setTamed(true);
        horse.setOwner(player);
        horse.setAdult();
        horse.setCustomName(ChatColor.GOLD + player.getName() + "'s Horse");
        horse.setCustomNameVisible(true);
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        horse.setDomestication(horse.getMaxDomestication());

        horse.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(speed);
        horse.getAttribute(Attribute.JUMP_STRENGTH).setBaseValue(jump);
        PadSound.playSoundAtPlayer(player, "minecraft:entity.horse.ambient",1.0f,1.0f);


        horse.addPassenger(player);
        if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.YELLOW + "Spawned a horse with speed: " + speed + " and jump: " + jump + "!");
        }
    }
}
