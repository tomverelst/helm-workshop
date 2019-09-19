package be.ordina.jworks.helm.member.speech;

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

    private static final ImmutableSet<String> AYE_LIKE_WORDS =
            ImmutableSet.of("aye", "eye", "i", "hi");

    private static final ImmutableSet<String> NO_LIKE_WORDS =
            ImmutableSet.of("no", "now", "noes", "nose");

    private static final Splitter WORD_SPLITTER = Splitter
            .on(Pattern.compile("[\\s.:?,!;'\"@#$&()\\[\\]=]+('(s|d|t|ve|m))?"))
            .omitEmptyStrings()
            .trimResults();

    public Mono<SpeechType> detect(final Speech speech){
        return Mono.just(speech)
                .map(Speech::getText)
                .map(txt -> {

                    if(soundsLikeAye(txt)){
                        return SpeechType.AYE;
                    } else if(soundsLikeNo(txt)){
                        return SpeechType.NO;
                    } else {
                        return SpeechType.UNKNOWN;
                    }
                });
    }

    private boolean soundsLikeAye(String text){
        final var words = getWords(text);
        final var firstWord = words.get(0);

        if(AYE_LIKE_WORDS.contains(firstWord)){
            return true;
        }

        return false;
    }

    private boolean soundsLikeNo(String text){
        final var words = getWords(text);
        final var firstWord = words.get(0);

        if(NO_LIKE_WORDS.contains(firstWord)){
            return true;
        }

        return false;
    }

    private List<String> getWords(String text) {
        return StreamSupport.stream(
                   WORD_SPLITTER.split(text).spliterator(), false)
                   .map(String::toLowerCase)
                   .collect(Collectors.toList());
    }

}
