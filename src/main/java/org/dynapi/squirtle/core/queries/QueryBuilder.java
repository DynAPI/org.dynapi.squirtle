package org.dynapi.squirtle.core.queries;

import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.interfaces.FinalSqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;
import org.dynapi.squirtle.core.terms.Term;
import org.dynapi.squirtle.core.terms.values.ValueWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QueryBuilder extends Selectable, Term implements FinalSqlAble {
    public static String QUOTE_CHAR = "\"";
    public static String SECONDARY_QUOTE_CHAR = "'";
    public static String ALIAS_QUOTE_CHAR = null;
    public static String QUERY_ALIAS_QUOTE_CHAR = null;
    public static Class<? extends Query> QUERY_CLASS = Query.class;

    protected List<Selectable> from = new ArrayList<>();
    protected Selectable insertTable = null;
    protected Selectable updateTable = null;
    protected Selectable deleteFrom = null;
    protected boolean replace = false;

    protected List<Object> with = new ArrayList<>();
    protected List<SqlAble> selects = new ArrayList<>();
    protected List<Object> forceIndexes = new ArrayList<>();
    protected List<Object> useIndexes = new ArrayList<>();
    protected List<Object> columns = new ArrayList<>();
    protected List<Object> values = new ArrayList<>();
    protected boolean distinct = false;
    protected boolean ignore = false;

    protected boolean forUpdate = false;

    protected Object wheres = null;
    protected Object preWheres = null;
    protected List<Object> groupBys = new ArrayList<>();
    protected boolean withTotals = false;
    protected Object havings = null;
    protected List<Object> orderBys = new ArrayList<>();
    protected List<Join> joins = new ArrayList<>();
    protected List<Object> unions = new ArrayList<>();
    protected List<Object> using = new ArrayList<>();

    protected Integer limit = null;
    protected Integer offset = null;

    protected List<Object> updates = new ArrayList<>();

    protected boolean selectStar = false;
    protected Set<Object> selectStarTables = new HashSet<>();
    protected boolean mySqlRollup = false;
    protected boolean selectInto = false;

    protected int subqueryCount = 0;
    protected boolean foreignTable = false;

    protected final Dialects dialects;
    protected final boolean wrapSetOperationQueries;
    protected final boolean asKeyword;

    protected final Class<? extends ValueWrapper> wrapperClass;

    protected final boolean immutable;

    public QueryBuilder(Dialects dialect, Boolean wrapSetOperationQueries, Class<? extends ValueWrapper> wrapperClass, Boolean immutable, Boolean asKeyword) {
        super(null);

        this.dialects = dialect;
        this.asKeyword = asKeyword == null ? false : asKeyword;
        this.wrapSetOperationQueries = wrapSetOperationQueries == null ? true : wrapSetOperationQueries;
        this.wrapperClass = wrapperClass;
        this.immutable = immutable == null ? true : immutable;
    }

    protected SqlAbleConfig getSqlAbleConfigWithDefaults(SqlAbleConfig config) {
        SqlAbleConfig.SqlAbleConfigBuilder builder = config.toBuilder();
        if (config.getQuoteChar() == null) builder.quoteChar(QUOTE_CHAR);
        if (config.getSecondaryQuoteChar() == null) builder.secondaryQuoteChar(SECONDARY_QUOTE_CHAR);
        return builder.build();
    }

    // xxx: __copy__

    public QueryBuilder from(String tableName) {
        return from(new Table(null, tableName));
    }

    public QueryBuilder from(Selectable selectable) {
        this.from.add(selectable);

        if ((selectable instanceof QueryBuilder) || (selectable instanceof SetOperation) && selectable.getAlias() == null) {
            int subQueryCount;
            if (selectable instanceof QueryBuilder) subQueryCount = ((QueryBuilder) selectable).subqueryCount;
            else subQueryCount = 0;

            subQueryCount = Math.max(this.subqueryCount, subQueryCount);
            selectable.setAlias(String.format("sq%d", subQueryCount));
            this.subqueryCount = subQueryCount;
        }

        return this;
    }
}
