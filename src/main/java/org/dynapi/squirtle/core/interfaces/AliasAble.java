package org.dynapi.squirtle.core.interfaces;

public interface AliasAble {
    String getAlias();

    /**
     * @see AliasAble#as(String)
     */
    void setAlias(String alias);

    /**
     * changes the alias and returns itself for chained building
     * @param alias new alias
     * @see AliasAble#setAlias(String)
     */
    AliasAble as(String alias);
}
