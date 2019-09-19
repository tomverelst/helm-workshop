package be.ordina.jworks.helm.house;

import be.ordina.jworks.helm.house.http.Event;

import java.util.Map;

public class MotionStartedEvent implements Event {

    private Motion motion;

    public MotionStartedEvent(Motion motion) {
        this.motion = motion;
    }

    @Override
    public String getType() {
        return "motion-started";
    }

    @Override
    public Object getData() {
        return Map.of("question", motion.getQuestion());
    }
}
