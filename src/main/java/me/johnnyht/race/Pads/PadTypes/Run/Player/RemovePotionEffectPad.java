package me.johnnyht.race.Pads.PadTypes.Run.Player;

import me.johnnyht.race.CommandManager.commands.SendPlayersMessages;
import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import java.util.Collection;
import java.util.ArrayList;

public class RemovePotionEffectPad implements PadAction {
    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {
        if (isJump) return;
        HtRacePads.playerCoolDownPad(player.getUniqueId(), 2, HtRacePads.plugin);

        if (player.getActivePotionEffects().isEmpty()) {
            if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.YELLOW + "You don't have any active potion effects.");
            }
            return;
        }

        Collection<PotionEffect> activeEffects = new ArrayList<>(player.getActivePotionEffects());

        for (PotionEffect effect : activeEffects) {
            player.removePotionEffect(effect.getType());
        }

        if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.GREEN + "All active potion effects removed.");
        }
    }
}