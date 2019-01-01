package net.euphalys.autorestart.task;

import net.euphalys.autorestart.AutoRestart;
import net.minecraft.server.v1_13_R2.ChatComponentText;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.MinecraftServer;
import net.minecraft.server.v1_13_R2.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.TimerTask;

/**
 * @author Dinnerwolph
 */
public class RestartTask extends TimerTask {
    private final String message;
    private final String minutes;
    private final String seconds;
    private final String restart;
    private final String script;

    public RestartTask(AutoRestart instance) {
        message = instance.getConfig().getString("message");
        minutes = instance.getConfig().getString("minutes");
        seconds = instance.getConfig().getString("seconds");
        restart = instance.getConfig().getString("restart");
        script = instance.getConfig().getString("script");
    }

    @Override
    public void run() {
        MinecraftServer.getServer().processQueue.add(new Runnable() {
            public void run() {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            sendTitle(message + 30 + minutes);
                            Thread.sleep(1200000);
                            sendTitle(message + 10 + minutes);
                            Thread.sleep(300000);
                            sendTitle(message + 5 + minutes);
                            Thread.sleep(270000);
                            sendTitle(message + 30 + seconds);
                            Thread.sleep(20000);
                            sendTitle(message + 10 + seconds);
                            Thread.sleep(10000);
                            sendTitle(restart);
                            Thread.sleep(1000);
                            restart();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();


            }
        });
    }

    public void restart() {
        try {
            ProcessBuilder builder = new ProcessBuilder(new String[]{"/bin/bash", "-c", script});
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String reponse;
            while ((reponse = reader.readLine()) != null)
                System.out.println("Got response whene starting server: " + reponse);
            Bukkit.shutdown();
        } catch (Exception e) {
            System.out.println("Error while starting server:");
            e.printStackTrace();
        }

    }

    private void sendTitle(String subtitle) {
        sendTitle("Restart", subtitle);
    }

    private void sendTitle(String title, String subtitle) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            EntityPlayer eplayer = ((CraftPlayer) player).getHandle();
            PacketPlayOutTitle packettitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, new ChatComponentText(title));
            PacketPlayOutTitle packetsubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, new ChatComponentText(subtitle));
            eplayer.playerConnection.sendPacket(packettitle);
            eplayer.playerConnection.sendPacket(packetsubtitle);
        }
    }
}
