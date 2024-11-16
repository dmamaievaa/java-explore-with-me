package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.HitDto;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class MainServiceApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MainServiceApplication.class, args);
        StatsClient client = context.getBean(StatsClient.class);

        HitDto hitDto = HitDto.builder()
                .app("my-app")
                .uri("/example")
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();

        client.postStats(hitDto);

        List<String> uris = List.of("/example");
        ResponseEntity<Object> stats = client.getStats(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                uris,
                false
        );

        System.out.println("Retrieved stats: " + stats.getBody());
    }
}
