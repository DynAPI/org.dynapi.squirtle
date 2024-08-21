package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.interfaces.AliasAble;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.criterion.Field;
import org.dynapi.squirtle.core.terms.criterion.Star;

public interface Selectable extends SqlAble, Node, AliasAble {
    default Field field(String name) {
        return new Field(name, this);
    }

    default Star asStar() {
        return new Star(this);
    }

    default String getTableName() {
        return getAlias();
    }
}
