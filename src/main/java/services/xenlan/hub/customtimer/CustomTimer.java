package services.xenlan.hub.customtimer;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import services.xenlan.hub.xHub;

@Getter
@Setter
public class CustomTimer{

    private String name;

    private String scoreboard;

    public long startMillis;

    public long endMillis;

    public long getRemaining;

    public BukkitTask task;

    public CustomTimer(String name, String scoreboard, long startMillis, long endMillis) {
        setName(name);
        setScoreboard(scoreboard);
        setStartMillis(startMillis);
        setEndMillis(endMillis);

        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (endMillis < System.currentTimeMillis()) {
                    xHub.getInstance().getCustomTimerManager().deleteTimer(xHub.getInstance().getCustomTimerManager().getCustomTimer(name));
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(xHub.getInstance(), 0, 20);
    }

    public long getRemaining(){
        return endMillis - System.currentTimeMillis();
    }

    public void cancel() {
        xHub.getInstance().getCustomTimerManager().deleteTimer(this);
    }

    public String getName() {
        return this.name;
    }

    public String getScoreboard() {
        return this.scoreboard;
    }

    public void setName( String name) {
        this.name = name;
    }

    public void setScoreboard( String scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setStartMillis( long startMillis) {
        this.startMillis = startMillis;
    }

    public void setEndMillis( long endMillis) {
        this.endMillis = endMillis;
    }

}
