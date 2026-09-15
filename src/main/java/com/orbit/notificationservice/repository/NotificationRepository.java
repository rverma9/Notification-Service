package com.orbit.notificationservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.orbit.notificationservice.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findByUserId(String userId);
}
