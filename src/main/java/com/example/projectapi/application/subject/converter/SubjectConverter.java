package com.example.projectapi.application.subject.converter;

import com.example.projectapi.application.subject.dto.SubjectRequestDTO;
import com.example.projectapi.application.subject.dto.SubjectResponseDTO;
import com.example.projectapi.domain.subject.model.SubjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SubjectConverter {
    SubjectResponseDTO toResponseDTO(SubjectEntity subject);
    SubjectEntity toEntity(SubjectRequestDTO dto);
}
