package me.johnnyht.race.Pads.PadTypes.Run.Player;

import me.johnnyht.race.CommandManager.commands.SendPlayersMessages;
import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import me.johnnyht.race.Sound.PadSound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
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
        if (player.getOpenInventory().getTopInventory() instanceof CraftingInventory crafting) {
            crafting.clear();
        }
        if (slotArg == null) {

            clearAllInventory(player);
            PadSound.playSoundAtPlayer(player, "minecraft:item.bundle.remove_one",1.0f,1.0f);
            return;
        }

        PadSound.playSoundAtPlayer(player, "minecraft:item.bundle.remove_one",1.0f,1.0f);
    }

    private void clearSlot(Inventory inv, int slotIndex) {
        ItemStack item = inv.getItem(slotIndex);
        if (isPluginItem(item)) {
            inv.setItem(slotIndex, null);
        }
    }
    

    private void clearAllInventory(Player p) {
        if (p.getOpenInventory().getTopInventory() instanceof CraftingInventory crafting) {
            crafting.clear();
        }
        for (int i = 0; i < p.getInventory().getSize(); i++) {
            if (checkItem(p.getInventory().getItem(i))) {
                p.getInventory().setItem(i, null);

            }
        }
        if (checkItem(p.getInventory().getItemInOffHand())) { p.getInventory().setItemInOffHand(null); }
        if (checkItem(p.getInventory().getHelmet())) { p.getInventory().setHelmet(null); }
        if (checkItem(p.getInventory().getChestplate())) { p.getInventory().setChestplate(null); }
        if (checkItem(p.getInventory().getLeggings())) { p.getInventory().setLeggings(null); }
        if (checkItem(p.getInventory().getBoots())) { p.getInventory().setBoots(null); }
        if (checkItem(p.getItemOnCursor())) { p.setItemOnCursor(null); }
    }


    private boolean checkItem(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(pluginItemKey);
    }

    private boolean isPluginItem(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(pluginItemKey, PersistentDataType.BYTE);
    }

}
