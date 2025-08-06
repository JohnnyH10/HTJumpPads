package me.johnnyht.race.Pads.PadTypes.Run.Player;

import me.johnnyht.race.CommandManager.commands.SendPlayersMessages;
import me.johnnyht.race.Pads.PadAction;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ClearInventoryPad implements PadAction {

    @Override
    public void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item) {
        if (isJump) return; // Only trigger on walk-over

        if (item == null || item.getType() == Material.AIR) return;

        String slotArg = args[1].toLowerCase();
        Inventory inv = player.getInventory();

        if (SendPlayersMessages.uuidSetMessages.contains(player.getUniqueId())) {
            player.sendMessage(ChatColor.YELLOW + "Equipping to " + slotArg);
        }

        switch (slotArg) {
            case "head":
                clearSlot(inv, 39); // Clear helmet slot
                break;
            case "chest":
                clearSlot(inv, 38); // Clear chestplate slot
                break;
            case "legs":
                clearSlot(inv, 37); // Clear leggings slot
                break;
            case "boots":
                clearSlot(inv, 36);
                break;
            case "hand":
                clearAllExceptArmor(inv);
                break;
            case null:
                clearSlot(inv, 38);
                clearSlot(inv, 37);
                clearSlot(inv, 36);
                clearAllExceptArmor(inv);
            default:
                player.sendMessage(ChatColor.RED + "Invalid equip slot: " + slotArg);
        }
    }

    private void clearSlot(Inventory inv, int slotIndex) {
        inv.setItem(slotIndex, null);
    }

    private void clearAllExceptArmor(Inventory inv) {
        for (int i = 0; i < inv.getSize(); i++) {
            if (i < 36) {
                inv.setItem(i, null);
            }
        }
    }
}
