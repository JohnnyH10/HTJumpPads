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

        if (!sender.isOp()){
            sender.sendMessage("You need to be oped.");
            return true;
        }

        uuidSetMessages.add(((Player) sender).getUniqueId());
        return true;
    }
}
