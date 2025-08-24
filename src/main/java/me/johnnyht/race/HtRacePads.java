package me.johnnyht.race;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import me.johnnyht.race.CommandManager.commands.PadGiveCommand;
import me.johnnyht.race.CommandManager.RegisterCommands;
import me.johnnyht.race.Pads.PadAction;
import me.johnnyht.race.Pads.PadTypes.Jump.Player.JumpPad;
import me.johnnyht.race.Pads.PadTypes.Jump.Player.LaunchPad;
import me.johnnyht.race.Pads.PadTypes.Run.Player.*;
import me.johnnyht.race.Pads.PadTypes.Run.Vehicles.*;
import me.johnnyht.race.bstats.Metrics;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.logging.Logger;


public final class HtRacePads extends JavaPlugin implements Listener {

    //public static final NamespacedKey PAD_ITEM = new NamespacedKey(HtRacePads.getInstance(),"pad_item");

    public static HtRacePads plugin;
    private static Logger log;

    private static List<UUID> playersNoMoreJump;
    private Set<UUID> uuidHasNox;
    private Map<String, PadAction> padActions;


    @Override
    public void onEnable() {
        plugin = this;
        log = getLogger();

        playersNoMoreJump = new ArrayList<>();
        uuidHasNox = new HashSet<>();
        padActions = new HashMap<>();

        getLogger().info("JumpPad Plugin Enabled!");
        getCommand("givepads").setExecutor(new PadGiveCommand());
        new RegisterCommands(this).registerCommands();
        registerPads();

        getServer().getPluginManager().registerEvents(this, this);

        int pluginId = 25452;
        Metrics metrics = new Metrics(this, pluginId);

        //ToDo we need to add Nox implementation to the pads as a toggleable feature in the config
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
        padActions.put("potionremove", new RemovePotionEffectPad());
        padActions.put("clearinv", new ClearInventoryPad());
        padActions.put("swimlaunch", new SwimLaunchPad());

        //ToDo add Tp Pad
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (playersNoMoreJump.contains(player.getUniqueId())) return;

        boolean isJumping = false;
        
        Location location = event.getPlayer().getLocation();
        for (Entity entity : location.getWorld().getNearbyEntities(location, 1.9, 1.0, 1.9)) {
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

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();
        if (playersNoMoreJump.contains(player.getUniqueId())) return;

        boolean isJumping = true;

        Location location = event.getPlayer().getLocation();
        for (Entity entity : location.getWorld().getNearbyEntities(location, 1.9, 1.0, 1.9)) {
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

    public static HtRacePads getInstance() {
        return plugin;
    }

    public static Logger getLog() {
        return log;
    }

    public List<UUID> getPlayersOnCooldown() {
        return playersNoMoreJump;
    }

    public Set<UUID> getNoxesiumPlayers() {
        return uuidHasNox;
    }

    public Map<String, PadAction> getPadActions() {
        return padActions;
    }


    public void executePadAction(String nameOfItemFrame, Location itemFrameLocation, Player player, boolean isJump, ItemStack item) {
        List<String> parts = Arrays.asList(nameOfItemFrame.split("\\s+"));
        String padType = parts.getFirst().toLowerCase(); // Ensure the key is lowercase

        PadAction action = padActions.get(padType);
        if (action != null) {

            String[] args = parts.subList(0, parts.size()).toArray(new String[0]);
            action.execute(args, itemFrameLocation, player, isJump, item);
        } else {
            // Handle unknown pad types if necessary
            getLog().warning("Unknown pad type: " + padType + " at location " + itemFrameLocation);
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