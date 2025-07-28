package com.fastcampus.projectvoucher.app.domain.voucher;

import com.fastcampus.projectvoucher.app.common.type.RequesterType;
import com.fastcampus.projectvoucher.app.common.type.VoucherAmountType;
import com.fastcampus.projectvoucher.app.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.app.storagy.voucher.VoucherEntity;
import com.fastcampus.projectvoucher.app.storagy.voucher.VoucherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class VoucherService {
    private final VoucherRepository voucherRepository;
    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    // 상품권 발행
    @Transactional
    public String publish(RequesterType requesterType, String requesterId, LocalDate validFrom, LocalDate validTo, VoucherAmountType amount) {
        final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-","");
        final VoucherEntity entity = new VoucherEntity(code, VoucherStatusType.PUBLISH, validFrom, validTo, amount);

        return voucherRepository.save(entity).code();
    }

    // 상품권 사용 불가 처리
    @Transactional
    public void disable(RequesterType requesterType, String requesterId, String code) {
        VoucherEntity entitiy = voucherRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품권이 존재하지 않습니다."));

        entitiy.disable();
    }

    // 상품권 사용
    @Transactional
    public void use(RequesterType requesterType, String requesterId,  String code) {
        VoucherEntity entitiy = voucherRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품권이 존재하지 않습니다."));

        entitiy.use();
    }
}
