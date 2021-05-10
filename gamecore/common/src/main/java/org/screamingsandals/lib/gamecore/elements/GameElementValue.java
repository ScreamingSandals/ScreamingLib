package org.screamingsandals.lib.gamecore.elements;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class GameElementValue<E> {
    private ValueHolder<E> obj = new DirectValueHolder<>(null);

    public GameElementValue(E firstValue) {
        this.obj = new DirectValueHolder<>(firstValue);
    }

    public E get() {
        return obj.getObj();
    }

    public Optional<E> getOptional() {
        return Optional.ofNullable(get());
    }

    public E getOrElse(E otherValue) {
        return getOptional().orElse(otherValue);
    }

    public void set(E obj) {
        this.obj = new DirectValueHolder<>(obj);
    }

    public void set(GameElementValue<E> otherElement) {
        this.obj = new RedirectedValueHolder<>(otherElement);
    }

    public boolean isPresent() {
        return get() != null;
    }

    public boolean isEmpty() {
        return get() == null;
    }

    public interface ValueHolder<E> {
        E getObj();
    }

    @Data
    public static class DirectValueHolder<E> implements ValueHolder<E> {
        private final E obj;
    }

    @Data
    public static class RedirectedValueHolder<E> implements ValueHolder<E> {
        private final GameElementValue<E> otherElement;

        @Override
        public E getObj() {
            return otherElement.get();
        }
    }
}
