package fr.ezzud.castlewar.methods.countdowns;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;


public class EndCountdownTimer implements Runnable {

    private JavaPlugin plugin;

    public static Integer assignedTaskId;

    private int seconds;
    private int secondsLeft;
    private boolean reduced = false;

    private Consumer<EndCountdownTimer> everySecond;
    private Runnable beforeTimer;
    private Runnable afterTimer;

    public EndCountdownTimer(JavaPlugin plugin, int seconds,
                          Runnable beforeTimer, Runnable afterTimer,
                          Consumer<EndCountdownTimer> everySecond) {
        this.plugin = plugin;

        this.seconds = seconds;
        this.secondsLeft = seconds;

        this.beforeTimer = beforeTimer;
        this.afterTimer = afterTimer;
        this.everySecond = everySecond;
    }


    @Override
    public void run() {
    	if(assignedTaskId == null) return;
        if (secondsLeft < 1) {
            afterTimer.run();

            if (assignedTaskId != null) Bukkit.getScheduler().cancelTask(assignedTaskId);
            return;
        }

        if (secondsLeft == seconds) beforeTimer.run();

        everySecond.accept(this);

        secondsLeft--;
    }


    public int getTotalSeconds() {
        return seconds;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }
    
    public void reduceTimer(Integer i) {
    	if(reduced == false) {
        	secondsLeft = i;
        	reduced = true;    		
    	}

    }
    
    
    public static void cancelTimer() {
    	Bukkit.getScheduler().cancelTask(assignedTaskId);
    	assignedTaskId = null;
    }
    
    public void scheduleTimer() {
        assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 20L);
    }

}