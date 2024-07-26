package org.dynapi.squirtle.core.enums;

public class Order extends Enumerator {
    public final static Order ASC = new Order("ASC");
    public final static Order DESC = new Order("DESC");

    public Order(String value) {
        super(value);
    }
}
