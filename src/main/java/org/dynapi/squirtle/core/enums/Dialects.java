package org.dynapi.squirtle.core.enums;

public class Dialects extends Enumerator {
    public final static Dialects VERTICA = new Dialects("vertica");
    public final static Dialects CLICKHOUSE = new Dialects("clickhouse");
    public final static Dialects ORACLE = new Dialects("oracle");
    public final static Dialects MSSQL = new Dialects("mssql");
    public final static Dialects MYSQL = new Dialects("mysql");
    public final static Dialects POSTGRESQL = new Dialects("postgresql");
    public final static Dialects REDSHIFT = new Dialects("redshift");
    public final static Dialects SQLITE = new Dialects("sqlite");
    public final static Dialects SNOWFLAKE = new Dialects("snowflake");

    public Dialects(String value) {
        super(value);
    }
}
