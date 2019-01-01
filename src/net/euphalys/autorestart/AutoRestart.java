package net.euphalys.autorestart;

import net.euphalys.autorestart.commands.RestartCommand;
import net.euphalys.autorestart.task.RestartTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.command.CraftCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.Timer;

/**
 * @author Dinnerwolph
 */
public class AutoRestart extends JavaPlugin {

    private Date date = new Date(System.currentTimeMillis());
    private Timer timer = new Timer();
    public RestartTask restartTask;
    public static AutoRestart instance;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        date.setHours(23);
        date.setMinutes(30);
        date.setSeconds(0);
        timer.schedule(restartTask = new RestartTask(this), date);
        getCommand("restart").setExecutor(new RestartCommand());
        try {
            Field field = CraftServer.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            CraftCommandMap map = (CraftCommandMap) field.get(Bukkit.getServer());
            Field field1 = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field1.setAccessible(true);
            Map<String, Command> knownCommands = (Map) field1.get(map);
            knownCommands.remove("spigot:restart");
            knownCommands.put("restart", knownCommands.get("autorestart:restart"));
            field1.set(map, knownCommands);
            field.set(Bukkit.getServer(), map);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
