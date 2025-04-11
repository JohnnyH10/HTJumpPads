package me.JohnnyHT.runningOnRandomJumpPads;

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
                                frameItemNameChecker(name, player, false, item);
                            }
                            return;
                        }
                    }
                }
            }
        } else {
            Block originBlock = event.getFrom().getBlock();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    Block checkBlock = originBlock.getRelative(dx, 0, dz);
                    for (Entity entity : checkBlock.getWorld().getNearbyEntities(checkBlock.getLocation(), 1.0, 1.0, 1.0)) {
                        if (entity instanceof ItemFrame itemFrame) {
                            ItemStack item = itemFrame.getItem();
                            if(item != null) {
                                ItemMeta meta = item.getItemMeta();
                                ItemStack copy = item.clone();
                                String name = meta.getDisplayName();
                                frameItemNameChecker(name, player, true, copy);
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void frameItemNameChecker(String nameOfItemFrame, Player player, Boolean noJump, ItemStack item) {
        List<String> parts = Arrays.asList(nameOfItemFrame.split("[ ]+"));
        String name = parts.get(0);
        playerCoolDownPad(player.getUniqueId(), 5, plugin);
        switch (name) {
            case "jump" -> {
                if (noJump) return;
                Double x = Double.parseDouble(parts.get(1));
                Double y = Double.parseDouble(parts.get(2));
                Location location = player.getEyeLocation();
                player.setVelocity(location.getDirection().setY(0).normalize().multiply(x).setY(y));
                player.sendMessage("§aHigh Jump! + " + parts.get(1) +" "+ parts.get(2));
                player.getWorld().spawnParticle(Particle.EXPLOSION, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.05);
            }
            case "speed" -> {
                if (!noJump) return;
                Integer time = Integer.parseInt(parts.get(1));
                Integer amplifier = Integer.parseInt(parts.get(2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, time, amplifier));
                player.sendMessage("§eSpeed...");
            }
            case "equipt" -> {
                if (!noJump) return;

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
                if (!noJump) return;

                double speed;
                try {
                    speed = Double.parseDouble(parts.get(1));
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid speed value.");
                    return;
                }

                double jump;
                try {
                    jump = Double.parseDouble(parts.get(2));
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid jump value.");
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
                if (!noJump) return;

                double speed;
                try {
                    speed = Double.parseDouble(parts.get(1));
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid speed value.");
                    return;
                }

                if (player.getVehicle() instanceof Pig) {
                    player.sendMessage(ChatColor.RED + "You are already riding a pig!");
                    return;
                }

                Pig pig = (Pig) player.getWorld().spawn(player.getLocation(), Pig.class);
                pig.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(speed);
                pig.setSaddle(true);

                pig.addPassenger(player);

                player.getInventory().addItem(new ItemStack(Material.CARROT_ON_A_STICK));
                player.sendMessage(ChatColor.YELLOW + "Spawned a pig and gave you a carrot on a stick!");
            }
            case "strider" -> {
                if (!noJump) return;

                if (player.getVehicle() instanceof Strider) {
                    player.sendMessage(ChatColor.RED + "You are already riding a strider!");
                    return;
                }

                double speed;
                try {
                    speed = Double.parseDouble(parts.get(1));
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid speed value.");
                    return;
                }

                Strider strider = player.getWorld().spawn(player.getLocation(), Strider.class);
                strider.setSaddle(true);
                strider.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(speed);
                strider.addPassenger(player);



                player.getInventory().addItem(new ItemStack(Material.WARPED_FUNGUS_ON_A_STICK));
                player.sendMessage(ChatColor.YELLOW + "Spawned a strider and gave you a warped fungus on a stick!");
            }
            case "camel" -> {
                if (!noJump) return;

                double speed;
                try {
                    speed = Double.parseDouble(parts.get(1));
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid speed value.");
                    return;
                }

                double jump;
                try {
                    jump = Double.parseDouble(parts.get(2));
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid jump value.");
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
                if (!noJump) return;

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
            default -> {

            }
        }
    }

    public static void playerCoolDownPad(UUID uuid,int timeTicks, RunningOnRandomJumpPads plugin){
        playersNoMoreJump.add(uuid);
        new BukkitRunnable() {
            @Override
            public void run() {
                playersNoMoreJump.remove(uuid);
            }
        }.runTaskLater(plugin, timeTicks);
    }
}