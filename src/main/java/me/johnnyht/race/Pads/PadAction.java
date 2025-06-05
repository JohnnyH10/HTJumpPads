package me.johnnyht.race.Pads;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PadAction {
    void execute(String[] args, Location loc, Player player, boolean isJump, ItemStack item);

}
