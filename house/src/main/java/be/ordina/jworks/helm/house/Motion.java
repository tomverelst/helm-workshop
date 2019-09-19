package be.ordina.jworks.helm.house;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Motion {

    private static final Logger LOGGER = LoggerFactory.getLogger(Motion.class);

    private final String question;

    private final Map<Member, Vote> votes = new ConcurrentHashMap<>();

    public Motion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public Map<Member, Vote> getVotes() {
        return votes;
    }

    long getAyes(){
        return votes.values().stream().filter(v -> v.equals(Vote.AYE)).count();
    }

    long getNoes(){
        return votes.values().stream().filter(v -> v.equals(Vote.NO)).count();
    }

    Motion vote(Member member, Vote vote){
        LOGGER.info("Member {} voted {}", member.getName(), vote);
        this.votes.put(member, vote);
        return this;
    }
}
