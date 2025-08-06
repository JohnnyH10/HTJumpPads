package me.johnnyht.race.CommandManager;

import me.johnnyht.race.CommandManager.commands.PadGiveCommand;
import me.johnnyht.race.CommandManager.commands.RemoveSelfFromMessagesList;
import me.johnnyht.race.CommandManager.commands.SendPlayersMessages;
import org.bukkit.plugin.java.JavaPlugin;

public class RegisterCommands {

    private final JavaPlugin plugin;

    public RegisterCommands(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerCommands() {
        plugin.getCommand("addmessage").setExecutor(new SendPlayersMessages());
        plugin.getCommand("removemessage").setExecutor(new RemoveSelfFromMessagesList());
        plugin.getCommand("givepads").setExecutor(new PadGiveCommand());
    }

}
