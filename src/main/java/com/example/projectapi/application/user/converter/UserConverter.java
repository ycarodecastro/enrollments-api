package com.example.projectapi.application.user.converter;

import com.example.projectapi.application.user.dto.UserRequestDTO;
import com.example.projectapi.application.user.dto.UserResponseDTO;
import com.example.projectapi.application.user.dto.UserUpdateDTO;
import com.example.projectapi.domain.user.model.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter {

    UserResponseDTO toResponseDTO (UserEntity user);
    UserEntity toEntity (UserRequestDTO dto);

    // faz com que os campos nulos nao sobreescreva
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UserUpdateDTO dto, @MappingTarget UserEntity user);
}
