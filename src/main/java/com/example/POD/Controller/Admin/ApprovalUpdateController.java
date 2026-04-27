package com.example.POD.Controller.Admin;

import com.example.POD.Service.UpdateApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")

@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ApprovalUpdateController {
    private final UpdateApprovalService updateApprovalService;
    @CacheEvict(value = {"ApprovalRequests", "ViewActionsList"}, allEntries = true)
    @PutMapping("/updateApproval/{userId}/{status}")
    public String updateApproval(@PathVariable Long userId,@PathVariable Boolean status)
    {
        return updateApprovalService.updateApproval(userId,status);
    }

}
