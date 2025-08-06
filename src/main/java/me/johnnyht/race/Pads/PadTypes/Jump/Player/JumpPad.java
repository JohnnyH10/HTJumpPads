package me.johnnyht.race.Pads.PadTypes.Jump.Player;

import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JumpPad implements PadAction {
    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {
        if (!isJump) return; // Launch pad only triggers on jump
        if (HtRacePads.uuidHasNox.contains(player.getUniqueId())) return;

        int y;
        try {
            y = Integer.parseInt(args[1]);
        } catch (Exception e) {
            HtRacePads.plugin.getLogger().severe("Invalid jump pad value at " + loc + "|| y is apparently =" + Integer.parseInt(args[1]));
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 10, y));


        if (player.hasPotionEffect(PotionEffectType.JUMP_BOOST)) {
            player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.05);
        }
        player.removePotionEffect(PotionEffectType.JUMP_BOOST);
        HtRacePads.playerCoolDownPad(player.getUniqueId(), 5, HtRacePads.plugin);
    }
}
