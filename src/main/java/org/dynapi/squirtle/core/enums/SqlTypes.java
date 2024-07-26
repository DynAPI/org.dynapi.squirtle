package org.dynapi.squirtle.core.enums;

import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

public class SqlTypes extends Enumerator {
    public final static SqlTypes BOOLEAN = new SqlTypes("BOOLEAN");
    public final static SqlTypes INTEGER = new SqlTypes("INTEGER");
    public final static SqlTypes FLOAT = new SqlTypes("FLOAT");
    public final static SqlTypes NUMERIC = new SqlTypes("NUMERIC");
    public final static SqlTypes SIGNED = new SqlTypes("SIGNED");
    public final static SqlTypes UNSIGNED = new SqlTypes("UNSIGNED");

    public final static SqlTypes DATE = new SqlTypes("DATE");
    public final static SqlTypes TIME = new SqlTypes("TIME");
    public final static SqlTypes TIMESTAMP = new SqlTypes("TIMESTAMP");

    public final static SqlTypesLengthAble CHAR = new SqlTypesLengthAble("CHAR");
    public final static SqlTypesLengthAble VARCHAR = new SqlTypesLengthAble("VARCHAR");
    public final static SqlTypesLengthAble LONG_VARCHAR = new SqlTypesLengthAble("LONG VARCHAR");
    public final static SqlTypesLengthAble BINARY = new SqlTypesLengthAble("BINARY");
    public final static SqlTypesLengthAble VARBINARY = new SqlTypesLengthAble("VARBINARY");
    public final static SqlTypesLengthAble LONG_VARBINARY = new SqlTypesLengthAble("LONG VARBINARY");

    public SqlTypes(String value) {
        super(value);
    }

    public static class SqlTypesLengthAble extends SqlTypes implements SqlAble {
        public SqlTypesLengthAble(String value) {
            super(value);
        }

        public SqlTypeWithLength withLength(int length) {
            return new SqlTypeWithLength(value, length);
        }

        @Override
        public String getSql(SqlAbleConfig config) {
            return value;
        }
    }


    public static class SqlTypeWithLength extends SqlTypes implements SqlAble {
        protected final int length;

        public SqlTypeWithLength(String name, int length) {
            super(name);
            this.length = length;
        }

        @Override
        public String getSql(SqlAbleConfig config) {
            return String.format("%s(%d)", value, length);
        }
    }
}
