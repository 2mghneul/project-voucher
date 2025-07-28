package com.fastcampus.projectvoucher.app.domain.voucher;

import com.fastcampus.projectvoucher.app.common.dto.RequestContext;
import com.fastcampus.projectvoucher.app.common.type.RequesterType;
import com.fastcampus.projectvoucher.app.common.type.VoucherAmountType;
import com.fastcampus.projectvoucher.app.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.app.storagy.voucher.VoucherEntity;
import com.fastcampus.projectvoucher.app.storagy.voucher.VoucherHistoryEntity;
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
    public String publish(RequestContext requestContext, LocalDate validFrom, LocalDate validTo, VoucherAmountType amount) {
        final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-","");
        final String order_id = "order_id";

        final VoucherHistoryEntity voucherHistoryEntity =
                new VoucherHistoryEntity(order_id, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.PUBLISH, "테스트 발행");
        final VoucherEntity entity = new VoucherEntity(code, VoucherStatusType.PUBLISH, validFrom, validTo, amount, voucherHistoryEntity);

        return voucherRepository.save(entity).code();
    }

    // 상품권 사용 불가 처리
    @Transactional
    public void disable(RequestContext requestContext, String code) {
        final String order_id = "order_id";
        final VoucherHistoryEntity voucherHistoryEntity =
                new VoucherHistoryEntity(order_id, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.DISABLE, "테스트 사용 불가");

        VoucherEntity entitiy = voucherRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품권이 존재하지 않습니다."));

        entitiy.disable(voucherHistoryEntity);
    }

    // 상품권 사용
    @Transactional
    public void use(RequestContext requestContext,  String code) {
        final String order_id = "order_id";
        final VoucherHistoryEntity voucherHistoryEntity =
                new VoucherHistoryEntity(order_id, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.USE, "테스트 사용");

        VoucherEntity entitiy = voucherRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품권이 존재하지 않습니다."));

        entitiy.use(voucherHistoryEntity);
    }
}
