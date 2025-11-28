package at.backend.MarketingCompany.crm.interaction.api.repository;

import at.backend.MarketingCompany.crm.interaction.infrastructure.persistence.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {

}