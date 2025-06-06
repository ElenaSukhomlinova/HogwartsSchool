package ru.hogwarts.school.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

    private final int serverPort;
    private final Environment environment;

    public InfoController(
            @Value("${server.port:8080}") int serverPort,
            Environment environment) {
        this.serverPort = serverPort;
        this.environment = environment;
    }

    @GetMapping("/port")
    public String getPort() {
        logger.info("Requested server port information");
        String activeProfile = String.join(", ", environment.getActiveProfiles());
        String response = String.format(
                "Application is running on port: %d (Profile: %s)",
                serverPort,
                activeProfile.isEmpty() ? "default" : activeProfile
        );
        logger.debug("Response: {}", response);
        return response;
    }
}
