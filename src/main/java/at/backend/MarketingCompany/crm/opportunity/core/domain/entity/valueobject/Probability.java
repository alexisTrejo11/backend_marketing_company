package at.backend.MarketingCompany.crm.opportunity.core.domain.entity.valueobject;

public record Probability(int value) {

  public static final Probability WON = new Probability(100);
  public static final Probability LOST = new Probability(0);
  public static final Probability MIN = new Probability(0);
  public static final Probability MAX = new Probability(100);

  public Probability {
    validate(value);
  }

  private static void validate(int value) {
    if (value < 0 || value > 100) {
      throw new IllegalArgumentException("Probability must be between 0 and 100");
    }
  }

  public static Probability fromStage(OpportunityStage stage) {
    return switch (stage) {
      case PROSPECTING -> new Probability(10);
      case QUALIFICATION -> new Probability(30);
      case PROPOSAL -> new Probability(60);
      case NEGOTIATION -> new Probability(80);
      case CLOSED_WON -> WON;
      case CLOSED_LOST -> LOST;
    };
  }

  public static Probability of(int value) {
    return new Probability(value);
  }

  public boolean isValidForStage(OpportunityStage stage) {
    if (stage == null)
      return false;

    return switch (stage) {
      case PROSPECTING -> value >= 0 && value <= 25;
      case QUALIFICATION -> value >= 20 && value <= 50;
      case PROPOSAL -> value >= 40 && value <= 75;
      case NEGOTIATION -> value >= 60 && value <= 90;
      case CLOSED_WON -> value == 100;
      case CLOSED_LOST -> value == 0;
    };
  }

  public boolean isWon() {
    return value == 100;
  }

  public boolean isLost() {
    return value == 0;
  }

  public boolean isActive() {
    return value > 0 && value < 100;
  }

  public Probability increaseBy(int percentage) {
    int newValue = Math.min(100, this.value + percentage);
    return new Probability(newValue);
  }

  public Probability decreaseBy(int percentage) {
    int newValue = Math.max(0, this.value - percentage);
    return new Probability(newValue);
  }

  @Override
  public String toString() {
    return value + "%";
  }
}
