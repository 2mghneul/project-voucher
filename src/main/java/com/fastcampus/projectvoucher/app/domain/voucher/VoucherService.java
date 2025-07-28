package com.fastcampus.projectvoucher.app.domain.voucher;

import com.fastcampus.projectvoucher.app.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.app.storagy.voucher.VoucherEntity;
import com.fastcampus.projectvoucher.app.storagy.voucher.VoucherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VoucherService {
    private final VoucherRepository voucherRepository;
    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    // 상품권 발행
    @Transactional
    public String publish(LocalDate validFrom, LocalDate validTo, Long amount) {
        final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-","");
        final VoucherEntity entity = new VoucherEntity(code, VoucherStatusType.PUBLISH, validFrom, validTo, amount);

        return voucherRepository.save(entity).code();
    }

    // 상품권 취소
    public void disable(String code) {}

    // 상품권 사용


}
