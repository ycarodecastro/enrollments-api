package com.example.projectapi.application.offer.converter;

import com.example.projectapi.application.offer.dto.OfferRequestDTO;
import com.example.projectapi.application.offer.dto.OfferResponseDTO;
import com.example.projectapi.application.offer.dto.OfferUpdateDTO;
import com.example.projectapi.domain.offer.model.OfferEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OfferConverter {

    // O MapStruct vai fazer: offer.getSchool().getName()
    @Mapping(target = "schoolName", source = "school.name")
    @Mapping(target = "totalInscribed", expression = "java(offer.getInscribes() != null ? offer.getInscribes().size() : 0)")
    OfferResponseDTO toResponseDTO(OfferEntity offer);

    // Ignora a escola na criação, pois vamos setar manualmente via UseCase
    @Mapping(target = "school", ignore = true)
    OfferEntity toEntity(OfferRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "school", ignore = true)
    void updateEntityFromDTO(OfferUpdateDTO dto, @MappingTarget OfferEntity offer);
}