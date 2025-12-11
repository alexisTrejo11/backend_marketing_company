package at.backend.MarketingCompany.customer.application.service;

import at.backend.MarketingCompany.account.user.domain.entity.valueobject.Email;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PersonName;
import at.backend.MarketingCompany.account.user.domain.entity.valueobject.PhoneNumber;
import at.backend.MarketingCompany.customer.application.dto.command.CompanyCommands.*;
import at.backend.MarketingCompany.customer.domain.valueobject.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CompanyMapper {
    public Set<ContactPerson> toContactPersons(
        Set<CreateCompanyCommand.ContactPersonCommand> commands
    ) {
        return commands.stream()
            .map(this::toContactPerson)
            .collect(Collectors.toSet());
    }

    public CompanyName toCompanyName(String name) {
        return new CompanyName(name);
    }
    
    public ContactPerson toContactPerson(
        String firstName,
        String lastName,
        String email,
        String phone,
        String position,
        String department,
        boolean isDecisionMaker
    ) {
        ContactPerson.Department dept = parseDepartment(department);
        String pos = Optional.ofNullable(position)
            .filter(p -> !p.isBlank())
            .orElse("Not specified");
        
        return new ContactPerson(
            PersonName.from(firstName, lastName),
            Optional.ofNullable(email).map(Email::new).orElse(null),
            Optional.ofNullable(phone).map(PhoneNumber::new).orElse(null),
            pos,
            dept,
            isDecisionMaker,
            false
        );
    }
    
    private ContactPerson toContactPerson(
        CreateCompanyCommand.ContactPersonCommand cmd
    ) {
        return toContactPerson(
            cmd.firstName(),
            cmd.lastName(),
            cmd.email(),
            cmd.phone(),
            cmd.position(),
            cmd.department().name(),
            cmd.isDecisionMaker()
        );
    }
    
    public CompanyProfile toCompanyProfile(
        String industry,
        CompanySize companySize,
        Integer employeeCount,
        Integer foundingYear,
        String missionStatement,
        String targetMarket,
        Set<String> keyProducts
    ) {
        CompanySize size = Optional.ofNullable(companySize)
            .orElseGet(() -> Optional.ofNullable(employeeCount)
                .map(CompanySize::fromEmployeeCount)
                .orElse(CompanySize.UNKNOWN));
        
        return CompanyProfile.builder()
            .industry(Industry.fromCode(industry))
            .size(size)
            .foundingYear(foundingYear)
            .missionStatement(missionStatement)
            .targetMarket(targetMarket)
            .keyProducts(Optional.ofNullable(keyProducts).orElse(Set.of()))
            .build();
    }
    
    private ContactPerson.Department parseDepartment(String department) {
        return Optional.ofNullable(department)
            .map(String::toUpperCase)
            .map(dept -> {
                try {
                    return ContactPerson.Department.valueOf(dept);
                } catch (IllegalArgumentException e) {
                    return ContactPerson.Department.OTHER;
                }
            })
            .orElse(ContactPerson.Department.OTHER);
    }
}


