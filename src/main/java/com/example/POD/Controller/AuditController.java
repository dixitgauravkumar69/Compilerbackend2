package com.example.POD.Controller;

import com.example.POD.DTO.AuditRequestDTO;
import com.example.POD.Repository.AuditRepository;
import com.example.POD.Service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/User")
public class AuditController {

    private final AuditService auditService;

    @PostMapping("/addAudit/{auditTo}/{auditBy}")

    public String addAudit(@PathVariable Long auditTo, @PathVariable Long auditBy, @RequestBody AuditRequestDTO auditRequestDTO)
    {
      return auditService.addingAudit(auditTo,auditBy,auditRequestDTO);
    }
}
