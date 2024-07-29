package org.dynapi.squirtle.core.interfaces;

import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.dynapi.squirtle.core.enums.Dialects;
import org.dynapi.squirtle.core.queries.Query;

@With
@Getter
@Builder(toBuilder = true)
public class SqlAbleConfig {
    private final String quoteChar;
    private final String secondaryQuoteChar;
    private final String aliasQuoteChar;
    private final String queryAliasQuoteChar;
    private final boolean asKeyword;
    private final boolean withAlias;
    private final boolean withNamespace;
    private final boolean subCriterion;
    private final boolean subQuery;
    private final boolean groupByAlias;
    private final boolean orderByAlias;
    private final Dialects dialect;
    private final Class<? extends Query> queryClass;
}
