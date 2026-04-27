package com.example.POD.Controller.Admin;

import com.example.POD.Entity.UserEntity;
import com.example.POD.Service.AllApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AllApprovalRequests {
    public final AllApprovalService allApprovalService;
    @Cacheable("ApprovalRequests")
    @GetMapping("/getApprovalRequests")
    public List<UserEntity> getApprovalList()
    {
        return allApprovalService.approvalRequestService();
    }
}
