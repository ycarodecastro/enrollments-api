package com.example.projectapi.domain.student.event;

import java.time.Year;

public record StudentEnrolledEvent(Long studentId, Long schoolId, Year schoolYear) {}
