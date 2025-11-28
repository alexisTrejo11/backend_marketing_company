package at.backend.MarketingCompany.crm.tasks.infrastructure.autoMappers;

import at.backend.MarketingCompany.crm.tasks.infrastructure.persistence.TaskEntity;
import at.backend.MarketingCompany.crm.tasks.infrastructure.DTOs.TaskInput;
import javax.annotation.processing.Generated;

import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-27T17:36:05-0600",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 23.0.2 (Homebrew)"
)
@Component
public class TaskMappersImpl implements TaskMappers {

    @Override
    public TaskEntity inputToEntity(TaskInput input) {
        if ( input == null ) {
            return null;
        }

        TaskEntity taskEntity = new TaskEntity();

        taskEntity.setTitle( input.title() );
        taskEntity.setDescription( input.description() );
        taskEntity.setDueDate( input.dueDate() );
        taskEntity.setStatus( input.status() );
        taskEntity.setPriority( input.priority() );

        return taskEntity;
    }

    @Override
    public TaskEntity inputToUpdatedEntity(TaskEntity existingTaskEntity, TaskInput input) {
        if ( input == null ) {
            return existingTaskEntity;
        }

        existingTaskEntity.setTitle( input.title() );
        existingTaskEntity.setDescription( input.description() );
        existingTaskEntity.setDueDate( input.dueDate() );
        existingTaskEntity.setStatus( input.status() );
        existingTaskEntity.setPriority( input.priority() );

        return existingTaskEntity;
    }
}
