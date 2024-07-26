package org.dynapi.squirtle.core.enums;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Enumerator {
    public final String value;

    public Enumerator(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
