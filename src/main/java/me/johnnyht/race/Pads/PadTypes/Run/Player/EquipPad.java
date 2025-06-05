package me.johnnyht.race.Pads.PadTypes.Run.Player;

import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EquipPad implements PadAction {
    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {
        if (isJump) return; // Equip pad only triggers on walk-over
        HtRacePads.playerCoolDownPad(player.getUniqueId(), 5, HtRacePads.plugin);

        if (item != null && item.getType() != Material.AIR) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                String displayName = item.getType().name().toLowerCase().replace("_", " ");
                meta.setDisplayName(displayName.substring(0, 1).toUpperCase() + displayName.substring(1));
                item.setItemMeta(meta);
            }

            String slot = args[1].toLowerCase();
            player.sendMessage("Equipping to " + slot); // More descriptive message

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
}
