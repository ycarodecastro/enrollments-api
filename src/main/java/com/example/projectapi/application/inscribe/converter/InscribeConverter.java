package com.example.projectapi.application.inscribe.converter;

import com.example.projectapi.application.inscribe.dto.InscribeRequestDTO;
import com.example.projectapi.application.inscribe.dto.InscribeResponseDTO;
import com.example.projectapi.application.inscribe.dto.InscribeUpdateDTO;
import com.example.projectapi.domain.inscribe.model.InscribeEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InscribeConverter {
    @Mapping(target = "studentName", source = "student.name")
    @Mapping(target = "offer_id", source = "offer.id")
    InscribeResponseDTO toResponseDTO (InscribeEntity inscribe);
    InscribeEntity toEntity (InscribeRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(InscribeUpdateDTO dto, @MappingTarget InscribeEntity inscribe);
}
