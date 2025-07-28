package com.fastcampus.projectvoucher.app.domain.voucher;

import com.fastcampus.projectvoucher.app.common.type.VoucherStatusType;
import com.fastcampus.projectvoucher.app.storagy.voucher.VoucherEntity;
import com.fastcampus.projectvoucher.app.storagy.voucher.VoucherRepository;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest
public class VoucherServiceDynamicTest {
    @Autowired
    private VoucherService voucherService;
    @Autowired private VoucherRepository voucherRepository;

    @TestFactory
    Stream<DynamicTest> test() {
        List<String> codes = new ArrayList<>();
        return Stream.of(
                dynamicTest("[0] 상품권을 발행합니다.", () -> {
                    // given
                    LocalDate validFrom = LocalDate.now();
                    LocalDate validTo = LocalDate.now().plusDays(30);
                    Long amount = 10000L;

                    // when
                    String code = voucherService.publish(validFrom, validTo, amount);;
                    codes.add(code);

                    // then
                    VoucherEntity entity = voucherRepository.findByCode(code).get();
                    assertThat(entity.status()).isEqualTo(VoucherStatusType.PUBLISH);
                }),
                dynamicTest("[1] 상품권을 사용불가 처리합니다.", () -> {
                    // given
                    String code = codes.get(0);

                    // when
                    voucherService.disable(code);

                    // then
                    VoucherEntity entity = voucherRepository.findByCode(code).get();
                    assertThat(entity.status()).isEqualTo(VoucherStatusType.DISABLE);
                }),
                dynamicTest("[2] 사용불가 상태의 상품권은 사용할 수 업습니다.", () -> {
                    // given
                    String code = codes.get(0);

                    // when
                    assertThatThrownBy(() -> voucherService.use(code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용할 수 없는 상태의 상품권 입니다.");

                    // then
                    VoucherEntity entity = voucherRepository.findByCode(code).get();
                    assertThat(entity.status()).isEqualTo(VoucherStatusType.DISABLE);
                }),
                dynamicTest("[3] 싱품권을 사용합니다.", () -> {
                    // given
                    LocalDate validFrom = LocalDate.now();
                    LocalDate validTo = LocalDate.now().plusDays(30);
                    Long amount = 10000L;
                    String code = voucherService.publish(validFrom, validTo, amount);;
                    codes.add(code);

                    // when
                    voucherService.use(code);

                    // then
                    VoucherEntity entity = voucherRepository.findByCode(code).get();
                    assertThat(entity.status()).isEqualTo(VoucherStatusType.USE);
                }),
                dynamicTest("[5] 사용한 상품권은 사용 불가 처리할 수 없습니다.", () -> {
                    // given
                    String code = codes.get(1);

                    // when
                    assertThatThrownBy(() -> voucherService.disable(code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용 뷸가 처리할 수 없는 상태의 상품권 입니다.");

                    // then
                    VoucherEntity entity = voucherRepository.findByCode(code).get();
                    assertThat(entity.status()).isEqualTo(VoucherStatusType.USE);
                }),
                dynamicTest("[6] 사용한 상품권은 또 사용할 수 없습니다.", () -> {
                    // given
                    String code = codes.get(1);

                    // when
                    assertThatThrownBy(() -> voucherService.use(code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용할 수 없는 상태의 상품권 입니다.");

                    // then
                    VoucherEntity entity = voucherRepository.findByCode(code).get();
                    assertThat(entity.status()).isEqualTo(VoucherStatusType.USE);
                })
        );
    }
}
