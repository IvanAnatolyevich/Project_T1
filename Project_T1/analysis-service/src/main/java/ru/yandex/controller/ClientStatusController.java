package ru.yandex.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.model.ClientStatus;
import ru.yandex.model.dto.ClientStatusResponse;

import java.util.Random;

 @RestController
 @RequestMapping("/api/client-status")
 public class ClientStatusController {
     @GetMapping
     public ResponseEntity<ClientStatusResponse> checkClient(@RequestParam String clientId,
                                                             @RequestParam String accountId) {
         boolean isBlacklisted = new Random().nextInt(100) < 10;
         ClientStatus status = isBlacklisted ? ClientStatus.BLOCKED : ClientStatus.OK;
         return ResponseEntity.ok(new ClientStatusResponse(clientId, accountId, status));
     }
 }
