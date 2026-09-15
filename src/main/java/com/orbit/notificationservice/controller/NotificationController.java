package com.orbit.notificationservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orbit.notificationservice.dto.NotificationRequestDto;
import com.orbit.notificationservice.dto.NotificationResponseDto;
import com.orbit.notificationservice.exception.NotificationNotFoundException;
import com.orbit.notificationservice.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
		super();
		this.notificationService = notificationService;
	}

	@PostMapping
    public ResponseEntity<NotificationResponseDto> createNotification(@RequestBody NotificationRequestDto request) {
        return ResponseEntity.ok(notificationService.createNotification(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDto> getNotificationById(@PathVariable Long id) throws NotificationNotFoundException {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponseDto>>getNotificationsByUserId(@PathVariable String userId) {

        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }
}
