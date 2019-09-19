package be.ordina.jworks.helm.member.speech;

import java.util.Objects;

public class Speech {

    private String text;
    private SpeechType type;

    public Speech(String text) {
        this(text, SpeechType.UNKNOWN);
    }

    public Speech(String text, SpeechType type) {
        this.text = Objects.requireNonNull(text).trim();
        this.type = type != null ? type : SpeechType.UNKNOWN;
    }

    public Speech withType(SpeechType type){
        return new Speech(this.text, type);
    }

    public String getText() {
        return text;
    }

    public SpeechType getType() {
        return type;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Speech{");
        sb.append("text='").append(text).append('\'');
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Speech speech = (Speech) o;
        return text.equals(speech.text) &&
                type == speech.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, type);
    }
}
