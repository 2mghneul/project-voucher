package com.fastcampus.projectvoucher.app.common.dto;

import com.fastcampus.projectvoucher.app.common.type.RequesterType;

public record RequestContext(
        RequesterType requesterType,
        String requesterId
) {}
