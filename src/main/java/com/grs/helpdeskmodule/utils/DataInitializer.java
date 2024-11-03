package com.grs.helpdeskmodule.utils;

import com.grs.helpdeskmodule.entity.*;
import com.grs.helpdeskmodule.repository.PermissionRepository;
import com.grs.helpdeskmodule.repository.PriorityRepository;
import com.grs.helpdeskmodule.repository.RoleRepository;
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
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Override
    public void run(String... args) {
        initiateUser();
        initiatePriority();
        initiateSettings();
        initiateRolePermissions();
    }
    private void initiateUser(){
        if (userService.findUserByEmail("superadmin@admin.com") == null){
            userService.save(
                    User.builder()
                            .flag(true)
                            .name("SUPERADMIN")
                            .email("superadmin@admin.com")
                            .officeId(null)
                            .password(passwordEncoder.encode("12345678"))
                            .phoneNumber(null)
                            .designation("SUPERADMIN")
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

    private void initiateRolePermissions(){

//        roleRepository.deleteAll();
//        permissionRepository.deleteAll();

        /*
            Dashboard & Settings
         */
        Permissions viewDashboaard = Permissions.builder()
                .id(1L)
                .permission("CAN VIEW DASHBOARD")
                .build();
        Permissions editSettings = Permissions.builder()
                .id(2L)
                .permission("CAN MODIFY SETTINGS")
                .build();

        /*
            Users
         */
        Permissions setUserRoles = Permissions.builder()
                .id(3L)
                .permission("CAN MODIFY ROLES")
                .build();

        Permissions editUsers = Permissions.builder()
                .id(4L)
                .permission("CAN MODIFY USERS")
                .build();

        Permissions createUser = Permissions.builder()
                .id(5L)
                .permission("CAN CREATE USER")
                .build();

        /*
            Issues
         */
        Permissions postIssues = Permissions.builder()
                .id(6L)
                .permission("CAN POST ISSUES")
                .build();

        Permissions replyToIssues = Permissions.builder()
                .id(7L)
                .permission("CAN RESPOND TO ISSUES")
                .build();
        Permissions viewIssues = Permissions.builder()
                .id(8L)
                .permission("CAN VIEW ISSUES")
                .build();

        Permissions viewOwnIssues = Permissions.builder()
                .id(9L)
                .permission("CAN VIEW OWN ISSUES")
                .build();

        Permissions setPriorities = Permissions.builder()
                .id(10L)
                .permission("CAN SET PRIORITY TO ISSUES")
                .build();

        if (permissionRepository.findAll().isEmpty()){
            permissionRepository.saveAll(List.of(
                    viewDashboaard,
                    editSettings,
                    setUserRoles,
                    editUsers,
                    createUser,
                    postIssues,
                    replyToIssues,
                    viewIssues,
                    viewOwnIssues,
                    setPriorities)
            );
        }

        viewDashboaard = permissionRepository.findById(1L).orElseThrow();
        editSettings = permissionRepository.findById(2L).orElseThrow();
        setUserRoles = permissionRepository.findById(3L).orElseThrow();
        editUsers = permissionRepository.findById(4L).orElseThrow();
        createUser = permissionRepository.findById(5L).orElseThrow();
        postIssues = permissionRepository.findById(6L).orElseThrow();
        replyToIssues = permissionRepository.findById(7L).orElseThrow();
        viewIssues = permissionRepository.findById(8L).orElseThrow();
        viewOwnIssues = permissionRepository.findById(9L).orElseThrow();
        setPriorities = permissionRepository.findById(10L).orElseThrow();

        if (roleRepository.findAll().isEmpty()) {
            List<Role> roles = new ArrayList<>();
            roles.add(
                    Role.builder()
                            .id(1L)
                            .role("ADMIN")
                            .permissions(List.of(
                                    viewDashboaard,
                                    replyToIssues,
                                    viewIssues,
                                    viewDashboaard,
                                    setPriorities)
                            )
                            .build()
            );

            roles.add(
                    Role.builder()
                            .id(2L)
                            .role("VENDOR")
                            .permissions(List.of(
                                    viewDashboaard,
                                    editSettings,
                                    setUserRoles,
                                    editUsers,
                                    viewIssues,
                                    replyToIssues,
                                    setPriorities,
                                    postIssues,
                                    createUser)
                            )
                            .build()
            );

            roles.add(
                    Role.builder()
                            .id(3L)
                            .role("SUPERADMIN")
                            .permissions(List.of(
                                    viewDashboaard,
                                    editSettings,
                                    setUserRoles,
                                    editUsers,
                                    viewIssues,
                                    createUser)
                            )
                            .build()
            );

            roles.add(
                    Role.builder()
                            .id(4L)
                            .role("OFFICER")
                            .permissions(List.of(
                                    viewOwnIssues,
                                    createUser,
                                    editUsers,
                                    postIssues)
                            )
                            .build()
            );
            roleRepository.saveAll(roles);
        }
    }
}
