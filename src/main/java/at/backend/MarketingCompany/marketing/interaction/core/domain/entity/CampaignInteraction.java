package at.backend.MarketingCompany.marketing.interaction.core.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import at.backend.MarketingCompany.crm.deal.core.domain.entity.valueobject.DealId;
import at.backend.MarketingCompany.customer.core.domain.valueobject.CustomerCompanyId;
import at.backend.MarketingCompany.marketing.campaign.core.domain.valueobject.MarketingCampaignId;
import at.backend.MarketingCompany.marketing.channel.core.domain.valueobject.MarketingChannelId;
import at.backend.MarketingCompany.marketing.interaction.core.domain.exception.ConversionException;
import at.backend.MarketingCompany.marketing.interaction.core.domain.exception.InteractionValidationException;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.CampaignInteractionId;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.DeviceInfo;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.LocationInfo;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.MarketingInteractionType;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.PageInfo;
import at.backend.MarketingCompany.marketing.interaction.core.domain.valueobject.UTMParameters;
import at.backend.MarketingCompany.shared.domain.BaseDomainEntity;
import lombok.Getter;

@Getter
public class CampaignInteraction extends BaseDomainEntity<CampaignInteractionId> {

  private MarketingCampaignId campaignId;
  private CustomerCompanyId customerId;
  private MarketingInteractionType marketingInteractionType;
  private LocalDateTime interactionDate;
  private MarketingChannelId channelId;
  private UTMParameters utmParameters;
  private DeviceInfo deviceInfo;
  private LocationInfo locationInfo;
  private PageInfo pageInfo;
  private DealId dealId;
  private String sessionId;
  private JsonNode properties;

  private BigDecimal conversionValue;
  private LocalDateTime conversionDate;
  private String conversionNotes;
  private boolean isConversion;

  private static final BigDecimal MIN_CONVERSION_VALUE = new BigDecimal("0.01");
  private static final BigDecimal MAX_CONVERSION_VALUE = new BigDecimal("999999999.99");
  private static final int MAX_PROPERTIES = 50;
  private static final int MAX_PROPERTY_KEY_LENGTH = 100;
  private static final int MAX_PROPERTY_VALUE_LENGTH = 1000;
  private static final int MAX_CONVERSION_NOTES_LENGTH = 500;

  private CampaignInteraction() {
    this.interactionDate = LocalDateTime.now();
    this.isConversion = false;
    this.conversionValue = BigDecimal.ONE;
    this.locationInfo = new LocationInfo();
    this.pageInfo = new PageInfo();
    this.utmParameters = new UTMParameters();
    this.deviceInfo = new DeviceInfo();
    this.properties = JsonNodeFactory.instance.objectNode();
  }

  public CampaignInteraction(CampaignInteractionId id) {
    super(id);
    this.interactionDate = LocalDateTime.now();
    this.isConversion = false;
    this.conversionValue = BigDecimal.ONE;
    this.locationInfo = new LocationInfo();
    this.pageInfo = new PageInfo();
    this.utmParameters = new UTMParameters();
    this.deviceInfo = new DeviceInfo();
    this.properties = JsonNodeFactory.instance.objectNode();
  }

  public static CampaignInteraction create(CreateInteractionParams params) {
    CampaignInteraction interaction = new CampaignInteraction(CampaignInteractionId.generate());
    interaction.campaignId = params.campaignId();
    interaction.customerId = params.customerId();
    interaction.marketingInteractionType = params.marketingInteractionType();
    interaction.sessionId = sanitizeSessionId(params.sessionId());
    interaction.locationInfo = params.locationInfo() != null ? params.locationInfo() : new LocationInfo();
    interaction.pageInfo = params.pageInfo() != null ? params.pageInfo() : new PageInfo();
    interaction.utmParameters = params.utmParameters() != null ? params.utmParameters() : new UTMParameters();
    interaction.deviceInfo = params.deviceInfo() != null ? params.deviceInfo() : new DeviceInfo();

    return interaction;
  }

  private static String sanitizeSessionId(String sessionId) {
    return sessionId != null ? sessionId.trim() : null;
  }

  public static CampaignInteraction reconstruct(CampaignInteractionReconstructParams params) {
    if (params == null) {
      return null;
    }

    CampaignInteraction interaction = new CampaignInteraction();
    populateReconstructedFields(interaction, params);
    return interaction;
  }

  private static void populateReconstructedFields(CampaignInteraction interaction,
      CampaignInteractionReconstructParams params) {
    var utmParameters = UTMParameters.reconstruct(
        params.utmSource(), params.utmMedium(), params.utmCampaign(),
        params.utmContent(), params.utmTerm());
    var deviceInfo = DeviceInfo.reconstruct(params.deviceType(), params.deviceOs(), params.browser());
    var locationInfo = LocationInfo.reconstruct(params.countryCode(), params.city());
    var pageInfo = PageInfo.reconstruct(
        params.landingPageUrl(), params.referrerUrl());

    interaction.id = params.id();
    interaction.campaignId = params.campaignId();
    interaction.customerId = params.customerId();
    interaction.marketingInteractionType = params.marketingInteractionType();
    interaction.interactionDate = params.interactionDate() != null ? params.interactionDate() : LocalDateTime.now();
    interaction.channelId = params.channelId();
    interaction.utmParameters = utmParameters;
    interaction.deviceInfo = deviceInfo;
    interaction.locationInfo = locationInfo;
    interaction.pageInfo = pageInfo;
    interaction.dealId = params.dealId();
    interaction.conversionValue = params.conversionValue() != null ? params.conversionValue() : BigDecimal.ZERO;
    interaction.isConversion = Boolean.TRUE.equals(params.isConversion());
    interaction.sessionId = params.sessionId();
    interaction.properties = params.properties() != null ? params.properties() : JsonNodeFactory.instance.objectNode();
    interaction.conversionDate = params.conversionDate();
    interaction.conversionNotes = params.conversionNotes();
    interaction.createdAt = params.createdAt();
    interaction.updatedAt = params.updatedAt();
    interaction.deletedAt = params.deletedAt();
    interaction.version = params.version();
  }

  public void updateChannel(MarketingChannelId channelId) {
    this.channelId = channelId;
    this.updatedAt = LocalDateTime.now();
  }

  public void updateUTMParameters(String source, String medium, String campaign,
      String content, String term) {
    this.utmParameters = UTMParameters.create(source, medium, campaign, content, term);
    this.updatedAt = LocalDateTime.now();
  }

  public void updateDeviceInfo(String type, String os, String browser) {
    this.deviceInfo = DeviceInfo.create(type, os, browser);
    this.updatedAt = LocalDateTime.now();
  }

  public void updateLocationInfo(String countryCode, String city) {
    this.locationInfo = LocationInfo.create(countryCode, city);
    this.updatedAt = LocalDateTime.now();
  }

  public void updatePageInfo(String landingPageUrl, String referrerUrl) {
    this.pageInfo = PageInfo.create(landingPageUrl, referrerUrl);
    this.updatedAt = LocalDateTime.now();
  }

  public void addProperty(String key, Object value) {
    validateProperty(key, value);

    if (properties.size() >= MAX_PROPERTIES) {
      throw new InteractionValidationException(
          String.format("Cannot exceed %d properties", MAX_PROPERTIES));
    }

    ObjectNode objectNode = (ObjectNode) properties;
    String trimmedKey = key.trim();

    if (value == null) {
      objectNode.putNull(trimmedKey);
    } else if (value instanceof String string) {
      objectNode.put(trimmedKey, string);
    } else if (value instanceof Integer integer) {
      objectNode.put(trimmedKey, integer);
    } else if (value instanceof Long aLong) {
      objectNode.put(trimmedKey, aLong);
    } else if (value instanceof Double aDouble) {
      objectNode.put(trimmedKey, aDouble);
    } else if (value instanceof Float aFloat) {
      objectNode.put(trimmedKey, aFloat);
    } else if (value instanceof Boolean aBoolean) {
      objectNode.put(trimmedKey, aBoolean);
    } else if (value instanceof BigDecimal aBigDecimal) {
      objectNode.put(trimmedKey, aBigDecimal);
    } else {
      objectNode.put(trimmedKey, value.toString());
    }

    this.updatedAt = LocalDateTime.now();
  }

  public void removeProperty(String key) {
    if (properties.has(key)) {
      ((ObjectNode) properties).remove(key);
      this.updatedAt = LocalDateTime.now();
    }
  }

  public void updateProperty(String key, Object value) {
    if (!properties.has(key)) {
      throw new InteractionValidationException(
          String.format("Property '%s' does not exist", key));
    }

    validateProperty(key, value);

    ObjectNode objectNode = (ObjectNode) properties;
    if (value == null) {
      objectNode.putNull(key);
    } else if (value instanceof String string) {
      objectNode.put(key, string);
    } else if (value instanceof Integer integer) {
      objectNode.put(key, integer);
    } else if (value instanceof Long aLong) {
      objectNode.put(key, aLong);
    } else if (value instanceof Double aDouble) {
      objectNode.put(key, aDouble);
    } else if (value instanceof Float aFloat) {
      objectNode.put(key, aFloat);
    } else if (value instanceof Boolean aBoolean) {
      objectNode.put(key, aBoolean);
    } else if (value instanceof BigDecimal aBigDecimal) {
      objectNode.put(key, aBigDecimal);
    } else {
      objectNode.put(key, value.toString());
    }

    this.updatedAt = LocalDateTime.now();
  }

  private void validateProperty(String key, Object value) {
    if (key == null || key.isBlank()) {
      throw new InteractionValidationException("Property key cannot be empty");
    }

    String trimmedKey = key.trim();
    if (trimmedKey.length() > MAX_PROPERTY_KEY_LENGTH) {
      throw new InteractionValidationException(
          String.format("Property key cannot exceed %d characters", MAX_PROPERTY_KEY_LENGTH));
    }

    if (!trimmedKey.matches("^[a-zA-Z0-9_\\-]+$")) {
      throw new InteractionValidationException(
          "Property key can only contain letters, numbers, underscores and hyphens");
    }

    if (value != null) {
      String valueStr = value.toString();
      if (valueStr.length() > MAX_PROPERTY_VALUE_LENGTH) {
        throw new InteractionValidationException(
            String.format("Property value cannot exceed %d characters", MAX_PROPERTY_VALUE_LENGTH));
      }
    }
  }

  public void markAsConversion(DealId dealId, BigDecimal conversionValue, String notes) {
    validateConversion(dealId, conversionValue);

    if (isConversion) {
      throw new ConversionException("Interaction is already marked as conversion");
    }

    validateConversionTiming();

    this.isConversion = true;
    this.dealId = dealId;
    this.conversionValue = conversionValue.setScale(2, java.math.RoundingMode.HALF_UP);
    this.conversionDate = LocalDateTime.now();
    this.conversionNotes = sanitizeConversionNotes(notes);
    this.updatedAt = LocalDateTime.now();

    // Automáticamente agregar propiedad de conversión
    addProperty("conversion_timestamp", this.conversionDate.toString());
    if (conversionNotes != null) {
      addProperty("conversion_notes", conversionNotes);
    }
  }

  private void validateConversion(DealId dealId, BigDecimal conversionValue) {
    if (dealId == null) {
      throw new ConversionException("Deal ID is required for conversion");
    }

    if (conversionValue == null) {
      throw new ConversionException("Conversion value is required");
    }

    if (conversionValue.compareTo(MIN_CONVERSION_VALUE) < 0) {
      throw new ConversionException(
          String.format("Conversion value must be at least %s", MIN_CONVERSION_VALUE));
    }

    if (conversionValue.compareTo(MAX_CONVERSION_VALUE) > 0) {
      throw new ConversionException(
          String.format("Conversion value cannot exceed %s", MAX_CONVERSION_VALUE));
    }
  }

  private void validateConversionTiming() {
    long hoursSinceInteraction = ChronoUnit.HOURS.between(interactionDate, LocalDateTime.now());

    if (hoursSinceInteraction > 720) { // 30 días
      throw new ConversionException(
          "Cannot mark as conversion more than 30 days after interaction");
    }

    if (hoursSinceInteraction < 0) {
      throw new ConversionException(
          "Interaction date is in the future");
    }
  }

  public void revertConversion() {
    if (!isConversion) {
      throw new ConversionException("Interaction is not marked as conversion");
    }

    // Validate that the conversion is being reverted within 24 hours
    long hoursSinceConversion = ChronoUnit.HOURS.between(conversionDate, LocalDateTime.now());
    if (hoursSinceConversion > 24) {
      throw new ConversionException(
          "Cannot revert conversion after 24 hours");
    }

    this.isConversion = false;
    this.dealId = null;
    this.conversionValue = BigDecimal.ONE;
    this.conversionDate = null;
    this.conversionNotes = null;
    this.updatedAt = LocalDateTime.now();

    // Remover propiedades de conversión
    ObjectNode objectNode = (ObjectNode) properties;
    objectNode.remove("conversion_timestamp");
    objectNode.remove("conversion_notes");
  }

  public void updateConversionValue(BigDecimal newValue, String reason) {
    if (!isConversion) {
      throw new ConversionException("Cannot update value on non-conversion interaction");
    }

    validateConversionValue(newValue);

    if (reason == null || reason.trim().isEmpty()) {
      throw new ConversionException("Reason is required for updating conversion value");
    }

    BigDecimal oldValue = this.conversionValue;
    BigDecimal changePercentage = calculateChangePercentage(oldValue, newValue);

    if (changePercentage.abs().compareTo(new BigDecimal("50")) > 0
        && reason.trim().length() < 20) {
      throw new ConversionException(
          "Detailed justification (min 20 chars) required for significant value changes (>50%)");
    }

    this.conversionValue = newValue.setScale(2, java.math.RoundingMode.HALF_UP);
    this.updatedAt = LocalDateTime.now();
    addProperty("value_update_reason", reason);
    addProperty("value_updated_at", LocalDateTime.now().toString());
  }

  private void validateConversionValue(BigDecimal value) {
    if (value == null) {
      throw new ConversionException("Conversion value cannot be null");
    }

    if (value.compareTo(MIN_CONVERSION_VALUE) < 0) {
      throw new ConversionException(
          String.format("Conversion value must be at least %s", MIN_CONVERSION_VALUE));
    }

    if (value.compareTo(MAX_CONVERSION_VALUE) > 0) {
      throw new ConversionException(
          String.format("Conversion value cannot exceed %s", MAX_CONVERSION_VALUE));
    }
  }

  private BigDecimal calculateChangePercentage(BigDecimal oldValue, BigDecimal newValue) {
    if (oldValue.compareTo(BigDecimal.ZERO) == 0) {
      return newValue.compareTo(BigDecimal.ZERO) > 0
          ? new BigDecimal("100")
          : BigDecimal.ZERO;
    }
    return newValue.subtract(oldValue)
        .divide(oldValue, 4, java.math.RoundingMode.HALF_UP)
        .multiply(new BigDecimal("100"));
  }

  private String sanitizeConversionNotes(String notes) {
    if (notes == null || notes.isBlank()) {
      return null;
    }

    String trimmed = notes.trim();
    if (trimmed.length() > MAX_CONVERSION_NOTES_LENGTH) {
      throw new ConversionException(
          String.format("Conversion notes cannot exceed %d characters", MAX_CONVERSION_NOTES_LENGTH));
    }

    return trimmed;
  }

  public boolean isValidForConversion() {
    if (isConversion) {
      return false;
    }

    long hoursSinceInteraction = ChronoUnit.HOURS.between(interactionDate, LocalDateTime.now());
    return hoursSinceInteraction <= 720 && hoursSinceInteraction >= 0; // 30 días máximo
  }

  public void validateInteraction() {
    if (interactionDate.isAfter(LocalDateTime.now())) {
      throw new InteractionValidationException("Interaction date cannot be in the future");
    }

    if (isConversion) {
      if (dealId == null) {
        throw new InteractionValidationException("Conversion must have a deal ID");
      }
      if (conversionValue == null || conversionValue.compareTo(BigDecimal.ZERO) <= 0) {
        throw new InteractionValidationException("Conversion must have a positive value");
      }
      if (conversionDate == null) {
        throw new InteractionValidationException("Conversion must have a conversion date");
      }
      if (conversionDate.isBefore(interactionDate)) {
        throw new InteractionValidationException("Conversion date cannot be before interaction date");
      }
    }

    if (sessionId == null || sessionId.isBlank()) {
      throw new InteractionValidationException("Session ID is required");
    }
  }
}
