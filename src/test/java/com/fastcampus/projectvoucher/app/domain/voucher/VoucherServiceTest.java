package com.fastcampus.projectvoucher.app.domain.voucher;

import com.fastcampus.projectvoucher.app.common.dto.RequestContext;
import com.fastcampus.projectvoucher.app.common.type.VoucherAmountType;
import com.fastcampus.projectvoucher.app.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.app.storagy.voucher.VoucherEntity;
import com.fastcampus.projectvoucher.app.storagy.voucher.VoucherHistoryEntity;
import com.fastcampus.projectvoucher.app.storagy.voucher.VoucherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("")
@SpringBootTest
class VoucherServiceTest {
    @Autowired private VoucherService voucherService;
    @Autowired private VoucherRepository voucherRepository;

    @DisplayName("발행된 상품권은 코드로 조회 할 수 있어야 한다.")
    @Test
    void test1() {
        // given
        RequestContext requestContext = new RequestContext(any(), any());
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = LocalDate.now().plusDays(30);
        VoucherAmountType amount = VoucherAmountType.KRW_30000;

        String code = voucherService.publish(requestContext, validFrom, validTo, amount);

        // when
        VoucherEntity entity = voucherRepository.findByCode(code).get();

        // then
        assertThat(entity.code()).isEqualTo(code);
        assertThat(entity.status()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(entity.validFrom()).isEqualTo(validFrom);
        assertThat(entity.validTo()).isEqualTo(validTo);
        assertThat(entity.amount()).isEqualTo(amount);

        // history
        VoucherHistoryEntity historyEntity = entity.histories().get(0);
        assertThat(historyEntity.orderId()).isNotNull();
        assertThat(historyEntity.requesterType()).isEqualTo(requestContext.requesterType());
        assertThat(historyEntity.requesterId()).isEqualTo(requestContext.requesterId());
        assertThat(historyEntity.status()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(historyEntity.description()).isEqualTo("테스트 발행");
    }

    @DisplayName("발행된 상품권은 사용불가 처리 할 수 있어야 한다.")
    @Test
    void test2() {
        // given
        RequestContext requestContext = new RequestContext(any(), any());
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = LocalDate.now().plusDays(30);
        VoucherAmountType amount = VoucherAmountType.KRW_30000;

        String code = voucherService.publish(requestContext, validFrom, validTo, amount);

        RequestContext disableRequestContext = new RequestContext(any(), any());

        // when
        voucherService.disable(requestContext, code);
        VoucherEntity entity = voucherRepository.findByCode(code).get();

        // then
        assertThat(entity.code()).isEqualTo(code);
        assertThat(entity.status()).isEqualTo(VoucherStatusType.DISABLE);
        assertThat(entity.validFrom()).isEqualTo(validFrom);
        assertThat(entity.validTo()).isEqualTo(validTo);
        assertThat(entity.amount()).isEqualTo(amount);
        assertThat(entity.createdAt()).isNotEqualTo(entity.updateAt());

        // history
        VoucherHistoryEntity historyEntity = entity.histories().get(entity.histories().size() - 1);
        assertThat(historyEntity.orderId()).isNotNull();
        assertThat(historyEntity.requesterType()).isEqualTo(disableRequestContext.requesterType());
        assertThat(historyEntity.requesterId()).isEqualTo(disableRequestContext.requesterId());
        assertThat(historyEntity.status()).isEqualTo(VoucherStatusType.DISABLE);
        assertThat(historyEntity.description()).isEqualTo("테스트 사용 불가");
    }

    @DisplayName("발행된 상품권은 사용 할 수 있다.")
    @Test
    void test3() {
        // given
        RequestContext requestContext = new RequestContext(any(), any());
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = LocalDate.now().plusDays(30);
        VoucherAmountType amount = VoucherAmountType.KRW_30000;

        String code = voucherService.publish(requestContext, validFrom, validTo, amount);

        RequestContext useRequestContext = new RequestContext(any(), any());

        // when
        voucherService.use(requestContext, code);
        VoucherEntity entity = voucherRepository.findByCode(code).get();

        // then
        assertThat(entity.code()).isEqualTo(code);
        assertThat(entity.status()).isEqualTo(VoucherStatusType.USE);
        assertThat(entity.validFrom()).isEqualTo(validFrom);
        assertThat(entity.validTo()).isEqualTo(validTo);
        assertThat(entity.amount()).isEqualTo(amount);
        assertThat(entity.createdAt()).isNotEqualTo(entity.updateAt());

        // history
        VoucherHistoryEntity historyEntity = entity.histories().get(entity.histories().size() - 1);
        assertThat(historyEntity.orderId()).isNotNull();
        assertThat(historyEntity.requesterType()).isEqualTo(useRequestContext.requesterType());
        assertThat(historyEntity.requesterId()).isEqualTo(useRequestContext.requesterId());
        assertThat(historyEntity.status()).isEqualTo(VoucherStatusType.USE);
        assertThat(historyEntity.description()).isEqualTo("테스트 사용");
    }
}
