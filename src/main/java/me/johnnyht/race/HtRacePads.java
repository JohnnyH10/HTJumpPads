package me.johnnyht.race;

import com.noxcrew.noxesium.paper.api.event.NoxesiumPlayerRegisteredEvent;
import me.johnnyht.race.CommandManager.commands.PadGiveCommand;
import me.johnnyht.race.CommandManager.RegisterCommands;
import me.johnnyht.race.Pads.PadAction;
import me.johnnyht.race.Pads.PadTypes.Jump.Player.JumpPad;
import me.johnnyht.race.Pads.PadTypes.Jump.Player.LaunchPad;
import me.johnnyht.race.Pads.PadTypes.Run.Player.*;
import me.johnnyht.race.Pads.PadTypes.Run.Vehicles.*;
import me.johnnyht.race.Pads.noxesium.QuibsDefinitions;
import me.johnnyht.race.bstats.Metrics;
import me.superneon4ik.noxesiumutils.NoxesiumUtils;
import me.superneon4ik.noxesiumutils.config.NoxesiumUtilsConfigBuilder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;

public final class HtRacePads extends JavaPlugin implements Listener {

    public static List<UUID> playersNoMoreJump = new ArrayList<>();
    public static Set<UUID> uuidHasNox = new HashSet<>();
    public static HtRacePads plugin;
    public static final Map<String, PadAction> padActions = new HashMap<>();

    public NoxesiumUtils noxesiumUtils;



    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("JumpPad Plugin Enabled!");
        getCommand("givepads").setExecutor(new PadGiveCommand());
        new RegisterCommands(this).registerCommands();
        registerPads();

        /*
        var jumpEffect = new QibEffect.GivePotionEffect(
                "minecraft",
                "jump_boost",
                10,
                5,
                true,
                true,
                true
        );

        var jumpDef = new QibDefinition(
                jumpEffect,
                null,
                jumpEffect,
                null,
                false
        );
        */

        //This is because I am shading the jar
        var config = new NoxesiumUtilsConfigBuilder().build();
        new QuibsDefinitions(config);
        config.setCheckForUpdates(false);
        config.getDefaults().disableSpinAttackCollisions = true;

        //config.getQibDefinitions().put("jump", jumpDef);

        this.noxesiumUtils = new NoxesiumUtils(plugin, config, plugin.getLogger());
        this.noxesiumUtils.register();

        getServer().getPluginManager().registerEvents(this, this);

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
    public void noxPlayerRegister(NoxesiumPlayerRegisteredEvent e){
        uuidHasNox.add(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerJump(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (playersNoMoreJump.contains(player.getUniqueId())) return;

        boolean isJumping = event.getFrom().getY() < event.getTo().getY() && player.getLocation().subtract(0, 0.1, 0).getBlock().getType().isSolid();
        
        Block originBlock = event.getFrom().getBlock();
        for (Entity entity : originBlock.getWorld().getNearbyEntities(originBlock.getLocation(), 3.0, 1.0, 3.0)) {
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