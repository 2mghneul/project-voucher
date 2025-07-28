package com.fastcampus.projectvoucher.app.domain.voucher;

import com.fastcampus.projectvoucher.app.common.type.VoucherAmountType;
import com.fastcampus.projectvoucher.app.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.app.storagy.voucher.VoucherEntity;
import com.fastcampus.projectvoucher.app.storagy.voucher.VoucherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("")
@SpringBootTest
class VoucherServiceTest {
    @Autowired private VoucherService voucherService;
    @Autowired private VoucherRepository voucherRepository;

    @DisplayName("발행된 상품권은 코드로 조회 할 수 있어야 한다.")
    @Test
    void test1() {
        // given
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = LocalDate.now().plusDays(30);
        VoucherAmountType amount = VoucherAmountType.KRW_30000;
        
        String code = voucherService.publish(validFrom, validTo, amount);

        // when
        VoucherEntity entity = voucherRepository.findByCode(code).get();

        // then
        assertThat(entity.code()).isEqualTo(code);
        assertThat(entity.status()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(entity.validFrom()).isEqualTo(validFrom);
        assertThat(entity.validTo()).isEqualTo(validTo);
        assertThat(entity.amount()).isEqualTo(amount);
    }

    @DisplayName("발행된 상품권은 사용불가 처리 할 수 있어야 한다.")
    @Test
    void test2() {
        // given
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = LocalDate.now().plusDays(30);
        VoucherAmountType amount = VoucherAmountType.KRW_30000;

        String code = voucherService.publish(validFrom, validTo, amount);

        // when
        voucherService.disable(code);
        VoucherEntity entity = voucherRepository.findByCode(code).get();

        // then
        assertThat(entity.code()).isEqualTo(code);
        assertThat(entity.status()).isEqualTo(VoucherStatusType.DISABLE);
        assertThat(entity.validFrom()).isEqualTo(validFrom);
        assertThat(entity.validTo()).isEqualTo(validTo);
        assertThat(entity.amount()).isEqualTo(amount);
        assertThat(entity.createdAt()).isNotEqualTo(entity.updateAt());
    }

    @DisplayName("발행된 상품권은 사용 할 수 있다.")
    @Test
    void test3() {
        // given
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = LocalDate.now().plusDays(30);
        VoucherAmountType amount = VoucherAmountType.KRW_30000;

        String code = voucherService.publish(validFrom, validTo, amount);

        // when
        voucherService.use(code);
        VoucherEntity entity = voucherRepository.findByCode(code).get();

        // then
        assertThat(entity.code()).isEqualTo(code);
        assertThat(entity.status()).isEqualTo(VoucherStatusType.USE);
        assertThat(entity.validFrom()).isEqualTo(validFrom);
        assertThat(entity.validTo()).isEqualTo(validTo);
        assertThat(entity.amount()).isEqualTo(amount);
        assertThat(entity.createdAt()).isNotEqualTo(entity.updateAt());
    }
}
