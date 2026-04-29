package com.example.POD.Service;

import com.example.POD.DTO.AuditRequestDTO;
import com.example.POD.Entity.AuditEntity;
import com.example.POD.Entity.UserEntity;
import com.example.POD.Repository.AuditRepository;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuditService {
    public final UserRepository user;
    public final AuditRepository auditRepository;
    public String addingAudit(Long auditTo, Long auditBy, AuditRequestDTO auditInfo)
    {
        AuditEntity auditEntity=new AuditEntity();

        UserEntity Audited_By=user.findByuserid(auditBy);

        if(Audited_By==null)
        {
            return "User Not found";
        }

         auditEntity.setUpdatedBy(Audited_By.getUserid());

        UserEntity audited_To=user.findByuserid(auditTo);

        if(audited_To==null)
        {
            return "Auditing user not found";
        }


        auditEntity.setUserId(auditTo);

        auditEntity.setReason(auditInfo.getReason());
        auditEntity.setAction(auditInfo.getAction());



        auditRepository.save(auditEntity);
        return "Your valuable action is captured";

    }
}
