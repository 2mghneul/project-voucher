package com.fastcampus.projectvoucher.app.storagy.voucher;

import com.fastcampus.projectvoucher.app.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.app.storagy.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Table(name = "voucher")
@Entity
public class VoucherEntity extends BaseEntity {
    @Column private String code;
    @Column private VoucherStatusType status;
    @Column private LocalDate validFrom;
    @Column private LocalDate validTo;
    @Column Long amount;

    public VoucherEntity() {
    }

    public VoucherEntity(String code, VoucherStatusType status, LocalDate validFrom, LocalDate validTo, Long amount) {
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

    public Long amount() { return amount; }
}
