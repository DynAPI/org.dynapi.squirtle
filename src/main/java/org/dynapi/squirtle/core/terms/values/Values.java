package org.dynapi.squirtle.core.terms.values;

import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.criterion.Field;

public class Values extends Term {
    private final Field field;

    public Values(String field) {
        this(new Field(field));
    }

    public Values(Field field) {
        super(null);
        this.field = field;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return String.format("VALUES(%s)", field.getSql(config));
    }
}
