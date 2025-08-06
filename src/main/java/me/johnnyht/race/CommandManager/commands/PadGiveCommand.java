package me.johnnyht.race.CommandManager.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PadGiveCommand implements CommandExecutor {

    private ItemStack createPad(String name, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Place in item frame to activate");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        // This is just to make sure the player is oped
        if (!sender.isOp()){
            sender.sendMessage("You need to be oped.");
            return true;
        }

        ItemStack shulkerBox = new ItemStack(Material.ORANGE_SHULKER_BOX);
        BlockStateMeta meta = (BlockStateMeta) shulkerBox.getItemMeta();
        ShulkerBox box = (ShulkerBox) meta.getBlockState();
        Inventory inv = box.getInventory();

        inv.addItem(createPad("jump 3", Material.SLIME_BALL));
        inv.addItem(createPad("launch 1.5 1", Material.FIREWORK_ROCKET));
        inv.addItem(createPad("speed 100 1", Material.SUGAR));
        inv.addItem(createPad("equip hand", Material.DIAMOND_SWORD));
        inv.addItem(createPad("horse 0.25 0.7", Material.SADDLE));
        inv.addItem(createPad("pig 0.3", Material.CARROT_ON_A_STICK));
        inv.addItem(createPad("strider 0.25", Material.WARPED_FUNGUS_ON_A_STICK));
        inv.addItem(createPad("camel 0.2 0.5", Material.HAY_BLOCK));
        inv.addItem(createPad("iceboat", Material.CHERRY_BOAT));
        inv.addItem(createPad("givepotioneffect jump_boost infinite 2", Material.POTION));
        inv.addItem(createPad("potionremove", Material.MILK_BUCKET));
        inv.addItem(createPad("killvehicle", Material.BARRIER));
        inv.addItem(createPad("clearinv boots", Material.STRUCTURE_VOID));
        inv.addItem(createPad("swimlaunch 2.0 0.0", Material.TROPICAL_FISH));

        box.update();
        meta.setBlockState(box);
        shulkerBox.setItemMeta(meta);

        player.getInventory().addItem(shulkerBox);
        player.sendMessage(ChatColor.GREEN + "You received a pad-filled Shulker Box!");
        return true;
    }
}
