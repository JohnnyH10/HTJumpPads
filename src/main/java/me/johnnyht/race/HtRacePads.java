package me.johnnyht.race;

import me.johnnyht.race.Commands.PadGiveCommand;
import me.johnnyht.race.bstats.Metrics;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class HtRacePads extends JavaPlugin implements Listener {

    public static List<UUID> playersNoMoreJump = new ArrayList<>();
    public static HtRacePads plugin;

    @Override
    public void onEnable() {
        getLogger().info("JumpPad Plugin Enabled!");
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("givepads").setExecutor(new PadGiveCommand());

        plugin = this;

        int pluginId = 25452;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @EventHandler
    public void onPlayerJump(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (playersNoMoreJump.contains(player.getUniqueId())) return;
        if (event.getFrom().getY() < event.getTo().getY() && player.getLocation().subtract(0, 0.1, 0).getBlock().getType().isSolid()) {
            Block originBlock = event.getFrom().getBlock();
            for (int dx = 0; dx <= 1; dx++) {
                for (int dz = 0; dz <= 1; dz++) {
                    Block checkBlock = originBlock.getRelative(dx, 0, dz);
                    for (Entity entity : checkBlock.getWorld().getNearbyEntities(checkBlock.getLocation(), 1.0, 1.0, 1.0)) {
                        if (entity instanceof ItemFrame itemFrame) {
                            ItemStack item = itemFrame.getItem();
                            if(item.getType() != Material.AIR) {
                                ItemMeta meta = item.getItemMeta();
                                String name = meta.getDisplayName();
                                frameItemNameChecker(name, itemFrame.getLocation(), player, true, item);
                            }
                            return;
                        }
                    }
                }
            }
        } else {
            Block originBlock = event.getFrom().getBlock();
            for (int dx = 0; dx <= 1; dx++) {
                for (int dz = 0; dz <= 1; dz++) {
                    Block checkBlock = originBlock.getRelative(dx, 0, dz);
                    for (Entity entity : checkBlock.getWorld().getNearbyEntities(checkBlock.getLocation(), 1.0, 1.0, 1.0)) {
                        if (entity instanceof ItemFrame itemFrame) {
                            ItemStack item = itemFrame.getItem();
                            if (item.getType() != Material.AIR) {
                                ItemMeta meta = item.getItemMeta();
                                ItemStack copy = item.clone();
                                String name = meta.getDisplayName();
                                frameItemNameChecker(name, itemFrame.getLocation(), player, false, copy);
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void frameItemNameChecker(String nameOfItemFrame, Location itemFrameLocation, Player player, boolean isJump, ItemStack item) {
        List<String> parts = Arrays.asList(nameOfItemFrame.split("\\s+"));
        String name = parts.get(0);

        switch (name) {
            case "jump" -> {

                if (!isJump) {
                    int y;
                    try {
                        y = Integer.parseInt(parts.get(1));
                    } catch (NumberFormatException e) {
                        plugin.getLogger().severe("Failed to parse int Y value for jump pad named " + nameOfItemFrame + " at location " + itemFrameLocation + ". Input: " + parts.get(1));
                        return;
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 10, y));
                    return;
                }

                if(player.hasPotionEffect(PotionEffectType.JUMP_BOOST)) {
                    player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.05);
                }
                player.removePotionEffect(PotionEffectType.JUMP_BOOST);
                playerCoolDownPad(player.getUniqueId(), 5, plugin);
            }
            case "launch" -> {
                if (!isJump) return;
                playerCoolDownPad(player.getUniqueId(), 5, plugin);

                double x, y;

                try {
                    x = Double.parseDouble(parts.get(1));
                } catch (NumberFormatException e) {
                    plugin.getLogger().severe("Failed to parse double X value for launch pad named " + nameOfItemFrame + " at location " + itemFrameLocation + ". Input: " + parts.get(1));
                    return;
                }

                try {
                    y = Double.parseDouble(parts.get(2));
                } catch (NumberFormatException e) {
                    plugin.getLogger().severe("Failed to parse double Y value for launch pad named " + nameOfItemFrame + " at location " + itemFrameLocation + ". Input: " + parts.get(2));
                    return;
                }

                Location location = player.getEyeLocation();
                player.setVelocity(location.getDirection().setY(0).normalize().multiply(x).setY(y));
                player.sendMessage("§aHigh Jump! + " + x + " " + y);
                player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.05);
            }
            case "speed" -> {
                if (isJump) return;
                playerCoolDownPad(player.getUniqueId(), 5, plugin);
                int time, amplifier;

                try {
                    time = Integer.parseInt(parts.get(1));
                } catch (NumberFormatException e) {
                    plugin.getLogger().severe("Failed to parse int speed time value for jump pad named " + nameOfItemFrame + " at location " + itemFrameLocation + ". Input: " + parts.get(1));
                    return;
                }

                try {
                    amplifier = Integer.parseInt(parts.get(2));
                } catch (NumberFormatException e) {
                    plugin.getLogger().severe("Failed to parse int speed amplifier value for jump pad named " + nameOfItemFrame + " at location " + itemFrameLocation + ". Input: " + parts.get(2));
                    return;
                }

                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, time, amplifier));
                player.sendMessage("§eSpeed...");
            }
            case "equip" -> {
                if (isJump) return;
                playerCoolDownPad(player.getUniqueId(), 5, plugin);

                if (item != null && item.getType() != Material.AIR) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        String displayName = item.getType().name().toLowerCase().replace("_", " ");
                        meta.setDisplayName(displayName.substring(0, 1).toUpperCase() + displayName.substring(1));
                        item.setItemMeta(meta);
                    }

                    String slot = parts.get(1).toLowerCase();
                    player.sendMessage(name + slot);

                    switch (slot) {
                        case "head" -> player.getInventory().setHelmet(item);
                        case "chest" -> player.getInventory().setChestplate(item);
                        case "legs" -> player.getInventory().setLeggings(item);
                        case "boots" -> player.getInventory().setBoots(item);
                        case "hand" -> player.getInventory().addItem(item);
                        default -> player.sendMessage(ChatColor.RED + "Invalid equip slot: " + slot);
                    }
                }
            }
            case "horse" -> {
                if (isJump) return;
                playerCoolDownPad(player.getUniqueId(), 5, plugin);

                double speed, jump;

                try {
                    speed = Double.parseDouble(parts.get(1));
                } catch (NumberFormatException e) {
                    plugin.getLogger().severe("Failed to parse double horse speed value for jump pad named " + nameOfItemFrame + " at location " + itemFrameLocation + ". Input: " + parts.get(1));
                    return;
                }

                try {
                    jump = Double.parseDouble(parts.get(2));
                } catch (NumberFormatException e) {
                    plugin.getLogger().severe("Failed to parse double horse jump value for jump pad named " + nameOfItemFrame + " at location " + itemFrameLocation + ". Input: " + parts.get(2));
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

                horse.addPassenger(player);
                player.sendMessage(ChatColor.YELLOW + "Spawned a horse with speed: " + speed);
            }
            case "pig" -> {
                if (isJump) return;
                playerCoolDownPad(player.getUniqueId(), 5, plugin);

                double speed;

                try {
                    speed = Double.parseDouble(parts.get(1));
                } catch (NumberFormatException e) {
                    plugin.getLogger().severe("Failed to parse double pig speed value for jump pad named " + nameOfItemFrame + " at location " + itemFrameLocation + ". Input: " + parts.get(1));
                    return;
                }

                if (player.getVehicle() instanceof Pig) {
                    player.sendMessage(ChatColor.RED + "You are already riding a pig!");
                    return;
                }

                Pig pig = player.getWorld().spawn(player.getLocation(), Pig.class);
                pig.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(speed);
                pig.setSaddle(true);
                pig.addPassenger(player);

                player.getInventory().addItem(new ItemStack(Material.CARROT_ON_A_STICK));
                player.sendMessage(ChatColor.YELLOW + "Spawned a pig and gave you a carrot on a stick!");
            }
            case "strider" -> {
                if (isJump) return;
                playerCoolDownPad(player.getUniqueId(), 5, plugin);

                if (player.getVehicle() instanceof Strider) {
                    player.sendMessage(ChatColor.RED + "You are already riding a strider!");
                    return;
                }

                double speed;

                try {
                    speed = Double.parseDouble(parts.get(1));
                } catch (NumberFormatException e) {
                    plugin.getLogger().severe("Failed to parse strider double speed value for jump pad named " + nameOfItemFrame + " at location " + itemFrameLocation + ". Input: " + parts.get(1));
                    return;
                }

                Strider strider = player.getWorld().spawn(player.getLocation(), Strider.class);
                strider.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(speed);
                strider.setSaddle(true);
                strider.addPassenger(player);

                player.getInventory().addItem(new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK));
                player.sendMessage(ChatColor.YELLOW + "Spawned a strider and gave you a warped fungus on a stick!");
            }
            case "camel" -> {
                if (isJump) return;
                playerCoolDownPad(player.getUniqueId(), 5, plugin);

                double speed, jump;

                try {
                    speed = Double.parseDouble(parts.get(1));
                } catch (NumberFormatException e) {
                    plugin.getLogger().severe("Failed to parse double camel speed value for jump pad named " + nameOfItemFrame + " at location " + itemFrameLocation + ". Input: " + parts.get(1));
                    return;
                }

                try {
                    jump = Double.parseDouble(parts.get(2));
                } catch (NumberFormatException e) {
                    plugin.getLogger().severe("Failed to parse double camel jump value for jump pad named " + nameOfItemFrame + " at location " + itemFrameLocation + ". Input: " + parts.get(2));
                    return;
                }

                if (player.getVehicle() instanceof Camel) {
                    player.sendMessage(ChatColor.RED + "You are already riding a camel!");
                    return;
                }

                Camel camel = player.getWorld().spawn(player.getLocation(), Camel.class);
                camel.setTamed(true);
                camel.setAdult();
                camel.setCustomName(ChatColor.GOLD + player.getName() + "'s Camel");
                camel.setCustomNameVisible(true);
                camel.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                camel.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(speed);
                camel.getAttribute(Attribute.JUMP_STRENGTH).setBaseValue(jump);
                camel.addPassenger(player);

                player.sendMessage(ChatColor.YELLOW + "Spawned a camel!");
            }
            case "iceboat" -> {
                if (isJump) return;
                playerCoolDownPad(player.getUniqueId(), 5, plugin);

                if (player.getVehicle() instanceof Boat) {
                    player.sendMessage(ChatColor.RED + "You are already in a boat!");
                    return;
                }

                Location loc = player.getLocation().clone();
                loc.setY(loc.getY() + 1);

                Entity entity = player.getWorld().spawnEntity(loc, EntityType.ACACIA_BOAT);
                if (entity instanceof Boat boat) {
                    boat.setInvulnerable(true);
                    boat.setCustomName(ChatColor.AQUA + player.getName() + "'s Ice Boat");
                    boat.setCustomNameVisible(true);
                    boat.addPassenger(player);
                    player.sendMessage(ChatColor.AQUA + "Spawned an ice boat!");
                } else {
                    player.sendMessage(ChatColor.RED + "Failed to spawn boat.");
                }
            }
            case "killvehicle" -> {
                if (isJump) return;
                playerCoolDownPad(player.getUniqueId(), 5, plugin);

                Entity vehicle = player.getVehicle();
                if (vehicle != null) {
                    vehicle.remove();
                    player.sendMessage(ChatColor.RED + "Your vehicle has been removed!");
                } else {
                    player.sendMessage(ChatColor.YELLOW + "You're not in a vehicle.");
                }
            }
            case "givepotioneffect" -> {
                if (isJump) return;
                playerCoolDownPad(player.getUniqueId(), 5, plugin);

                if (parts.size() < 3) {
                    player.sendMessage(ChatColor.RED + "Usage: givepotioneffect <effect> <durationTicks|infinite> [amplifier]");
                    return;
                }

                String effectName = parts.get(1).toUpperCase();
                String durationInput = parts.get(2);
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
                if (parts.size() >= 4) {
                    try {
                        amplifier = Integer.parseInt(parts.get(3));
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Invalid amplifier: " + parts.get(3));
                        return;
                    }
                }

                PotionEffectType effectType = PotionEffectType.getByName(effectName);
                if (effectType == null) {
                    player.sendMessage(ChatColor.RED + "Invalid potion effect: " + effectName);
                    return;
                }

                player.addPotionEffect(new PotionEffect(effectType, duration, amplifier));
                player.sendMessage(ChatColor.GREEN + "Applied potion effect: " + effectType.getName() +
                        (duration == Integer.MAX_VALUE ? " infinitely" : " for " + duration + " ticks") +
                        " with amplifier " + amplifier + ".");
            }
            case "removepotioneffect" -> {
                if (isJump) return;
                playerCoolDownPad(player.getUniqueId(), 2, plugin);

                if (player.getActivePotionEffects().isEmpty()) {
                    player.sendMessage(ChatColor.YELLOW + "You don't have any active potion effects.");
                    return;
                }

                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }

                player.sendMessage(ChatColor.GREEN + "All active potion effects removed.");
            }
            case "clearinv" -> {
                if (isJump) return; // Only trigger on walk-over, not on jump
                playerCoolDownPad(player.getUniqueId(), 5, plugin);

                player.getInventory().clear();
                player.sendMessage(ChatColor.RED + "Your inventory has been cleared!");
            }
            default -> {

            }
        }
    }

    public static void playerCoolDownPad(UUID uuid, int timeTicks, HtRacePads plugin) {
        playersNoMoreJump.add(uuid);
        new BukkitRunnable() {
            @Override
            public void run() {
                playersNoMoreJump.remove(uuid);
            }
        }.runTaskLater(plugin, timeTicks);
    }
}