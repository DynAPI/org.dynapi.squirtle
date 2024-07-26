package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.enums.JoinType;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Node;

import java.util.Collection;

public class Join implements SqlAble {
    protected Node item;
    protected JoinType how;

    public Join(Node item, JoinType how) {
        this.item = item;
        this.how = how;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String sql = String.format("JOIN %s", item.getSql(config.withSubQuery(true).withWithAlias(true)));

        if (!how.value.isEmpty())
            return String.format("%s %s", how.value, sql);
        return sql;
    }

    public void validate(Collection<Table> from, Collection<Table> joins) {}

    public Join replaceTable(Table currentTable, Table newTable) {
        item = item.replaceTable(currentTable, newTable);
        return this;
    }
}
