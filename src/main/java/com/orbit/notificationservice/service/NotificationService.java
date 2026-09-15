package com.orbit.notificationservice.service;

import java.util.List;

import com.orbit.notificationservice.dto.NotificationRequestDto;
import com.orbit.notificationservice.dto.NotificationResponseDto;
import com.orbit.notificationservice.exception.NotificationNotFoundException;

public interface NotificationService {

	NotificationResponseDto createNotification(NotificationRequestDto request);

    NotificationResponseDto getNotificationById(Long id) throws NotificationNotFoundException;

    List<NotificationResponseDto>getNotificationsByUserId(
            String userId);
}
