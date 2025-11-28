package at.backend.MarketingCompany.crm.tasks.api.repository;

import at.backend.MarketingCompany.crm.tasks.infrastructure.persistence.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

}