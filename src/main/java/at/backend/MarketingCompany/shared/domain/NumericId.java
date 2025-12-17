package at.backend.MarketingCompany.shared.domain;

public abstract class NumericId extends BaseId<Long> {
    protected NumericId(Long value) {
        super(value);
    }

    @Override
    protected Long validate(Long value) {
        if (value <= 0) {
            throw new IllegalArgumentException(getClass().getSimpleName() + " must be positive");
        }
        return value;
    }
    
    public static <T extends NumericId> T fromString(String id, java.util.function.Function<Long, T> constructor) {
        try {
            Long longId = Long.parseLong(id);
            return constructor.apply(longId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID format: " + id);
        }
    }
}