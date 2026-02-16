package com.example.projectapi.application.address.converter;

import com.example.projectapi.application.address.dto.AddressRequestDTO;
import com.example.projectapi.application.address.dto.AddressResponseDTO;
import com.example.projectapi.application.address.dto.AddressUpdateDTO;
import com.example.projectapi.domain.address.model.AddressEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressConverter {
    AddressResponseDTO toResponseDTO(AddressEntity address);
    AddressEntity toEntity (AddressRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(AddressUpdateDTO dto, @MappingTarget AddressEntity address);
}
