package org.dynapi.squirtle.core.queries;

public class Database extends Schema {
    public Database(String name) {
        this(name, null);
    }

    public Database(String name, Schema parent) {
        super(name, parent);
    }

    public Schema getSchema(String name) {
        return new Schema(name, this);
    }
}
