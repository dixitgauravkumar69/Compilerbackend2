package com.example.POD.Service;

import com.example.POD.DTO.AuditViewFullDTO;
import com.example.POD.Entity.AuditEntity;
import com.example.POD.Repository.AuditRepository;
import com.example.POD.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class AuditViewService {
    public final AuditRepository auditRepository;
    private final UserRepository userRepository;
    public List<AuditViewFullDTO> gettingHistoryWork()
    {
        //For returning with this format
        List<AuditViewFullDTO> View_Work_History=new ArrayList<>();

        //Work_History have whole data of audit table
       List<AuditEntity>Work_History =auditRepository.findAll();

       for(AuditEntity work:Work_History)
       {
           AuditViewFullDTO auditData=new AuditViewFullDTO();

           auditData.setAction(work.getAction());

           auditData.setReason(work.getReason());

           auditData.setActionDate(work.getCreatedAt());

           auditData.setUpdatedBy(work.getUpdatedBy());

           //Jisne update kia nam hai
           auditData.setUpdatedByName(userRepository.findUsernameByUserid(auditData.getUpdatedBy()));


                auditData.setUserId(work.getUserId());
                //Jo effected user hai uska nam
           auditData.setEffectedUser(userRepository.findUsernameByUserid(auditData.getUserId()));


           View_Work_History.add(auditData);
       }

       return View_Work_History;
    }
}
