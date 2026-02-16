package com.example.projectapi.infra.event.student;

import com.example.projectapi.domain.grade.model.GradeEntity;
import com.example.projectapi.domain.student.event.StudentEnrolledEvent;
import com.example.projectapi.domain.student.model.StudentEntity;
import com.example.projectapi.domain.student.repository.StudentRepository;
import com.example.projectapi.domain.subject.model.SubjectEntity;
import com.example.projectapi.domain.subject.repository.SubjectRepository;
import com.example.projectapi.domain.transcript.model.TranscriptEntity;
import com.example.projectapi.domain.transcript.repository.TranscriptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class StudentEventListener {

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository; // Para buscar as matérias da escola
    private final TranscriptRepository transcriptRepository;

    @EventListener
    @Transactional
    public void handleStudentEnrolled (StudentEnrolledEvent event) {

        log.info("Criando esqueleto de boletim para o aluno {}", event.studentId());

        StudentEntity student = studentRepository.findById(event.studentId()).orElseThrow();

        List<SubjectEntity> subjects = subjectRepository.findBySchoolId(event.schoolId());

        TranscriptEntity transcript = new TranscriptEntity();
        transcript.setStudent(student);
        transcript.setSchool(student.getSchool());
        transcript.setSchoolYear(event.schoolYear());

        List<GradeEntity> emptyGrades = subjects.stream().map(subject -> {
            GradeEntity grade = new GradeEntity();
            grade.setSubject(subject);
            grade.setPeriod("1° Bimestre");
            grade.setValue(0.0); // Começa zerado
            grade.setTranscript(transcript); // Vincula ao boletim
            return grade;
        }).toList();

        transcript.setGrades(emptyGrades);
        transcriptRepository.save(transcript);

        log.info("Boletim criado com {} matérias zeradas.", emptyGrades.size());
    }
}
