package com.example.projectapi.application.student.converter;

import com.example.projectapi.application.student.dto.StudentRequestDTO;
import com.example.projectapi.application.student.dto.StudentResponseDTO;
import com.example.projectapi.application.student.dto.StudentUpdateDTO;
import com.example.projectapi.domain.student.model.StudentEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentConverter {

    @Mapping(source = "user.email", target = "email")
    StudentResponseDTO toResponseDTO (StudentEntity student);
    StudentEntity toEntity (StudentRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(StudentUpdateDTO dto, @MappingTarget StudentEntity student);

}
