package be.ordina.jworks.helm.house.stream;

import be.ordina.jworks.helm.house.Vote;

public class MemberVotedStreamEvent {

    private String memberId;
    private Vote vote;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }
}
