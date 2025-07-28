package com.fastcampus.projectvoucher.app.controller.voucher.request;

import com.fastcampus.projectvoucher.app.common.type.RequesterType;

public record VoucherDisableRequest(
        RequesterType requesterType,
        String requesterId,
        String code
) {}
