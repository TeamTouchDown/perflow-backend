/*
package com.touchdown.perflowbackend.workAttitude.command.mapper;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeApprovalRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.ApprovalRequest;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.DeleteStatus;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeApprovalRequestResponseDTO;

import java.time.LocalDateTime;

public class WorkAttitudeApprovalMapper {

    // DTO → Entity 변환
    public static ApprovalRequest toEntity(WorkAttitudeApprovalRequestDTO dto, Employee employee, Employee approver) {
        return ApprovalRequest.builder()
                .empId(employee)                           // 신청자 정보 (Employee 객체)
                .approverId(approver)                      // 결재자 정보 (Employee 객체)
                .relatedId(dto.getRelatedId())             // 관련 요청 ID
                .requestType(dto.getRequestType())         // 요청 타입
                .status(dto.getStatus() != null ? dto.getStatus() : Status.PENDING) // 기본 상태 설정
                .rejectReason(dto.getRejectReason())       // 반려 사유
                .deleteStatus(DeleteStatus.ACTIVATED)      // 기본 상태: 활성화
                .createDatetime(LocalDateTime.now())       // 생성 시간
                .updateDatetime(LocalDateTime.now())       // 업데이트 시간
                .build();
    }


    // **DTO → 기존 Entity 업데이트**
    public static void updateEntityFromDto(WorkAttitudeApprovalRequestDTO dto, ApprovalRequest approvalRequest) {
        if (dto.getStatus() != null) {
            approvalRequest.setStatus(dto.getStatus()); // 상태 변경
        }

        if (dto.getRejectReason() != null) {
            approvalRequest.setRejectReason(dto.getRejectReason()); // 반려 사유 업데이트
        }


        approvalRequest.setUpdateDatetime(LocalDateTime.now()); // 업데이트 시간 갱신
    }
    public static void softDelete(ApprovalRequest approvalRequest) {
        approvalRequest.setDeleteStatus(DeleteStatus.DELETED); // 삭제 상태 변경
        approvalRequest.setUpdateDatetime(LocalDateTime.now()); // 삭제 시간 갱신
    }


    // **Entity → DTO 변환**
    public static WorkAttitudeApprovalRequestResponseDTO toDto(ApprovalRequest approvalRequest) {
        return WorkAttitudeApprovalRequestResponseDTO.builder()
                .empId(approvalRequest.getEmpId().getEmpId())               // 신청자 ID
                .approverId(approvalRequest.getApproverId().getEmpId())     // 결재자 ID
                .relatedId(approvalRequest.getRelatedId())                  // 관련 ID
                .requestType(approvalRequest.getRequestType())              // 요청 타입
                .status(approvalRequest.getStatus())                        // 상태
                .rejectReason(approvalRequest.getRejectReason())            // 반려 사유
                .deleteStatus(approvalRequest.getDeleteStatus())            // 삭제 상태
                .createDatetime(approvalRequest.getCreateDatetime())        // 생성일시
                .updateDatetime(approvalRequest.getUpdateDatetime())        // 업데이트일시
                .build();
    }

}
*/
