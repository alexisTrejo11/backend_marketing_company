package at.backend.MarketingCompany.marketing.channel.core.port.output;

import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.channel.core.domain.entity.MarketingChannel;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.ChannelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ChannelRepositoryPort {
    
    MarketingChannel save(MarketingChannel channel);
    
    Optional<MarketingChannel> findById(MarketingChannelId id);
    
    void delete(MarketingChannelId id);
    
    Page<MarketingChannel> findByActiveStatus(Boolean isActive, Pageable pageable);
    
    Page<MarketingChannel> findByChannelType(
            ChannelType channelType,
            Pageable pageable);
    
    Page<MarketingChannel> searchByName(String searchTerm, Pageable pageable);
    
    Optional<MarketingChannel> findByName(String name);
    
    long countActiveChannels();
    
    boolean existsByNameAndNotDeleted(String name);
}