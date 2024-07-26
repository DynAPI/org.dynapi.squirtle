package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.criterion.Criterion;
import org.dynapi.squirtle.core.terms.criterion.PeriodCriterion;

import java.util.Objects;

public class Table extends Selectable implements SqlAble {
    protected static Schema initSchema(String... names) {
        Schema schema =  new Schema(names[0], null);
        for (int i = 1; i < names.length; i++) {
            schema = new Schema(names[i], schema);
        }
        return schema;
    }

    private final String tableName;
    private final Schema schema;
    private final Class<?> queryClass;
    private SqlAble for_;
    private SqlAble forPortion;

    public Table(String alias, String name) {
        this(alias, name, null, null);
    }

    public Table(String alias, String name, Object schema) {
        this(alias, name, schema, null);
    }

    public Table(String alias, String name, Object schema, Class<? extends Query> queryClass) {
        super(alias);
        this.tableName = name;
        this.schema = (schema == null)
                ? null
                : (schema instanceof Schema)
                ? (Schema) schema
                : initSchema((String) schema);
        this.queryClass = queryClass;
        this.for_ = null;
        this.forPortion = null;
    }

    @Override
    public String getTableName() {
        return alias != null ? alias : tableName;
    }

    @Override
    public String getSql(SqlAbleConfig config) {
        String tableSql = Utils.formatQuotes(tableName, config.getQuoteChar());

        if (schema != null)
            tableSql = String.format("%s.%s", schema.getSql(config), tableSql);

        if (for_ != null)
            tableSql = String.format("%s FOR %s", tableSql, for_.getSql(config));
        else if (forPortion != null)
            tableSql = String.format("%s FOR PORTION OF %s", tableSql, forPortion.getSql(config));

        return Utils.formatAliasSql(tableSql, alias, config);
    }

    public Table asFor(Criterion temporalCriterion) {
        if (for_ != null)
            throw new RuntimeException("'Query' object already has attribute for");
        if (forPortion != null)
            throw new RuntimeException("'Query' object already has attribute forPortion");
        this.for_ = temporalCriterion;
        return this;
    }

    public Table asForPortion(PeriodCriterion periodCriterion) {
        if (forPortion != null)
            throw new RuntimeException("'Query' object already has attribute forPortion");
        if (for_ != null)
            throw new RuntimeException("'Query' object already has attribute for");
        this.forPortion = periodCriterion;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Table table)) return false;
        if (!Objects.equals(tableName, table.tableName)) return false;
        if (!Objects.equals(schema, table.schema)) return false;
        if (!Objects.equals(alias, table.alias)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        SqlAbleConfig config = SqlAbleConfig.builder()
                .quoteChar("\"")
                .build();
        return getSql(config).hashCode();
    }

    public Query select(Term... terms) {
        return Utils.newInstance(queryClass, this).select(terms);
    }

    public Query update(Term... terms) {
        return Utils.newInstance(queryClass, this).update(terms);
    }

    public Query insert(Term... terms) {
        return Utils.newInstance(queryClass, this).insert(terms);
    }
}
