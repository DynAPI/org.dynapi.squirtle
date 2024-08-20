package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.enums.JoinType;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

import java.util.Collection;

public class Join implements SqlAble {
    protected Selectable item;
    protected JoinType how;

    public Join(Join original) {
        this.item = CloneUtils.copyConstructorClone(original.item);
        this.how = original.how;
    }

    public Join(Selectable item, JoinType how) {
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

    public void validate(Collection<Selectable> from, Collection<Join> joins) {}

    public Join replaceTable(Table currentTable, Table newTable) {
//        item = item.replaceTable(currentTable, newTable);
        return this;
    }
}
