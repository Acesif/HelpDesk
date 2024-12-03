package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.dto.Response;
import com.grs.helpdeskmodule.entity.Settings;
import com.grs.helpdeskmodule.repository.SettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsRepository settingsRepository;

    @GetMapping("/app_background")
    public Response<?> getAppBackground() {

        Settings settings = settingsRepository.findByKey("app_background");

        return Response.builder()
                .status(HttpStatus.OK)
                .data(settings.getValue())
                .message("App background")
                .build();
    }

    @PostMapping("/app_background")
    public Response<?> getAppBackground(@RequestParam String filePath) {

        Settings settings = settingsRepository.findByKey("app_background");
        settings.setValue(filePath);
        settingsRepository.save(settings);

        return Response.builder()
                .status(HttpStatus.OK)
                .data(settings.getValue())
                .message("App background")
                .build();
    }
}
