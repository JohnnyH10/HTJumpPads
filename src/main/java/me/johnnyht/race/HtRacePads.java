package me.johnnyht.race;

import me.johnnyht.race.Commands.PadGiveCommand;
import me.johnnyht.race.Pads.PadAction;
import me.johnnyht.race.Pads.PadTypes.Jump.Player.JumpPad;
import me.johnnyht.race.Pads.PadTypes.Jump.Player.LaunchPad;
import me.johnnyht.race.Pads.PadTypes.Run.Player.*;
import me.johnnyht.race.Pads.PadTypes.Run.Vehicles.*;
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
    public static final Map<String, PadAction> padActions = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("JumpPad Plugin Enabled!");
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("givepads").setExecutor(new PadGiveCommand());
        registerPads();

        plugin = this;

        int pluginId = 25452;
        Metrics metrics = new Metrics(this, pluginId);
    }

    private void registerPads() {
        padActions.put("jump", new JumpPad());
        padActions.put("launch", new LaunchPad());
        padActions.put("speed", new SpeedPad());
        padActions.put("equip", new EquipPad());
        padActions.put("horse", new HorsePad());
        padActions.put("pig", new PigPad());
        padActions.put("strider", new StriderPad());
        padActions.put("camel", new CamelPad());
        padActions.put("iceboat", new IceBoatPad());
        padActions.put("killvehicle", new KillVehiclePad());
        padActions.put("givepotioneffect", new GivePotionEffectPad());
        padActions.put("removepotioneffect", new RemovePotionEffectPad());
        padActions.put("clearinv", new ClearInventoryPad());
        padActions.put("swimlaunch", new SwimLaunchPad());
    }


    @EventHandler
    public void onPlayerJump(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (playersNoMoreJump.contains(player.getUniqueId())) return;

        boolean isJumping = event.getFrom().getY() < event.getTo().getY() && player.getLocation().subtract(0, 0.1, 0).getBlock().getType().isSolid();

        Block originBlock = event.getFrom().getBlock();
        for (int dx = 0; dx <= 1; dx++) {
            for (int dz = 0; dz <= 1; dz++) {
                Block checkBlock = originBlock.getRelative(dx, 0, dz);
                for (Entity entity : checkBlock.getWorld().getNearbyEntities(checkBlock.getLocation(), 1.0, 1.0, 1.0)) {
                    if (entity instanceof ItemFrame itemFrame) {
                        ItemStack item = itemFrame.getItem();
                        if (item.getType() != Material.AIR) {
                            ItemMeta meta = item.getItemMeta();
                            String name = meta.getDisplayName();
                            executePadAction(name, itemFrame.getLocation(), player, isJumping, item);
                        }
                        return;
                    }
                }
            }
        }
    }


    public static void executePadAction(String nameOfItemFrame, Location itemFrameLocation, Player player, boolean isJump, ItemStack item) {
        List<String> parts = Arrays.asList(nameOfItemFrame.split("\\s+"));
        String padType = parts.get(0).toLowerCase(); // Ensure the key is lowercase

        PadAction action = padActions.get(padType);
        if (action != null) {

            String[] args = parts.subList(0, parts.size()).toArray(new String[0]);
            action.execute(args, itemFrameLocation, player, isJump, item);
        } else {
            // Handle unknown pad types if necessary
            plugin.getLogger().warning("Unknown pad type: " + padType + " at location " + itemFrameLocation);
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