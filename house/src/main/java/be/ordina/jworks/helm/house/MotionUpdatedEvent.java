package be.ordina.jworks.helm.house;

import be.ordina.jworks.helm.house.http.Event;

import java.util.Map;

public class MotionUpdatedEvent implements Event {

    private final Motion motion;

    public MotionUpdatedEvent(Motion motion) {
        this.motion = motion;
    }

    @Override
    public String getType() {
        return "motion-updated";
    }

    @Override
    public Object getData() {
        return Map.of(
                "question", motion.getQuestion(),
                "ayes", motion.getAyes(),
                "noes", motion.getNoes());
    }
}
