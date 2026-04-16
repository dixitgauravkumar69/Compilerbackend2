package com.example.POD.Controller;

import com.example.POD.Entity.NotificationEntity;
import com.example.POD.Repository.NotificationRepository;
import com.example.POD.Service.NotificationStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")

public class NotificationController {
    private final Map<Long, SseEmitter>userEmitters=new HashMap<>();

    private final NotificationRepository notify;
    private final NotificationStatusService notificationStatusService;

    @GetMapping("/subscribe/{userId}")

    public SseEmitter subscribe(@PathVariable Long userId)
    {
        SseEmitter sseEmitter=new SseEmitter(Long.MAX_VALUE);

        userEmitters.put(userId,sseEmitter);

        sseEmitter.onCompletion(()->userEmitters.remove(userId));
        sseEmitter.onTimeout(()->userEmitters.remove(userId));

        return sseEmitter;
    }


    public void sendToUser(Long userId,String message)
    {
        SseEmitter emitter=userEmitters.get(userId);
        System.out.println("Emitter for " + userId + ": " + emitter);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().data(message));
                System.out.println("added emit message");
            } catch (Exception e) {
                userEmitters.remove(userId);
            }
        }


    }




    @Cacheable(value="notificationCache")
    @GetMapping("/getNotification/{userId}")
    public List<NotificationEntity> getNotification(@PathVariable Long userId) {

        List<NotificationEntity> notifications= notify.findByUserId(userId);
        List<NotificationEntity> unreadNotifications=new ArrayList<>();

        for(NotificationEntity notificationItem:notifications)
        {
            if(notificationItem.isRead()==false)
            {
                unreadNotifications.add(notificationItem);
            }

            System.out.println(notificationItem);
        }


        return unreadNotifications;
    }



    @CacheEvict(value="notificationCache",allEntries = true)
    @PostMapping("/notification/markAsRead/{notificationId}")

    public boolean markAsRead(@PathVariable Long notificationId)
    {
     Optional<NotificationEntity> notifications= notificationStatusService.markAsRead(notificationId);
     return notifications.get().isRead();
    }

}
