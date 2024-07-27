package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.criterion.Field;
import org.dynapi.squirtle.core.terms.criterion.Star;

public interface Selectable extends Node {
    default Field field(String name) {
        return new Field(null, name, this);
    }

    default Star asStar() {
        return new Star(this);
    }

    default String getTableName() {
        return getAlias();
    }

    String getAlias();
    void setAlias(String alias);
}
