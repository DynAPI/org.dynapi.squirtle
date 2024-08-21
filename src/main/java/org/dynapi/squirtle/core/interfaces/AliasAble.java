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
    @SuppressWarnings("unchecked")
    // weird syntax. I know. But this basically means `as` returns itself
    default <T extends AliasAble> T as(String alias) {
        setAlias(alias);
        return (T) this;
    }
}
