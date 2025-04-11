package me.JohnnyHT.runningOnRandomJumpPads;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public final class RunningOnRandomJumpPads extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("JumpPad Plugin Enabled!");
        getServer().getPluginManager().registerEvents(this, this);

    }

    @EventHandler
    public void onPlayerJump(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!player.isOnGround() && event.getFrom().getY() < event.getTo().getY()) {
            Block originBlock = event.getFrom().getBlock();
            // Check a 3x3 area around the original jump location
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    Block checkBlock = originBlock.getRelative(dx, 0, dz);
                    for (Entity entity : checkBlock.getWorld().getNearbyEntities(checkBlock.getLocation(), 1.0, 1.0, 1.0)) {
                        if (entity instanceof ItemFrame itemFrame) {
                            ItemStack frameItem = itemFrame.getItem();
                            String name = itemFrame.getName();
                            frameItemNameChecker(name, player);
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void frameItemNameChecker(String nameOfItemFrame, Player player) {
        List<String> parts = Arrays.asList(nameOfItemFrame.split("[ .]+"));
        String name = parts.get(0);
        switch (name) {
            case "jump" -> {
                Double x = Double.parseDouble(parts.get(1));
                Double y = Double.parseDouble(parts.get(2));
                Location location = player.getEyeLocation();
                player.setVelocity(location.getDirection().setY(0).normalize().multiply(x).setY(y));
                player.sendMessage("§aHigh Jump!");
            }
            case "speed" -> {
                Integer time = Integer.parseInt(parts.get(1));
                Integer amplifier = Integer.parseInt(parts.get(2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, time, amplifier));
                player.sendMessage("§eSpeed...");
            }
            case "trident" -> {

            }
            default -> {

            }
        }
    }
}





