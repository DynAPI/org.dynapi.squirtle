package org.dynapi.squirtle.core.terms.values;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.criterion.Field;

public class Values extends Term {
    private final Field field;

    public Values(Values original) {
        super(original);
        this.field = CloneUtils.copyConstructorCloneNoFail(original.field);
    }

    public Values(String fieldName) {
        this(new Field(fieldName));
    }

    public Values(Field field) {
        this.field = field;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        return String.format("VALUES(%s)", field.getSql(config));
    }
}
