package be.ordina.jworks.helm.speaker.speech;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class SpeechTypeDetector {

    private static final String ORDER = "order";

    private static final ImmutableSet<String> ORDER_LIKE_WORDS =
            ImmutableSet.of("other", "louder", "older", "old", "the", "orders");

    private static final ImmutableSet<String> INTERROGATIVE_WORDS =
            ImmutableSet.of("why", "how", "which", "what", "who", "whose", "whom", "where",
                    "whither", "whence", "whether", "whatsoever");

    private static final ImmutableSet<String> INTERROGATIVE_PARTICLE =
            ImmutableSet.of("are", "do", "does", "can", "aren't", "don't", "can't", "doesn't");

    private static final Splitter WORD_SPLITTER = Splitter
            .on(Pattern.compile("[\\s.:?,!;'\"@#$&()\\[\\]=]+('(s|d|t|ve|m))?"))
            .omitEmptyStrings()
            .trimResults();

    private static final double TRESHOLD = 0.8d;

    public Mono<SpeechType> detect(final Speech speech){
        return Mono.just(speech)
                .map(Speech::getText)
                .map(txt -> {

                    if(soundsLikeOrder(txt)){
                        return SpeechType.ORDER;
                    } else if(soundsLikeQuestion(txt)){
                        return SpeechType.QUESTION;
                    } else {
                        return SpeechType.UNKNOWN;
                    }
                });
    }

    private boolean soundsLikeOrder(String text){
        final var words = getWords(text);

        final var score = words.stream()
               .mapToDouble(this::wordScore)
               .sum();

        return score >= TRESHOLD * words.size();
    }

    private boolean soundsLikeQuestion(String text){
        final var words = getWords(text);
        final var firstWord = words.get(0);

        if(INTERROGATIVE_PARTICLE.contains(firstWord)){
            return true;
        }
        if(INTERROGATIVE_WORDS.contains(firstWord)){
            return true;
        }

        return false;
    }

    private double wordScore(String w) {
        if(w.equals(ORDER)){
            return 1d;

        } else if (ORDER_LIKE_WORDS.contains(w)){
            return TRESHOLD;

        } else {
            return 0d;
        }
    }

    private List<String> getWords(String text) {
        return StreamSupport.stream(
                   WORD_SPLITTER.split(text).spliterator(), false)
                   .map(String::toLowerCase)
                   .collect(Collectors.toList());
    }

}
