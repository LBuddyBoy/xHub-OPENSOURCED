package services.xenlan.hub.customtimer;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomTimerManager {
    private List<CustomTimer> customtimer;

    public CustomTimerManager() {
        this.customtimer = new ArrayList<>();
    }

    public void createTimer(CustomTimer timer) {
        this.customtimer.add(timer);
    }

    public void deleteTimer(CustomTimer timer) {
        this.customtimer.remove(timer);
    }

    public CustomTimer getCustomTimer(String name) {
        return this.customtimer.stream().filter(timer -> timer.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<CustomTimer> getCustomtimer() {
        return this.customtimer;
    }
}
