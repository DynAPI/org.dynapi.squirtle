package org.dynapi.squirtle.core.queries;

public class Database extends Schema {
    public Database(Database original) {
        super(original);
    }

    public Database(String name) {
        this(name, null);
    }

    public Database(String name, Schema parent) {
        super(name, parent);
    }

    public Schema schema(String name) {
        return new Schema(name, this);
    }
}
