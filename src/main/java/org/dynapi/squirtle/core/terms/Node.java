package org.dynapi.squirtle.core.terms;

import java.util.List;

public interface Node {
    /** @apiNote internal usage only */
    default Boolean isAggregate() {
        return null;
    }

    /** @apiNote internal usage only */
    default List<Node> nodes() {
        return List.of(this);
    }

    /** @apiNote internal usage only */
    @SuppressWarnings("unchecked")
    default<T extends Node> List<T> find(Class<T> type) {
        return (List<T>) nodes().stream().filter(type::isInstance).toList();
    }
}
