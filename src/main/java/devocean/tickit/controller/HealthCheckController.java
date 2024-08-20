package devocean.tickit.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health-check")
@RequiredArgsConstructor
public class HealthCheckController {
    @GetMapping
    public String healthCheck() {
        return "Good :)";
    }
}