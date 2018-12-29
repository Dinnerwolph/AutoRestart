package net.euphalys.autorestart;

import net.euphalys.autorestart.task.RestartTask;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.util.Timer;

/**
 * @author Dinnerwolph
 */
public class AutoRestart extends JavaPlugin {

    private Date date = new Date(System.currentTimeMillis());
    private Timer timer = new Timer();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        date.setHours(23);
        date.setMinutes(30);
        date.setSeconds(0);
        timer.schedule(new RestartTask(this), date);
    }
}
