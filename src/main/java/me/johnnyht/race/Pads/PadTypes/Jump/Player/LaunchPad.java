package me.johnnyht.race.Pads.PadTypes.Jump.Player;

import me.johnnyht.race.CommandManager.commands.SendPlayersMessages;
import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import me.johnnyht.race.Sound.PadSound;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LaunchPad implements PadAction {
    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {
        if (!isJump) return; // Launch pad only triggers on jump

        HtRacePads.getInstance().playerCoolDownPad(player.getUniqueId(), 5, HtRacePads.getInstance());

        double x, y;
        try {
            x = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            HtRacePads.getInstance().getLogger().severe("Failed to parse double X value for launch pad at location " + loc + ". Input: " + args[1]);
            return;
        }

        try {
            y = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            HtRacePads.getLog().severe("Failed to parse double Y value for launch pad at location " + loc + ". Input: " + args[2]);
            return;
        }

        Location playerEyeLocation = player.getEyeLocation();
        player.setVelocity(playerEyeLocation.getDirection().setY(0).normalize().multiply(x).setY(y));
        if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
            player.sendMessage("§aHigh Jump! + " + x + " " + y);
        }
        player.spawnParticle(Particle.CLOUD, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.05);
        PadSound.playSoundAtPlayer(player, "minecraft:entity.ender_dragon.flap", 1.0f, 1.0f);
    }
}