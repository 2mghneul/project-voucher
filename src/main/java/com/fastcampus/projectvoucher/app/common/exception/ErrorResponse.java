package com.fastcampus.projectvoucher.app.common.exception;

import java.time.LocalDateTime;
import java.util.UUID;

public record ErrorResponse(
        String message,
        LocalDateTime timestamp,
        UUID traceId
) {}
