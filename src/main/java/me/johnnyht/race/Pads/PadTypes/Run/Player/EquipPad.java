package me.johnnyht.race.Pads.PadTypes.Run.Player;

import me.johnnyht.race.CommandManager.commands.SendPlayersMessages;
import me.johnnyht.race.HtRacePads;
import me.johnnyht.race.Pads.PadAction;
import me.johnnyht.race.Sound.PadSound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class EquipPad implements PadAction {
    private final NamespacedKey pluginItemKey = new NamespacedKey(HtRacePads.getInstance(), "plugin_item");

    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {
        if (isJump) return; // Only trigger on walk-over
        if (item == null || item.getType() == Material.AIR) return;

        // A check for the arguments to prevent ArrayIndexOutOfBoundsException
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Invalid command usage.");
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String displayName = item.getType().name().toLowerCase().replace("_", " ");
            meta.setDisplayName(displayName.substring(0, 1).toUpperCase() + displayName.substring(1));
            meta.getPersistentDataContainer().set(pluginItemKey, PersistentDataType.BYTE, (byte) 1);
            item.setItemMeta(meta);
        }

        if (checkIfPlayerHasItem(item, player)) {
            if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.YELLOW + "You already have this item!");
            }
            return;
        }

        if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.YELLOW + "Equipping to " + args[1].toLowerCase());
        }

        String slot = args[1].toLowerCase();
        switch (slot) {
            case "head" -> player.getInventory().setHelmet(item);
            case "chest" -> player.getInventory().setChestplate(item);
            case "legs" -> player.getInventory().setLeggings(item);
            case "boots" -> player.getInventory().setBoots(item);
            case "hand" -> giveItem(player, item);
            default -> player.sendMessage(ChatColor.RED + "Invalid equip slot: " + slot);
        }
        PadSound.playSoundAtPlayer(player, "entity.item.pickup",1.0f,1.0f);
    }

    private void giveItem(Player player, ItemStack item) {
        if (HtRacePads.getInstance().getServer().getPluginManager().isPluginEnabled("VelocityTridents")
            && item.getType() == Material.TRIDENT && item.getItemMeta().hasEnchant(Enchantment.RIPTIDE)) {

            int level = item.getItemMeta().getEnchantLevel(Enchantment.RIPTIDE);
            ItemMeta im = item.getItemMeta();
            im.removeEnchant(Enchantment.RIPTIDE);
            im.setCustomModelData(level); //TODO: replace with components in VelcityTridents refactor
            item.setItemMeta(im);
        }

        player.give(item);
    }

    private boolean checkIfPlayerHasItem(ItemStack itemToCheck, Player player) {
        Inventory inv = player.getInventory();
        return inv.contains(itemToCheck.getType()) ||
                player.getInventory().getItemInOffHand().getType() == itemToCheck.getType() ||
                player.getInventory().getHelmet().getType() == itemToCheck.getType() ||
                player.getInventory().getChestplate().getType() == itemToCheck.getType() ||
                player.getInventory().getLeggings().getType() == itemToCheck.getType() ||
                player.getInventory().getBoots().getType() == itemToCheck.getType() ||
                player.getItemOnCursor().getType() == itemToCheck.getType();
    }
}