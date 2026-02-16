package com.example.projectapi.application.school.converter;

import com.example.projectapi.application.school.dto.SchoolRequestDTO;
import com.example.projectapi.application.school.dto.SchoolResponseDTO;
import com.example.projectapi.application.school.dto.SchoolUpdateDTO;
import com.example.projectapi.domain.school.model.SchoolEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SchoolConverter {

    @Mapping(source = "user.email", target = "email")
    SchoolResponseDTO toResponseDTO (SchoolEntity school);
    SchoolEntity toEntity (SchoolRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(SchoolUpdateDTO dto, @MappingTarget SchoolEntity school);
}
