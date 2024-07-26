package org.dynapi.squirtle.core.interfaces;

import lombok.Builder;
import lombok.Getter;
import lombok.With;
import org.dynapi.squirtle.core.enums.Dialects;

@With
@Getter
@Builder(toBuilder = true)
public class SqlAbleConfig {
    private final String quoteChar;
    private final String secondaryQuoteChar;
    private final String aliasQuoteChar;
    private final boolean asKeyword;
    private final boolean withAlias;
    private final boolean withNamespace;
    private final boolean subCriterion;
    private final boolean subQuery;
    private final boolean groupByAlias;
    private final Dialects dialect;
}
