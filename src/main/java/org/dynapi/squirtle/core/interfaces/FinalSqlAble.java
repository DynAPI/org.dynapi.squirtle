package org.dynapi.squirtle.core.interfaces;

public interface FinalSqlAble extends SqlAble {
    default String getSql() {
        return getSql(SqlAbleConfig.builder().build());
    }
}
