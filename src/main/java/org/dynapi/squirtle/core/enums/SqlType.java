package org.dynapi.squirtle.core.enums;

import org.dynapi.squirtle.core.interfaces.SqlAble;
import org.dynapi.squirtle.core.interfaces.SqlAbleConfig;

public class SqlType extends Enumerator {
    public final static SqlType BOOLEAN = new SqlType("BOOLEAN");
    public final static SqlType INTEGER = new SqlType("INTEGER");
    public final static SqlType FLOAT = new SqlType("FLOAT");
    public final static SqlType NUMERIC = new SqlType("NUMERIC");
    public final static SqlType SIGNED = new SqlType("SIGNED");
    public final static SqlType UNSIGNED = new SqlType("UNSIGNED");

    public final static SqlType DATE = new SqlType("DATE");
    public final static SqlType TIME = new SqlType("TIME");
    public final static SqlType TIMESTAMP = new SqlType("TIMESTAMP");

    public final static SqlTypesLengthAble CHAR = new SqlTypesLengthAble("CHAR");
    public final static SqlTypesLengthAble VARCHAR = new SqlTypesLengthAble("VARCHAR");
    public final static SqlTypesLengthAble LONG_VARCHAR = new SqlTypesLengthAble("LONG VARCHAR");
    public final static SqlTypesLengthAble BINARY = new SqlTypesLengthAble("BINARY");
    public final static SqlTypesLengthAble VARBINARY = new SqlTypesLengthAble("VARBINARY");
    public final static SqlTypesLengthAble LONG_VARBINARY = new SqlTypesLengthAble("LONG VARBINARY");

    public SqlType(String value) {
        super(value);
    }

    public static class SqlTypesLengthAble extends SqlType implements SqlAble {
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


    public static class SqlTypeWithLength extends SqlType implements SqlAble {
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
