package org.dynapi.squirtle.core.queries;

import lombok.Getter;
import lombok.Setter;
import org.dynapi.squirtle.core.CloneUtils;
import org.dynapi.squirtle.core.Utils;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.criterion.Criterion;
import org.dynapi.squirtle.core.terms.criterion.PeriodCriterion;

import java.util.List;
import java.util.Objects;

public class Table implements Selectable, SqlAble {
    protected static Schema initSchema(String... schemaNames) {
        if (schemaNames == null) return null;
        if (schemaNames.length == 0) throw new IllegalArgumentException("Schema names cannot be empty");
        Schema schema =  new Schema(schemaNames[0], null);
        for (int i = 1; i < schemaNames.length; i++) {
            schema = new Schema(schemaNames[i], schema);
        }
        return schema;
    }

    @Getter @Setter
    private String alias = null;
    protected final String tableName;
    protected final Schema schema;
    protected final Class<? extends Query> queryClass;
    protected SqlAble for_;
    protected SqlAble forPortion;

    public Table(Table original) {
        this.alias = original.alias;
        this.tableName = original.tableName;
        this.schema = CloneUtils.copyConstructorClone(original.schema);
        this.queryClass = original.queryClass;
        this.for_ = CloneUtils.copyConstructorCloneNoFail(original.for_);
        this.forPortion = CloneUtils.copyConstructorCloneNoFail(original.forPortion);
    }

    public Table(String tableName) {
        this(tableName, (Schema) null, null);
    }

    public Table(String tableName, String schemaName) {
        this(tableName, new String[]{schemaName}, null);
    }

    public Table(String tableName, String[] schemaNames) {
        this(tableName, schemaNames, null);
    }

    public Table(String tableName, Schema schema) {
        this(tableName, schema, null);
    }

    public Table(String tableName, String schemaName, Class<? extends Query> queryClass) {
        this(tableName, new String[]{schemaName}, queryClass);
    }

    public Table(String tableName, String[] schemaNames, Class<? extends Query> queryClass) {
        this(tableName, initSchema(schemaNames), queryClass);
    }

    public Table(String name, Schema schema, Class<? extends Query> queryClass) {
        this.tableName = name;
        this.schema = schema;
        this.queryClass = queryClass != null ? queryClass : Query.class;
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

    public QueryBuilder select(Object... terms) {
        return Utils.newInstance(queryClass, new Object[]{ this }).select(List.of(terms));
    }

    public QueryBuilder update() {
        return Utils.newInstance(queryClass, new Object[]{ this }).update(this);
    }

    public QueryBuilder insert(Object... terms) {
        return Utils.newInstance(queryClass, new Object[]{ this }).into(this).insert(terms);
    }
}
