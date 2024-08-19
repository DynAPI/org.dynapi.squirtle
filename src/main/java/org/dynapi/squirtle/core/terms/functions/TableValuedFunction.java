package org.dynapi.squirtle.core.terms.functions;

import lombok.NonNull;
import org.dynapi.squirtle.core.queries.Schema;
import org.dynapi.squirtle.core.queries.Selectable;

/**
 * A {@link Function} that can be used in a {@code SELECT} query. <br>
 *
 * @see Function
 * @see <a href="https://www.sqlite.org/pragma.html">https://www.sqlite.org/pragma.html</a>
 */
public class TableValuedFunction extends Function implements Selectable {
    public TableValuedFunction(String alias, @NonNull String name, Object... args) {
        super(alias, name, args);
    }

    public TableValuedFunction(String alias, @NonNull String name, Schema schema, Object... args) {
        super(alias, name, schema, args);
    }
}
