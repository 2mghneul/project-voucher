package com.fastcampus.projectvoucher.app.common.type;

public enum VoucherAmountType {
    KRW_30000(30_000L),
    KRW_50000(50_000L),
    KRW_100000(100_000L)
    ;

    public final Long amount;

    VoucherAmountType(long amount) {
        this.amount = amount;
    }

    public Long amount() {
        return amount;
    }
}
