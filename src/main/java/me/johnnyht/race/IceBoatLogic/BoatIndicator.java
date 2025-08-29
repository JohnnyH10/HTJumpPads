package me.johnnyht.race.IceBoatLogic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BoatIndicator implements Listener {
    private static final Map<UUID, ArmorStand> armorStand = new HashMap<>();
    //This is for the config later
    private boolean spawnIndicator = true;



    public static void removeArmorStand() {
        if(armorStand !=null) {
        for (ArmorStand stand : armorStand.values()) {
            stand.remove();
        }
    }
}


    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (!spawnIndicator) return;

        if (event.getEntered() instanceof Player && event.getVehicle() instanceof Boat) {

            Player player = (Player) event.getEntered();

            ArmorStand stand = armorStand.get(player.getUniqueId());
            if(stand == null) {


                ArmorStand armorStand1 = player.getWorld().spawn(player.getLocation(), ArmorStand.class);

                // Create an invisible armor stand
                armorStand1.setVisible(false);
                armorStand1.setSmall(true);

                // Create an ItemStack that represents the player's head
                ItemStack headItem = getHead(player);

                // Set the armor stand's helmet to the player's head ItemStack
                armorStand1.getEquipment().setHelmet(headItem);

                // Teleport the armor stand to the player's location
                armorStand1.teleport(player.getLocation());

                armorStand.put(player.getUniqueId(), armorStand1);
            }
        }
    }


    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        if (!spawnIndicator) return;
        if (event.getExited() instanceof Player player && event.getVehicle() instanceof Boat) {
            ArmorStand stand = armorStand.get(player.getUniqueId());
            if (stand != null) {
                stand.remove();
                armorStand.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        if (!spawnIndicator) return;
        Player player = event.getPlayer();
        if (event.getPlayer().getVehicle() instanceof Boat) {
            ArmorStand stand = armorStand.get(player.getUniqueId());
            if (stand != null) {
                stand.remove();
                armorStand.remove(player.getUniqueId());
            }
        }

    }

    public static ItemStack getHead(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(player.getName());
        skull.setOwner(player.getName());
        item.setItemMeta(skull);
        return item;
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!spawnIndicator) return;
        Player player = event.getPlayer();
        Location newLocation = event.getPlayer().getLocation();
        ArmorStand stand = armorStand.get(player.getUniqueId());
        if (stand != null){
            stand.teleport(newLocation);
            stand.teleport(newLocation.setDirection(player.getLocation().getDirection()));
        }
    }


}
