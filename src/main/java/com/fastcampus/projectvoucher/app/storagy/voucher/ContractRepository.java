package com.fastcampus.projectvoucher.app.storagy.voucher;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<ContractEntity, Long> {
    Optional<ContractEntity> findByCode(String code);
}
