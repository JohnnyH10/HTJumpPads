package me.johnnyht.race.Pads.PadTypes.Run.Player;

import me.johnnyht.race.CommandManager.commands.SendPlayersMessages;
import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;



public class ClearInventoryPad implements PadAction {


    private final NamespacedKey pluginItemKey = new NamespacedKey(HtRacePads.getInstance(), "plugin_item");

    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {
        if (isJump) return;
        if (item == null || item.getType() == Material.AIR) return;

        Inventory inv = player.getInventory();
        String slotArg = args.length > 1 ? args[1].toLowerCase() : null;

        if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.YELLOW + "Clearing plugin items" + (slotArg != null ? " in slot: " + slotArg : " (all slots)"));
        }

        if (slotArg == null) {

            clearAllInventory(inv);
            return;
        }

        switch (slotArg) {
            case "head" -> clearSlot(inv, 39);
            case "chest" -> clearSlot(inv, 38);
            case "legs" -> clearSlot(inv, 37);
            case "boots" -> clearSlot(inv, 36);
            case "hand" -> clearAllInventory(inv);
            default -> player.sendMessage(ChatColor.RED + "Invalid equip slot: " + slotArg);
        }
    }

    private void clearSlot(Inventory inv, int slotIndex) {
        ItemStack item = inv.getItem(slotIndex);
        if (isPluginItem(item)) {
            inv.setItem(slotIndex, null);
        }
    }

    private void clearAllInventory(Inventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (isPluginItem(item)) {
                inv.clear(i);
            }
        }
    }

    private boolean isPluginItem(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(pluginItemKey, PersistentDataType.BYTE);
    }

}
