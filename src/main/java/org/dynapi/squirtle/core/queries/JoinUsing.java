package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.enums.JoinType;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.criterion.Field;

import java.util.ArrayList;
import java.util.Collection;

public class JoinUsing extends Join {
    private Collection<Field> fields;

    public JoinUsing(Selectable item, JoinType how, Collection<Field> fields) {
        super(item, how);
        this.fields = fields;
    }

    @Override
    public JoinUsing replaceTable(Table currentTable, Table newTable) {
        item = (currentTable.equals(item)) ? newTable : item;
        fields = new ArrayList<>(fields.stream().map(field -> field.replaceTable(currentTable, newTable)).toList());
        return this;
    }

    @Override
    public void validate(Collection<Selectable> from, Collection<Join> joins) {}

    @Override
    public String getSql(SqlAbleConfig config) {
        String joinSql = super.getSql(config);
        return String.format(
                "%s USING (%s)",
                joinSql,
                String.join(",", fields.stream().map(field -> field.getSql(config)).toList())
        );
    }
}
