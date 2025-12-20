package at.backend.MarketingCompany.marketing.channel.adapter.output.persitence.repository;


import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.channel.adapter.output.persitence.mapper.ChannelEntityMapper;
import at.backend.MarketingCompany.marketing.channel.adapter.output.persitence.model.MarketingChannelEntity;
import at.backend.MarketingCompany.marketing.channel.core.domain.entity.MarketingChannel;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.ChannelType;
import at.backend.MarketingCompany.marketing.channel.core.port.output.ChannelRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChannelRepositoryAdapter implements ChannelRepositoryPort {
    private final MarketingChannelJpaRepository jpaRepository;
    private final ChannelEntityMapper mapper;

    @Override
    @Transactional
    public MarketingChannel save(MarketingChannel channel) {
        MarketingChannelEntity entity = mapper.toEntity(channel);
        MarketingChannelEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MarketingChannel> findById(MarketingChannelId id) {
        return jpaRepository.findByIdAndNotDeleted(id.getValue())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void delete(MarketingChannelId id) {
        MarketingChannelEntity entity = jpaRepository.findByIdAndNotDeleted(id.getValue())
                .orElseThrow(() -> new IllegalArgumentException("Channel not found with id: " + id));
        entity.setDeletedAt(java.time.LocalDateTime.now());
        jpaRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarketingChannel> findByActiveStatus(Boolean isActive, Pageable pageable) {
        return jpaRepository.findByActiveStatus(isActive, pageable)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarketingChannel> findByChannelType(
            ChannelType channelType,
            Pageable pageable) {
        return jpaRepository.findByChannelType(channelType, pageable)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarketingChannel> searchByName(String searchTerm, Pageable pageable) {
        return jpaRepository.searchByName(searchTerm, pageable)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MarketingChannel> findByName(String name) {
        return jpaRepository.findByName(name)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveChannels() {
        return jpaRepository.countActiveChannels();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndNotDeleted(String name) {
        return jpaRepository.findByName(name).isPresent();
    }
}