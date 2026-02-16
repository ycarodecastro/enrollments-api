package com.example.projectapi.application.common;

import java.util.List;

public record ResponseDefault<T>(
   boolean success,
   T data,
   String message,
   List<String> errors
) {}
