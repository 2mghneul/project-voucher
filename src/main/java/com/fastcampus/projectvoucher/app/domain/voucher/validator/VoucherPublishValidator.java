package com.fastcampus.projectvoucher.app.domain.voucher.validator;

import com.fastcampus.projectvoucher.app.storagy.voucher.ContractEntity;
import org.springframework.stereotype.Component;

@Component
public class VoucherPublishValidator {
    public void validate(ContractEntity contractEntity) {
        if (contractEntity.isExpired()) {
            throw new IllegalStateException("유효기간이 지난 계약입니다.");
        }

    }
}
