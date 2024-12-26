package com.grs.helpdeskmodule.controller;

import com.grs.helpdeskmodule.dto.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DBReset {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @GetMapping("/drop/users")
    public ResponseEntity<Response<?>> dropUsers() {
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        String sql = "DELETE FROM helpdesk.user WHERE email <> 'superadmin@admin.com'";
        entityManager.createNativeQuery(sql).executeUpdate();
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        return ResponseEntity.ok().body(
                Response.builder()
                        .status(HttpStatus.OK)
                        .message("Users table dropped")
                        .data(null)
                        .build()
        );
    }

    @Transactional
    @GetMapping("/drop/offices")
    public ResponseEntity<Response<?>> dropOffices() {
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        String sql = "TRUNCATE `helpdesk`.`office`;";
        entityManager.createNativeQuery(sql).executeUpdate();
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        return ResponseEntity.ok().body(
                Response.builder()
                        .status(HttpStatus.OK)
                        .message("Offices table dropped")
                        .data(null)
                        .build()
        );
    }
}
