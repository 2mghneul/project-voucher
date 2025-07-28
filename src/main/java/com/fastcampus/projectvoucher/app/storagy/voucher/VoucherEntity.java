package com.fastcampus.projectvoucher.app.storagy.voucher;

import com.fastcampus.projectvoucher.app.common.type.VoucherAmountType;
import com.fastcampus.projectvoucher.app.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.app.storagy.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Table(name = "voucher")
@Entity
public class VoucherEntity extends BaseEntity {
    @Column private String code;
    @Enumerated(EnumType.STRING)
    @Column private VoucherStatusType status;
    @Column private LocalDate validFrom;
    @Column private LocalDate validTo;
    @Enumerated(EnumType.STRING)
    @Column VoucherAmountType amount;

    public VoucherEntity() {
    }

    public VoucherEntity(String code, VoucherStatusType status, LocalDate validFrom, LocalDate validTo, VoucherAmountType amount) {
        this.code = code;
        this.status = status;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.amount = amount;
    }

    public String code() { return code; }

    public VoucherStatusType status() { return status; }

    public LocalDate validFrom() { return validFrom; }

    public LocalDate validTo() { return validTo; }

    public VoucherAmountType amount() { return amount; }

    public void disable() {
        if (!this.status.equals(VoucherStatusType.PUBLISH)) {
            throw new IllegalStateException("사용 뷸가 처리할 수 없는 상태의 상품권 입니다.");
        }

        this.status = VoucherStatusType.DISABLE;
    }

    public void use() {
        if (!this.status.equals(VoucherStatusType.PUBLISH)) {
            throw new IllegalStateException("사용할 수 없는 상태의 상품권 입니다.");
        }

        this.status = VoucherStatusType.USE;
    }
}
