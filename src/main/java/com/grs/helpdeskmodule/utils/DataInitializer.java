package com.grs.helpdeskmodule.utils;

import com.grs.helpdeskmodule.entity.Priorities;
import com.grs.helpdeskmodule.entity.Settings;
import com.grs.helpdeskmodule.entity.User;
import com.grs.helpdeskmodule.repository.PriorityRepository;
import com.grs.helpdeskmodule.repository.SettingsRepository;
import com.grs.helpdeskmodule.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final PriorityRepository priorityRepository;
    private final SettingsRepository settingsRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Override
    public void run(String... args) {
        initiateUser();
        initiatePriority();
        initiateSettings();
    }
    private void initiateUser(){
        if (userService.findUserByEmail("aaa@nai.com") == null){
            userService.save(
                    User.builder()
                            .flag(true)
                            .name("test user")
                            .email("aaa@nai.com")
                            .officeId(1L)
                            .password(passwordEncoder.encode("nullhobe"))
                            .phoneNumber("01111111111")
                            .designation("GRO")
                            .build()
            );
        }
    }
    private void initiatePriority(){

        if (priorityRepository.findAll().isEmpty()){
            List<Priorities> priorities = new ArrayList<>();
            priorities.add(
                    Priorities.builder()
                            .id(1L)
                            .name("LOW")
                            .value(1)
                            .build()
            );
            priorities.add(
                    Priorities.builder()
                            .id(2L)
                            .name("MEDIUM")
                            .value(2)
                            .build()
            );
            priorities.add(
                    Priorities.builder()
                            .id(3L)
                            .name("HIGH")
                            .value(3)
                            .build()
            );
            priorities.add(
                    Priorities.builder()
                            .id(4L)
                            .name("URGENT")
                            .value(4)
                            .build()
            );

            priorityRepository.saveAllAndFlush(priorities);
        }
    }

    private void initiateSettings(){
        List<Settings> settings = new ArrayList<>();

        if (settingsRepository.findAll().isEmpty()){
            settings.add(
              Settings.builder()
                      .key("app_background")
                      .value("default")
                      .build()
            );
            settings.add(
                    Settings.builder()
                            .key("app_background")
                            .value("default")
                            .build()
            );
            settings.add(
                    Settings.builder()
                            .key("app_date_format")
                            .value("L")
                            .build()
            );
            settings.add(
                    Settings.builder()
                            .key("app_date_locale")
                            .value("en")
                            .build()
            );
            settings.add(
                    Settings.builder()
                            .key("app_icon")
                            .value("default")
                            .build()
            );
            settings.add(
                    Settings.builder()
                            .key("app_locale")
                            .value("en_US")
                            .build()
            );
            settings.add(
                    Settings.builder()
                            .key("app_name")
                            .value("HelpDesk")
                            .build()
            );
            settings.add(
                    Settings.builder()
                            .key("app_timezone")
                            .value("UTC")
                            .build()
            );
        }
        settingsRepository.saveAllAndFlush(settings);
    }
}
