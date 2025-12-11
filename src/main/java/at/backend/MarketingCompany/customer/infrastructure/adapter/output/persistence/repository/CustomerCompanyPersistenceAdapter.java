package at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.repository;


import at.backend.MarketingCompany.customer.application.port.ouput.CustomerCompanyRepositoryPort;
import at.backend.MarketingCompany.customer.domain.entity.CustomerCompany;
import at.backend.MarketingCompany.customer.domain.valueobject.*;
import at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.entity.CustomerCompanyEntity;
import at.backend.MarketingCompany.customer.infrastructure.adapter.output.persistence.mapper.CustomerCompanyEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class CustomerCompanyPersistenceAdapter implements CustomerCompanyRepositoryPort {
    private final CustomerCompanyJpaRepository customerCompanyJpaRepository;
    private final CustomerCompanyEntityMapper customerCompanyMapper;

    @Override
    @Transactional
    public CustomerCompany save(CustomerCompany customerCompany) {
        CustomerCompanyEntity entity = customerCompanyMapper.toEntity(customerCompany);
        CustomerCompanyEntity savedEntity = customerCompanyJpaRepository.save(entity);
        return customerCompanyMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerCompany> findById(CustomerCompanyId id) {
        return customerCompanyJpaRepository.findById(id.value())
                .map(customerCompanyMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerCompany> findAll(Pageable pageable) {
        return customerCompanyJpaRepository.findAll(pageable)
                .map(customerCompanyMapper::toDomain);
    }

    @Override
    @Transactional
    public void delete(CustomerCompanyId id) {
        customerCompanyJpaRepository.deleteById(id.value());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(CustomerCompanyId id) {
        return customerCompanyJpaRepository.existsById(id.value());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerCompany> findByIndustry(String industryCode) {
        return customerCompanyJpaRepository.findByIndustryCode(industryCode).stream()
                .map(customerCompanyMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerCompany> findByCompanyNameContaining(String name) {
        return customerCompanyJpaRepository.findByCompanyNameContainingIgnoreCase(name).stream()
                .map(customerCompanyMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerCompany> findByStatus(CompanyStatus status) {
        return customerCompanyJpaRepository.findByStatus(status).stream()
                .map(customerCompanyMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerCompany> findByCompanySize(CompanySize size, Pageable pageable) {
        return customerCompanyJpaRepository.findByCompanySize(size, pageable)
                .map(customerCompanyMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerCompany> findHighValueClients() {
        BigDecimal minRevenue = new BigDecimal("10000000"); // 10M+
        return customerCompanyJpaRepository.findHighValueCompanies(minRevenue).stream()
                .map(customerCompanyMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerCompany> findByTaxId(String taxId) {
        return customerCompanyJpaRepository.findByTaxId(taxId)
                .map(customerCompanyMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerCompany> findCompaniesWithExpiringContracts() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);
        return customerCompanyJpaRepository.findCompaniesWithExpiringContracts(startDate, endDate).stream()
                .map(customerCompanyMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerCompany> findRecentStartups(int startYear) {
        return customerCompanyJpaRepository.findRecentStartups(startYear).stream()
                .map(customerCompanyMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CustomerCompany> searchCompanies(String searchTerm, Pageable pageable) {
        return customerCompanyJpaRepository.searchCompanies(searchTerm, pageable)
                .map(customerCompanyMapper::toDomain);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByTaxId(String taxId) {
        return customerCompanyJpaRepository.existsByTaxId(taxId);
    }
}