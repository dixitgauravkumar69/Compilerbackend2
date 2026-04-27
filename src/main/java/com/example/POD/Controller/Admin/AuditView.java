package com.example.POD.Controller.Admin;

import com.example.POD.DTO.AuditViewFullDTO;
import com.example.POD.Entity.AuditEntity;
import com.example.POD.Service.AuditService;
import com.example.POD.Service.AuditViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")

@PreAuthorize("hasRole('ROLE_ADMIN')")

@RequiredArgsConstructor
public class AuditView {
    private final AuditViewService auditViewService;
    @Cacheable("ViewActionsList")
    @GetMapping("/view/actions")
    public List<AuditViewFullDTO> viewWorkHistory()
    {

        return auditViewService.gettingHistoryWork();
    }

}
