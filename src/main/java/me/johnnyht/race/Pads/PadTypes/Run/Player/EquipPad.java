package me.johnnyht.race.Pads.PadTypes.Run.Player;

import me.johnnyht.race.CommandManager.commands.SendPlayersMessages;
import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class EquipPad implements PadAction {
    private final NamespacedKey pluginItemKey = new NamespacedKey(HtRacePads.getInstance(), "plugin_item");

    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {
        if (isJump) return; // Only trigger on walk-over

        if (item == null || item.getType() == Material.AIR) return;

        // Tag the item as plugin-given
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String displayName = item.getType().name().toLowerCase().replace("_", " ");
            meta.setDisplayName(displayName.substring(0, 1).toUpperCase() + displayName.substring(1));
            meta.getPersistentDataContainer().set(pluginItemKey, PersistentDataType.BYTE, (byte) 1);
            item.setItemMeta(meta);
        }

        // Determine slot
        String slot = args[1].toLowerCase();
        if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.YELLOW + "Equipping to " + slot);
        }

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
