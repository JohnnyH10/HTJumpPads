package me.johnnyht.race.CommandManager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SendPlayersMessages implements CommandExecutor {

    public static Set<UUID> uuidSetMessages = new HashSet<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        try {
            boolean value = Boolean.parseBoolean(strings[0]);
            if (value) {
                uuidSetMessages.add(player.getUniqueId());
                player.sendMessage("You have been added to get pad messages from pad use.");
                return true;
            } else {
                UUID playerUUID = player.getUniqueId();
                if (uuidSetMessages.remove(playerUUID)) {
                    player.sendMessage("You have been removed from getting messages.");
                } else {
                    player.sendMessage("You were not on the message list.");
                }
            }
        } catch (IllegalArgumentException e){
            sender.sendMessage("Invalid argument. Please use 'true' or 'false'.");
        }
        return true;
    }
}
