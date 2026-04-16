package com.example.POD.Service;

import com.example.POD.Entity.NotificationEntity;
import com.example.POD.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationStatusService {

    private final NotificationRepository notificationRepository;
    public Optional<NotificationEntity> markAsRead(Long notId)
    {
       Optional<NotificationEntity> Notifications= notificationRepository.findById(notId);

        if(Notifications.isPresent()) {
            NotificationEntity notification = Notifications.get();
            notification.setRead(true);

            notificationRepository.save(notification);
        }
       return Notifications;
    }
}
