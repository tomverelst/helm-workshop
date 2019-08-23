package be.ordina.jworks.helm.speaker.test;

import be.ordina.jworks.helm.speaker.SpeakerApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringBootTest(classes = SpeakerApplication.class)
@ActiveProfiles("test")
public @interface IntegrationTest {
}
