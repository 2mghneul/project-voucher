package com.fastcampus.projectvoucher.app.domain.voucher;

import com.fastcampus.projectvoucher.app.common.dto.RequestContext;
import com.fastcampus.projectvoucher.app.common.type.VoucherAmountType;
import com.fastcampus.projectvoucher.app.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.app.storagy.voucher.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class VoucherService {
    private final VoucherRepository voucherRepository;
    private final ContractRepository contractRepository;
    public VoucherService(VoucherRepository voucherRepository, ContractRepository contractRepository) {
        this.voucherRepository = voucherRepository;
        this.contractRepository = contractRepository;
    }

    // 상품권 발행
    @Transactional
    public String publish(RequestContext requestContext, String contractCode, VoucherAmountType amount) {
        final String code = UUID.randomUUID().toString().toUpperCase().replaceAll("-","");
        final String order_id = "order_id";

        ContractEntity contractEntity = contractRepository.findByCode(contractCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계약입니다."));

        final VoucherHistoryEntity voucherHistoryEntity =
                new VoucherHistoryEntity(order_id, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.PUBLISH, "테스트 발행");
        final VoucherEntity voucherEntity = new VoucherEntity(code, VoucherStatusType.PUBLISH, amount, voucherHistoryEntity, contractEntity);

        return voucherRepository.save(voucherEntity).code();
    }

    // 상품권 사용 불가 처리
    @Transactional
    public void disable(RequestContext requestContext, String code) {
        final String order_id = "order_id";
        final VoucherHistoryEntity voucherHistoryEntity =
                new VoucherHistoryEntity(order_id, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.DISABLE, "테스트 사용 불가");

        VoucherEntity entity = voucherRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품권이 존재하지 않습니다."));

        entity.disable(voucherHistoryEntity);
    }

    // 상품권 사용
    @Transactional
    public void use(RequestContext requestContext,  String code) {
        final String order_id = "order_id";
        final VoucherHistoryEntity voucherHistoryEntity =
                new VoucherHistoryEntity(order_id, requestContext.requesterType(), requestContext.requesterId(), VoucherStatusType.USE, "테스트 사용");

        VoucherEntity entity = voucherRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품권이 존재하지 않습니다."));

        entity.use(voucherHistoryEntity);
    }
}
