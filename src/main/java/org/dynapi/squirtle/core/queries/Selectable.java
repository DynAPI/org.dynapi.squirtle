package org.dynapi.squirtle.core.queries;

import lombok.Getter;
import org.dynapi.squirtle.core.terms.Node;
import org.dynapi.squirtle.core.terms.criterion.Field;
import org.dynapi.squirtle.core.terms.criterion.Star;

public class Selectable implements Node {
    @Getter
    protected final String alias;

    public Selectable(String alias) {
        this.alias = alias;
    }

    public Field field(String name) {
        return new Field(null, name, this);
    }

    public Star asStar() {
        return new Star(this);
    }

    public String getTableName() {
        return alias;
    }
}
