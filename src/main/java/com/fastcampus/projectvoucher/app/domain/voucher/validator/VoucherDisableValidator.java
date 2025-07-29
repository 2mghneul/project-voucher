package com.fastcampus.projectvoucher.app.domain.voucher.validator;

import com.fastcampus.projectvoucher.app.common.dto.RequestContext;
import com.fastcampus.projectvoucher.app.storagy.voucher.VoucherEntity;
import org.springframework.stereotype.Component;

@Component
public class VoucherDisableValidator {
    public void validate(VoucherEntity voucherEntity, RequestContext requestContext) {
        발품권_사용_불가_권한이_있는지_확인(voucherEntity, requestContext);
    }

    private static void 발품권_사용_불가_권한이_있는지_확인(VoucherEntity voucherEntity, RequestContext requestContext) {
        if (voucherEntity.publishHistory().requesterType() != requestContext.requesterType() || !voucherEntity.publishHistory().requesterId().equals(requestContext.requesterId())) {
            throw  new IllegalArgumentException("사용 불가 처리 권한이 없는 상품권 입니다.");
        }
    }
}
