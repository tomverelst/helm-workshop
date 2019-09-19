package be.ordina.jworks.helm.member;

public class MemberSeatedEvent {

    private String memberId;
    private String name;

    public MemberSeatedEvent(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }
}
