package com.example.POD.Controller.Admin;

import com.example.POD.DTO.ActiveDeactiveStatusDTO;
import com.example.POD.Service.ChangeStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/api/admin")
public class ChangeStatusOfUser {

    private  final ChangeStatusService changeStatusService;
    @CacheEvict(value = {"ApprovalRequests", "ViewActionsList"}, allEntries = true)
    @PutMapping("/changeStatus/{userId}")
    public String changeStatus(@PathVariable Long userId, @RequestBody ActiveDeactiveStatusDTO status)
    {
     return changeStatusService.changingStatus(userId,status.getStatus());
    }

}
