package at.backend.MarketingCompany.shared.domain;

import java.io.Serializable;

public abstract class BaseId<T> implements Serializable {
    private final T value;
    
    protected BaseId(T value) {
        this.value = validate(value);
    }

    protected abstract T validate(T value);

    public T getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseId<?> baseId = (BaseId<?>) o;
        return value.equals(baseId.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + value + "]";
    }

    public String asString() {
        return value.toString();
    }
}