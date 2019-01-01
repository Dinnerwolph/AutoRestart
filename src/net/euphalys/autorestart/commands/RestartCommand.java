package net.euphalys.autorestart.commands;

import net.euphalys.autorestart.AutoRestart;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author Dinnerwolph
 */
public class RestartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        AutoRestart.instance.restartTask.restart();
        return true;
    }
}
