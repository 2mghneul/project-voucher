package com.fastcampus.projectvoucher.app.domain.voucher;

import com.fastcampus.projectvoucher.app.common.dto.RequestContext;
import com.fastcampus.projectvoucher.app.common.type.RequesterType;
import com.fastcampus.projectvoucher.app.common.type.VoucherAmountType;
import com.fastcampus.projectvoucher.app.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.app.storagy.voucher.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class VoucherServiceTest {
    @Autowired private VoucherService voucherService;
    @Autowired private VoucherRepository voucherRepository;
    @Autowired private ContractRepository contractRepository;

    @DisplayName("발행된 상품권은 코드로 조회 할 수 있어야 한다.")
    @Test
    void test1() {
        // given
        RequestContext requestContext = new RequestContext(any(), any());
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = LocalDate.now().plusDays(366 * 5);
        VoucherAmountType amount = VoucherAmountType.KRW_30000;
        String contactCode = "CT001";

        String code = voucherService.publish(requestContext, contactCode, amount);


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

    @DisplayName("발행된 상품권은 voucherValidPeriodDayCount 만큼 유효기간을 가져야 한다.")
    @Test
    void test2() {
        // given
        RequestContext requestContext = new RequestContext(any(), any());
        VoucherAmountType amount = VoucherAmountType.KRW_30000;

        String contactCode = "CT001";

        // when
        String code = voucherService.publish(requestContext, contactCode, amount);
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        // then
        ContractEntity contractEntity = contractRepository.findByCode(contactCode).get();
        assertThat(voucherEntity.code()).isEqualTo(code);
        assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherEntity.validFrom()).isEqualTo(LocalDate.now());
        assertThat(voucherEntity.validTo()).isEqualTo(LocalDate.now().plusDays(contractEntity.voucherValidPeriodDayCount()));
        assertThat(voucherEntity.amount()).isEqualTo(amount);
    }

    @DisplayName("발행된 상품권은 사용불가 처리 할 수 있어야 한다.")
    @Test
    void test3() {
        // given
        RequestContext requestContext = new RequestContext(RequesterType.PARTNER, "A00056");
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = LocalDate.now().plusDays(366 * 5);
        VoucherAmountType amount = VoucherAmountType.KRW_30000;
        String contactCode = "CT001";

        String code = voucherService.publish(requestContext, contactCode, amount);

        RequestContext disableRequestContext = new RequestContext(RequesterType.PARTNER, "A00056");

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
    void test4() {
        // given
        RequestContext requestContext = new RequestContext(any(), any());
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = LocalDate.now().plusDays(366 * 5);
        VoucherAmountType amount = VoucherAmountType.KRW_30000;
        String contactCode = "CT001";

        String code = voucherService.publish(requestContext, contactCode, amount);

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

    @DisplayName("유효기간이 지난 계약으로는 상품권을 발행 할 수 없다.")
    @Test
    void test5() {
        // given
        RequestContext requestContext = new RequestContext(any(), any());
        VoucherAmountType amount = VoucherAmountType.KRW_30000;

        String contactCode = "CT0010";

        // when & then
        assertThatThrownBy(() -> voucherService.publish(requestContext, contactCode, amount))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("유효기간이 지난 계약입니다.");
    }

    @DisplayName("상품권은 발행 요청자만 사용 불가 처리할 수 있다.")
    @Test
    void test6() {
        // given
        RequestContext requestContext = new RequestContext(any(), any());
        VoucherAmountType amount = VoucherAmountType.KRW_30000;

        String contactCode = "CT001";
        String code = voucherService.publish(requestContext, contactCode, amount);

        RequestContext otherRequestContext = new RequestContext(RequesterType.USER, UUID.randomUUID().toString());

        // when
        assertThatThrownBy(() -> voucherService.disable(otherRequestContext, code))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용 불가 처리 권한이 없는 상품권 입니다.");

        // then
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        assertThat(voucherEntity.code()).isEqualTo(code);
        assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.PUBLISH);
    }
}
