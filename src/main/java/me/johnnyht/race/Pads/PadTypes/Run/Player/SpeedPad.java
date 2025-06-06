package me.johnnyht.race.Pads.PadTypes.Run.Player;

import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedPad implements PadAction {
    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {
        if (isJump) return; // Speed pad only triggers on walk-over

        HtRacePads.playerCoolDownPad(player.getUniqueId(), 5, HtRacePads.plugin);
        int time, amplifier;

        try {
            time = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            HtRacePads.plugin.getLogger().severe("Failed to parse int speed time value for speed pad at location " + loc + ". Input: " + args[1]);
            return;
        }

        try {
            amplifier = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            HtRacePads.plugin.getLogger().severe("Failed to parse int speed amplifier value for speed pad at location " + loc + ". Input: " + args[2]);
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, time, amplifier));
        player.sendMessage("§eSpeed...");
    }
}
