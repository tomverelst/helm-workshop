package be.ordina.jworks.helm.house;

import org.springframework.util.Assert;

public class HouseScenario {

    private HouseOfCommons house;

    public HouseScenario(HouseOfCommons house){
        Assert.notNull(house, "house cannot be null");
        this.house = house;
    }

    public HouseScenario seatMember(){
        this.house.seat(new Member("memberId", "memberName")).block();
        return this;
    }

    public HouseScenario startMotion(){
        this.house.startMotion("Question?").block();
        return this;
    }

    public HouseScenario voteAye(){
        this.house.vote("memberId", Vote.AYE).block();
        return this;
    }

    public HouseScenario voteNo(){
        this.house.vote("memberId", Vote.NO).block();
        return this;
    }

    public HouseScenario order() {
        this.house.order().block();
        return this;
    }
}
