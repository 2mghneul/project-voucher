package com.fastcampus.projectvoucher.app.config;

import com.fastcampus.projectvoucher.app.storagy.voucher.ContractEntity;
import com.fastcampus.projectvoucher.app.storagy.voucher.ContractRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

//@Profile("!prod")
@Configuration
public class LocalDataInit {
    private final ContractRepository contractRepository;
    public LocalDataInit(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @PostConstruct
    public void init() {
        // 계약 데이터 초기화
        contractRepository.save(new ContractEntity("CT001", LocalDate.now().minusDays(7), LocalDate.now().plusDays(7), 366 * 5));
    }
}
