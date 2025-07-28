package com.fastcampus.projectvoucher.app.controller.voucher.request;

import com.fastcampus.projectvoucher.app.common.type.RequesterType;

public record VoucherUseRequest(
        RequesterType requesterType,
        String requesterId,
        String code
) {}
