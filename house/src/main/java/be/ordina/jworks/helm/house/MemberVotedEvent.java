package be.ordina.jworks.helm.house;

import be.ordina.jworks.helm.house.http.Event;

import java.util.Map;

public class MemberVotedEvent implements Event {

    private Member member;
    private Vote vote;

    public MemberVotedEvent(Member member, Vote vote) {
        this.member = member;
        this.vote = vote;
    }

    public Member getMember() {
        return member;
    }

    public Vote getVote() {
        return vote;
    }

    @Override
    public String getType() {
        return "member-voted";
    }

    @Override
    public Object getData() {
        return Map.of("member", member, "vote", vote);
    }
}
