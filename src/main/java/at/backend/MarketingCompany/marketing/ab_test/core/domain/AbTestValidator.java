package at.backend.MarketingCompany.marketing.ab_test.core.domain;

import at.backend.MarketingCompany.marketing.ab_test.core.domain.exception.AbTestValidationException;
import at.backend.MarketingCompany.shared.exception.BusinessRuleException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class AbTestValidator {

  private static final int MIN_SAMPLE_SIZE = 100;
  private static final BigDecimal MIN_CONFIDENCE_LEVEL = new BigDecimal("0.8");
  private static final BigDecimal MAX_CONFIDENCE_LEVEL = new BigDecimal("0.999");

  public static void validateForCreation(
      String testName,
      LocalDateTime startDate,
      String controlVariant,
      Map<String, Object> treatmentVariants,
      Object campaign) {
    
    if (testName == null || testName.isBlank()) {
      throw new BusinessRuleException("Test name is required");
    }
    
    if (testName.length() > 200) {
      throw new BusinessRuleException("Test name cannot exceed 200 characters");
    }
    
    if (startDate == null) {
      throw new BusinessRuleException("Start date is required");
    }
    
    if (startDate.isBefore(LocalDateTime.now())) {
      throw new BusinessRuleException("AB Test start date cannot be in the past");
    }
    
    if (controlVariant == null || controlVariant.isBlank()) {
      throw new BusinessRuleException("Control variant is required");
    }
    
    if (treatmentVariants == null || treatmentVariants.isEmpty()) {
      throw new BusinessRuleException("At least one treatment variant is required");
    }
    
    if (campaign == null) {
      throw new BusinessRuleException("Campaign is required for AB Test");
    }
  }

  public static void validateSampleSize(Integer sampleSize) {
    if (sampleSize != null && sampleSize < MIN_SAMPLE_SIZE) {
      throw new BusinessRuleException(
          String.format("Sample size must be at least %d for valid results", MIN_SAMPLE_SIZE)
      );
    }
  }

  public static void validateConfidenceLevel(BigDecimal confidenceLevel) {
    if (confidenceLevel == null) {
      return; // Optional field
    }
    
    if (confidenceLevel.compareTo(MIN_CONFIDENCE_LEVEL) < 0) {
      throw new BusinessRuleException(
          String.format("Confidence level must be at least %.2f", MIN_CONFIDENCE_LEVEL)
      );
    }
    
    if (confidenceLevel.compareTo(MAX_CONFIDENCE_LEVEL) > 0) {
      throw new BusinessRuleException(
          String.format("Confidence level cannot exceed %.3f", MAX_CONFIDENCE_LEVEL)
      );
    }
  }

  public static void validateCompletion(
      AbTest test,
      String winningVariant,
      BigDecimal statisticalSignificance) {
    
    if (test == null) {
      throw new AbTestValidationException("Test cannot be null");
    }
    
    if (test.isCompleted()) {
      throw new AbTestValidationException("AB Test is already completed");
    }
    
    if (winningVariant == null || winningVariant.isBlank()) {
      throw new AbTestValidationException("Winning variant is required to complete test");
    }
    
    if (statisticalSignificance == null) {
      throw new AbTestValidationException("Statistical significance is required");
    }
    
    if (statisticalSignificance.compareTo(BigDecimal.ZERO) < 0 ||
        statisticalSignificance.compareTo(BigDecimal.ONE) > 0) {
      throw new AbTestValidationException("Statistical significance must be between 0 and 1");
    }
  }

  public static void validateForUpdate(AbTest test) {
    if (test == null) {
      throw new AbTestValidationException("Test cannot be null");
    }
    
    if (test.isCompleted()) {
      throw new AbTestValidationException("Cannot update a completed test");
    }
  }
}
