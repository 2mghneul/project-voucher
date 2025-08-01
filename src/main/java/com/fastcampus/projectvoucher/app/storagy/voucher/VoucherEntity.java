package com.fastcampus.projectvoucher.app.storagy.voucher;

import com.fastcampus.projectvoucher.app.common.type.VoucherAmountType;
import com.fastcampus.projectvoucher.app.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.app.storagy.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name ="voucher_id")
    private List<VoucherHistoryEntity> histories = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "contract_id")
    private ContractEntity contract;

    public VoucherEntity() {
    }

    public VoucherEntity(
            String code, VoucherStatusType status,
            VoucherAmountType amount,
            VoucherHistoryEntity voucherHistoryEntity,
            ContractEntity contractEntity
    ) {
        this.code = code;
        this.status = status;
        this.validFrom = LocalDate.now();
        this.validTo = LocalDate.now().plusDays(contractEntity.voucherValidPeriodDayCount());
        this.amount = amount;

        this.histories.add(voucherHistoryEntity);
        this.contract = contractEntity;
    }

    public String code() { return code; }

    public VoucherStatusType status() { return status; }

    public LocalDate validFrom() { return validFrom; }

    public LocalDate validTo() { return validTo; }

    public VoucherAmountType amount() { return amount; }

    public List<VoucherHistoryEntity> histories() { return histories; }

    public VoucherHistoryEntity publishHistory() {
        return histories.stream()
                .filter(voucherHistoryEntity -> voucherHistoryEntity.status().equals(VoucherStatusType.PUBLISH))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("발행 이력이 존재하지 않습니다."));
    }

    public void disable(VoucherHistoryEntity voucherHistoryEntity) {
        if (!this.status.equals(VoucherStatusType.PUBLISH)) {
            throw new IllegalStateException("사용 뷸가 처리할 수 없는 상태의 상품권 입니다.");
        }

        this.status = VoucherStatusType.DISABLE;
        this.histories.add(voucherHistoryEntity);
    }

    public void use(VoucherHistoryEntity voucherHistoryEntity) {
        if (!this.status.equals(VoucherStatusType.PUBLISH)) {
            throw new IllegalStateException("사용할 수 없는 상태의 상품권 입니다.");
        }

        this.status = VoucherStatusType.USE;
        this.histories.add(voucherHistoryEntity);
    }
}
