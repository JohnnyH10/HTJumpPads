package me.JohnnyHT.runningOnRandomJumpPads;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class RunningOnRandomJumpPads extends JavaPlugin implements Listener {

    public static List<UUID> playersNoMoreJump = new ArrayList<>();
    public static RunningOnRandomJumpPads plugin;

    @Override
    public void onEnable() {
        getLogger().info("JumpPad Plugin Enabled!");
        getServer().getPluginManager().registerEvents(this, this);

        plugin = this;
    }

    @EventHandler
    public void onPlayerJump(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (playersNoMoreJump.contains(player.getUniqueId())) return;
        if (!player.isOnGround() && event.getFrom().getY() < event.getTo().getY()) {
            Block originBlock = event.getFrom().getBlock();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    Block checkBlock = originBlock.getRelative(dx, 0, dz);
                    for (Entity entity : checkBlock.getWorld().getNearbyEntities(checkBlock.getLocation(), 1.0, 1.0, 1.0)) {
                        if (entity instanceof ItemFrame itemFrame) {
                            ItemStack item = itemFrame.getItem();
                            if(item != null) {
                                ItemMeta meta = item.getItemMeta();
                                String name = meta.getDisplayName();
                                frameItemNameChecker(name, player);
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void frameItemNameChecker(String nameOfItemFrame, Player player) {
        List<String> parts = Arrays.asList(nameOfItemFrame.split("[ ]+"));
        String name = parts.get(0);
        playerCoolDownPad(player.getUniqueId(), plugin);
        switch (name) {
            case "jump" -> {
                Double x = Double.parseDouble(parts.get(1));
                Double y = Double.parseDouble(parts.get(2));
                Location location = player.getEyeLocation();
                player.setVelocity(location.getDirection().setY(0).normalize().multiply(x).setY(y));
                player.sendMessage("§aHigh Jump! + " + parts.get(1) +" "+ parts.get(2));
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

    public static void playerCoolDownPad(UUID uuid, RunningOnRandomJumpPads plugin){
        playersNoMoreJump.add(uuid);
        new BukkitRunnable() {
            @Override
            public void run() {
                playersNoMoreJump.remove(uuid);
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
}