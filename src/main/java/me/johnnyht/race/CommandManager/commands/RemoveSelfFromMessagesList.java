package me.johnnyht.race.CommandManager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public class RemoveSelfFromMessagesList implements CommandExecutor {

    public static Set<UUID> uuidSetMessages = SendPlayersMessages.uuidSetMessages;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.isOp()) {
            player.sendMessage("You need to be oped.");
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        if (uuidSetMessages.remove(playerUUID)) {
            player.sendMessage("You have been removed from getting messages.");
        } else {
            player.sendMessage("You were not on the message list.");
        }

        return true;
    }
}
