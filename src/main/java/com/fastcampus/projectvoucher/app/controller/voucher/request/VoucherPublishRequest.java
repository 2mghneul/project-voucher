package com.fastcampus.projectvoucher.app.controller.voucher.request;

import com.fastcampus.projectvoucher.app.common.type.RequesterType;
import com.fastcampus.projectvoucher.app.common.type.VoucherAmountType;

public record VoucherPublishRequest(
        RequesterType requesterType,
        String requesterId,
        String contractCode,
        VoucherAmountType amount
) {}
