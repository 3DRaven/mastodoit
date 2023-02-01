package com.anyqn.mastodon.common.models.values;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.Objects;

public abstract class TypedValue<T extends Comparable<T>> implements Comparable<TypedValue<T>>,
        Serializable {

    @Getter
    @JsonValue
    protected final T value;

    public TypedValue(T value) {
        this.value = value;
    }

    @Override
    public int compareTo(TypedValue<T> o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (null == o) return false;
        if (this == o) return true;
        if (!(o instanceof TypedValue)) return false;
        TypedValue<?> that = (TypedValue<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}