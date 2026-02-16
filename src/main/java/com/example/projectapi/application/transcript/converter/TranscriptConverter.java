package com.example.projectapi.application.transcript.converter;

import com.example.projectapi.application.grade.converter.GradeConverter;
import com.example.projectapi.application.transcript.dto.TranscriptRequestDTO;
import com.example.projectapi.application.transcript.dto.TranscriptResponseDTO;
import com.example.projectapi.domain.transcript.model.TranscriptEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        // Com o uses ele não ignora o mapping do gradeConverter
        // Mostrando o subject.name
        uses = GradeConverter.class
)
public interface TranscriptConverter {

    @Mapping(target = "studentName", source = "student.name")
    @Mapping(target = "studentCpf", source = "student.cpf")
    @Mapping(target = "studentRg", source = "student.rg")
    @Mapping(target = "schoolName", source = "school.name")
    TranscriptResponseDTO toResponseDTO(TranscriptEntity transcript);
    TranscriptEntity toEntity (TranscriptRequestDTO dto);

}
