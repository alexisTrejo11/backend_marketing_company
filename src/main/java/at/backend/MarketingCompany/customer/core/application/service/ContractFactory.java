package at.backend.MarketingCompany.customer.core.application.service;

import at.backend.MarketingCompany.customer.core.domain.valueobject.ContractDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Service
public class ContractFactory {

    private static final int DEFAULT_DURATION_MONTHS = 12;

    public ContractDetails createContract(
            String contractId,
            ContractDetails.ContractType contractType,
            BigDecimal monthlyFee,
            Integer durationMonths,
            boolean autoRenewal
    ) {

        int duration = Optional.ofNullable(durationMonths)
                .filter(d -> d > 0)
                .orElseGet(() -> Optional.of(contractType.getDefaultDurationMonths())
                        .filter(d -> d > 0)
                        .orElse(DEFAULT_DURATION_MONTHS));

        return ContractDetails.of(
                contractId,
                LocalDate.now(),
                LocalDate.now().plusMonths(duration),
                monthlyFee,
                contractType,
                Set.of(),
                autoRenewal
        );
    }
}
