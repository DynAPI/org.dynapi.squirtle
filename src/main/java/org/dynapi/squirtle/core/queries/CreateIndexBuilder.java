package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.criterion.Criterion;
import org.dynapi.squirtle.core.terms.criterion.Index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateIndexBuilder implements SqlAble {
    protected Index index = null;
    protected List<Column> columns = new ArrayList<>();
    protected Table table = null;
    protected Criterion wheres = null;
    protected boolean isUnique = false;
    protected boolean ifNotExists = false;

    public CreateIndexBuilder(CreateIndexBuilder original) {
        this.index = CloneUtils.copyConstructorClone(original.index);
        this.columns = CloneUtils.copyConstructorCloneCollection(original.columns);
        this.table = CloneUtils.copyConstructorClone(original.table);
        this.wheres = CloneUtils.copyConstructorClone(original.wheres);
        this.isUnique = original.isUnique;
        this.ifNotExists = original.ifNotExists;
    }

    public CreateIndexBuilder() {}

    public CreateIndexBuilder createIndex(String indexName) {
        return createIndex(new Index(null, indexName));
    }

    public CreateIndexBuilder createIndex(Index index) {
        this.index = index;
        return this;
    }

    public CreateIndexBuilder columns(String... columns) {
        return columns((Column[]) Arrays.stream(columns).map(Column::new).toArray());
    }

    public CreateIndexBuilder columns(Column... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    public CreateIndexBuilder on(String tableName) {
        return on(new Table(null, tableName));
    }

    public CreateIndexBuilder on(Table table) {
        this.table = table;
        return this;
    }

    public CreateIndexBuilder where(Criterion criterion) {
        if (this.wheres != null) {
            this.wheres = this.wheres.and(criterion);
        } else {
            this.wheres = criterion;
        }
        return this;
    }

    public CreateIndexBuilder unique() {
        return unique(true);
    }

    public CreateIndexBuilder unique(boolean unique) {
        this.isUnique = unique;
        return this;
    }

    public CreateIndexBuilder ifNotExists() {
        return ifNotExists(true);
    }

    public CreateIndexBuilder ifNotExists(boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
        return this;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        if (columns.isEmpty()) throw new RuntimeException("Cannot create index without columns");
        if (table == null) throw new RuntimeException("Cannot create index without table");
        String columnsSql = String.join(",", columns.stream().map(col -> col.getNameSql(config)).toList());
        String uniqueSql = isUnique ? " UNIQUE" : "";
        String ifNotExistsSql = ifNotExists ? " IF NOT EXISTS" : "";
        String baseSql = String.format("CREATE%s INDEX%s %s ON %s(%s)", uniqueSql, ifNotExistsSql, index.getSql(config), table.getSql(config), columnsSql);
        if (wheres != null)
            baseSql += String.format(" WHERE %s", wheres.getSql(config));
        return baseSql;
    }
}
