package com.fastcampus.projectvoucher.app.domain.voucher;

import com.fastcampus.projectvoucher.app.common.dto.RequestContext;
import com.fastcampus.projectvoucher.app.common.type.VoucherAmountType;
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
import static org.mockito.ArgumentMatchers.any;

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
                    RequestContext requestContext = new RequestContext(any(), any());
                    LocalDate validFrom = LocalDate.now();
                    LocalDate validTo = LocalDate.now().plusDays(30);
                    VoucherAmountType amount = VoucherAmountType.KRW_30000;

                    // when
                    String code = voucherService.publish(requestContext, validFrom, validTo, amount);;
                    codes.add(code);

                    // then
                    VoucherEntity entity = voucherRepository.findByCode(code).get();
                    assertThat(entity.status()).isEqualTo(VoucherStatusType.PUBLISH);
                }),
                dynamicTest("[1] 상품권을 사용불가 처리합니다.", () -> {
                    // given
                    RequestContext requestContext = new RequestContext(any(), any());
                    String code = codes.get(0);

                    // when
                    voucherService.disable(requestContext, code);

                    // then
                    VoucherEntity entity = voucherRepository.findByCode(code).get();
                    assertThat(entity.status()).isEqualTo(VoucherStatusType.DISABLE);
                }),
                dynamicTest("[2] 사용불가 상태의 상품권은 사용할 수 업습니다.", () -> {
                    // given
                    RequestContext requestContext = new RequestContext(any(), any());

                    String code = codes.get(0);

                    // when
                    assertThatThrownBy(() -> voucherService.use(requestContext, code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용할 수 없는 상태의 상품권 입니다.");

                    // then
                    VoucherEntity entity = voucherRepository.findByCode(code).get();
                    assertThat(entity.status()).isEqualTo(VoucherStatusType.DISABLE);
                }),
                dynamicTest("[3] 싱품권을 사용합니다.", () -> {
                    // given
                    RequestContext requestContext = new RequestContext(any(), any());
                    LocalDate validFrom = LocalDate.now();
                    LocalDate validTo = LocalDate.now().plusDays(30);
                    VoucherAmountType amount = VoucherAmountType.KRW_30000;
                    String code = voucherService.publish(requestContext, validFrom, validTo, amount);;
                    codes.add(code);

                    // when
                    voucherService.use(requestContext, code);

                    // then
                    VoucherEntity entity = voucherRepository.findByCode(code).get();
                    assertThat(entity.status()).isEqualTo(VoucherStatusType.USE);
                }),
                dynamicTest("[5] 사용한 상품권은 사용 불가 처리할 수 없습니다.", () -> {
                    // given
                    RequestContext requestContext = new RequestContext(any(), any());
                    String code = codes.get(1);

                    // when
                    assertThatThrownBy(() -> voucherService.disable(requestContext, code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용 뷸가 처리할 수 없는 상태의 상품권 입니다.");

                    // then
                    VoucherEntity entity = voucherRepository.findByCode(code).get();
                    assertThat(entity.status()).isEqualTo(VoucherStatusType.USE);
                }),
                dynamicTest("[6] 사용한 상품권은 또 사용할 수 없습니다.", () -> {
                    // given
                    RequestContext requestContext = new RequestContext(any(), any());
                    String code = codes.get(1);

                    // when
                    assertThatThrownBy(() -> voucherService.use(requestContext, code))
                            .isInstanceOf(IllegalStateException.class)
                            .hasMessage("사용할 수 없는 상태의 상품권 입니다.");

                    // then
                    VoucherEntity entity = voucherRepository.findByCode(code).get();
                    assertThat(entity.status()).isEqualTo(VoucherStatusType.USE);
                })
        );
    }
}
