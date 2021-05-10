package org.screamingsandals.lib.gamecore.elements;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
public class GameElementManager<E extends GameElement> {
    private final List<E> elements = new LinkedList<>();

    public Optional<E> getElement(UUID uuid) {
        return elements.stream()
                .filter(e -> e.getUuid().equals(uuid))
                .findFirst();
    }

    public Optional<E> getElement(String name) {
        return elements.stream()
                .filter(e -> e.getName().map(s -> s.equals(name)).orElse(false))
                .findFirst();
    }

    public boolean hasElement(E element) {
        return elements.contains(element);
    }

    public boolean hasElement(UUID uuid) {
        return getElement(uuid).isPresent();
    }

    public boolean hasElement(String name) {
        return getElement(name).isPresent();
    }

    public void addElement(E element) {
        if (!elements.contains(element)) {
            elements.add(element);
        }
    }

    public void removeElement(UUID uuid) {
        getElement(uuid).ifPresent(this::removeElement);
    }

    public void removeElement(E element) {
        elements.remove(element);
    }
}
