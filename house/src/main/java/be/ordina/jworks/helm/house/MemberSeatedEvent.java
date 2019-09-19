package be.ordina.jworks.helm.house;

import be.ordina.jworks.helm.house.http.Event;

import java.util.Objects;

public class MemberSeatedEvent implements Event {

    private final Member member;

    public MemberSeatedEvent(Member member) {
        this.member = Objects.requireNonNull(member);
    }

    public Member getMember() {
        return member;
    }

    @Override
    public String getType() {
        return "member-seated";
    }

    @Override
    public Object getData() {
        return member;
    }
}
