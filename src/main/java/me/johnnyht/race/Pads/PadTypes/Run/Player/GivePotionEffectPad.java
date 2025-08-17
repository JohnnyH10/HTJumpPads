package me.johnnyht.race.Pads.PadTypes.Run.Player;

import me.johnnyht.race.CommandManager.commands.SendPlayersMessages;
import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import me.johnnyht.race.Sound.PadSound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GivePotionEffectPad implements PadAction {
    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {

        if (isJump) return; // Only trigger on walk-over
        //if (HtRacePads.getInstance().getNoxesiumPlayers().contains(player.getUniqueId())) return;

        HtRacePads.playerCoolDownPad(player.getUniqueId(), 5, HtRacePads.plugin);

        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Usage: givepotioneffect <effect> <durationTicks|infinite> [amplifier]");
            return;
        }

        String effectName = args[1].toUpperCase();
        String durationInput = args[2];
        int duration;

        if (durationInput.equalsIgnoreCase("infinite")) {
            duration = Integer.MAX_VALUE;
        } else {
            try {
                duration = Integer.parseInt(durationInput);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid duration: " + durationInput);
                return;
            }
        }

        int amplifier = 1; // Default amplifier

        try {
            amplifier = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid amplifier: " + args[3]);
            return;
        }


        PotionEffectType effectType = PotionEffectType.getByName(effectName);
        if (effectType == null) {
            player.sendMessage(ChatColor.RED + "Invalid potion effect: " + effectName);
            return;
        }

        player.addPotionEffect(new PotionEffect(effectType, duration, amplifier));
        PadSound.playSoundAtPlayer(player, "minecraft:entity.player.splash.high_speed");

        if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.GREEN + "Applied potion effect: " + effectType.getName() +
                    (duration == Integer.MAX_VALUE ? " infinitely" : " for " + duration + " ticks") +
                    " with amplifier " + amplifier + ".");
        }
    }
}
