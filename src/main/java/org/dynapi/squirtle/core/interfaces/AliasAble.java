package org.dynapi.squirtle.core.interfaces;

public interface AliasAble {
    String getAlias();
    void setAlias(String alias);

    // weird syntax. I know. But this basically means `as` returns itself

    /**
     * changes the alias and returns itself for chained building
     * @param alias new alias
     */
    @SuppressWarnings("unchecked")
    default <T extends AliasAble> T as(String alias) {
        setAlias(alias);
        return (T) this;
    }
}
