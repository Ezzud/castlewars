package fr.ezzud.castlewar.methods.countdowns;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SBCountdownTimer implements Runnable {

    private JavaPlugin plugin;

    public static Integer assignedTaskId;

    private Runnable afterTimer;

    public SBCountdownTimer(JavaPlugin plugin, Runnable afterTimer) {
        this.plugin = plugin;

        this.afterTimer = afterTimer;
    }


    @Override
    public void run() {
    	if(assignedTaskId == null) return;

            afterTimer.run();
            return;
    }

    
    
    
    public static void cancelTimer() {
    	Bukkit.getScheduler().cancelTask(assignedTaskId);
    	assignedTaskId = null;
    }
    
    public void scheduleTimer() {
        assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 20L);
    }

}