package be.ordina.jworks.helm.house;

import be.ordina.jworks.helm.house.stream.MessageChannels;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableBinding(MessageChannels.class)
public class HouseOfCommonsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HouseOfCommonsApplication.class, args);
    }

}
