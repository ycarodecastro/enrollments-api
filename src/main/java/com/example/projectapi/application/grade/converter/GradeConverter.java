package com.example.projectapi.application.grade.converter;

import com.example.projectapi.application.grade.dto.GradeRequestDTO;
import com.example.projectapi.application.grade.dto.GradeResponseDTO;
import com.example.projectapi.application.grade.dto.GradeUpdateDTO;
import com.example.projectapi.domain.grade.model.GradeEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GradeConverter {

    @Mapping(target = "subjectName", source = "subject.name")
    GradeResponseDTO toResponseDTO(GradeEntity grade);
    GradeEntity toEntity(GradeRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(GradeUpdateDTO dto, @MappingTarget GradeEntity grade);

}
