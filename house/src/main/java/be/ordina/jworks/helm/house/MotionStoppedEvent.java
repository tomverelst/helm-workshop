package be.ordina.jworks.helm.house;

import be.ordina.jworks.helm.house.http.Event;

import java.util.Map;

public class MotionStoppedEvent implements Event {

    @Override
    public String getType() {
        return "motion-stopped";
    }

    @Override
    public Object getData() {
        return Map.of();
    }
}
