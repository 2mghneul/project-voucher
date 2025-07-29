package com.fastcampus.projectvoucher.app.controller.voucher;

import com.fastcampus.projectvoucher.app.common.dto.RequestContext;
import com.fastcampus.projectvoucher.app.controller.voucher.request.VoucherDisableRequest;
import com.fastcampus.projectvoucher.app.controller.voucher.request.VoucherPublishRequest;
import com.fastcampus.projectvoucher.app.controller.voucher.request.VoucherUseRequest;
import com.fastcampus.projectvoucher.app.controller.voucher.response.VoucherDisableResponse;
import com.fastcampus.projectvoucher.app.controller.voucher.response.VoucherPublishResponse;
import com.fastcampus.projectvoucher.app.controller.voucher.response.VoucherUseResponse;
import com.fastcampus.projectvoucher.app.domain.voucher.VoucherService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class VoucherController {
    private final VoucherService voucherService;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    // 상품권 발행
    @PostMapping("api/v1/voucher")
    public VoucherPublishResponse publish(@RequestBody VoucherPublishRequest request) {
        String publishedVoucherCode = voucherService.publish(
                new RequestContext(request.requesterType(), request.requesterId()),
                request.contractCode(),
                request.amount()
                );
        return new VoucherPublishResponse("order_id", publishedVoucherCode);
    }

    // 상품권 사용 불가 처리
    @PutMapping("api/v1/voucher/disable")
    public VoucherDisableResponse disable(@RequestBody VoucherDisableRequest request) {
        voucherService.disable(
                new RequestContext(request.requesterType(), request.requesterId()),
                request.code());
        return  new VoucherDisableResponse("order_id");
    }

    // 상품권 사용
    @PutMapping("api/v1/voucher/use")
    public VoucherUseResponse use(@RequestBody VoucherUseRequest request) {
        voucherService.use(new RequestContext(request.requesterType(), request.requesterId()),
                request.code());
        return new VoucherUseResponse("order_id");
    }
}
