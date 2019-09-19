package be.ordina.jworks.helm.house;

import org.springframework.util.Assert;

import java.util.Objects;

public class Member {

    private final String id;
    private final String name;

    public Member(String id, String name) {
        Assert.hasText(id, "id cannot be empty");
        Assert.hasText(name, "name cannot be empty");
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
