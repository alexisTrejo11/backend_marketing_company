package at.backend.MarketingCompany.marketing.channel.adapter.output.persitence.repository;

import at.backend.MarketingCompany.marketing.channel.adapter.output.persitence.model.MarketingChannelEntity;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.ChannelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarketingChannelJpaRepository extends JpaRepository<MarketingChannelEntity, Long> {
    
    @Query("SELECT c FROM MarketingChannelEntity c WHERE c.deletedAt IS NULL AND c.id = :id")
    Optional<MarketingChannelEntity> findByIdAndNotDeleted(@Param("id") Long id);
    
    @Query("SELECT c FROM MarketingChannelEntity c WHERE c.deletedAt IS NULL AND c.isActive = :isActive")
    Page<MarketingChannelEntity> findByActiveStatus(
            @Param("isActive") Boolean isActive,
            Pageable pageable);
    
    @Query("SELECT c FROM MarketingChannelEntity c WHERE c.deletedAt IS NULL " +
           "AND c.channelType = :channelType")
    Page<MarketingChannelEntity> findByChannelType(
            @Param("channelType") ChannelType channelType,
            Pageable pageable);
    
    @Query("SELECT c FROM MarketingChannelEntity c WHERE c.deletedAt IS NULL " +
           "AND LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<MarketingChannelEntity> searchByName(
            @Param("searchTerm") String searchTerm,
            Pageable pageable);
    
    @Query("SELECT c FROM MarketingChannelEntity c WHERE c.deletedAt IS NULL " +
           "AND c.name = :name")
    Optional<MarketingChannelEntity> findByName(
            @Param("name") String name);
    
    @Query("SELECT COUNT(c) FROM MarketingChannelEntity c WHERE c.deletedAt IS NULL AND c.isActive = true")
    long countActiveChannels();
}