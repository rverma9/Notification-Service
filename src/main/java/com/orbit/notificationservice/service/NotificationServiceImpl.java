package com.orbit.notificationservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.orbit.notificationservice.dto.NotificationRequestDto;
import com.orbit.notificationservice.dto.NotificationResponseDto;
import com.orbit.notificationservice.exception.NotificationNotFoundException;
import com.orbit.notificationservice.model.Notification;
import com.orbit.notificationservice.repository.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}

	@Override
    public NotificationResponseDto createNotification(NotificationRequestDto request) {

        Notification notification = new Notification();

        notification.setUserId(request.getUserId());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());

        return mapToDto(notificationRepository.save(notification));
    }

    @Override
    public NotificationResponseDto getNotificationById(Long id) throws NotificationNotFoundException {

        Notification notification =
                notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found"));

        return mapToDto(notification);
    }

    @Override
    public List<NotificationResponseDto> getNotificationsByUserId(String userId) {

        return notificationRepository
                .findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private NotificationResponseDto mapToDto(Notification notification) {

        NotificationResponseDto dto =
                new NotificationResponseDto();

        dto.setId(notification.getId());
        dto.setUserId(notification.getUserId());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());

        return dto;
    }
}
