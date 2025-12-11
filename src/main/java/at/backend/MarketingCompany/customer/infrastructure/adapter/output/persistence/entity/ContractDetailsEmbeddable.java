package at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class ContractDetailsEmbeddable {
    @Column(name = "contract_id")
    private String contractId;
    
    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;
    
    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;
    
    @Column(name = "monthly_fee", precision = 10, scale = 2)
    private BigDecimal monthlyFee;
    
    @Column(name = "contract_type")
    private String contractType;
    
    @Column(name = "auto_renewal")
    private Boolean autoRenewal;
    
    @Column(name = "is_active")
    private Boolean isActive;
}