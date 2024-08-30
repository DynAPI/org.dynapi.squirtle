package org.dynapi.squirtle.core;

import org.dynapi.squirtle.core.terms.PseudoColumn;

/**
 * @see PseudoColumns#ColumnValue
 * @see PseudoColumns#ObjectID
 * @see PseudoColumns#ObjectValue
 * @see PseudoColumns#RowNum
 * @see PseudoColumns#RowID
 * @see PseudoColumns#SysDate
 */
public class PseudoColumns {
    public static PseudoColumn ColumnValue = new PseudoColumn("COLUMN_VALUE");
    public static PseudoColumn ObjectID = new PseudoColumn("OBJECT_ID");
    public static PseudoColumn ObjectValue = new PseudoColumn("OBJECT_VALUE");
    public static PseudoColumn RowNum = new PseudoColumn("ROWNUM");
    public static PseudoColumn RowID = new PseudoColumn("ROWID");
    public static PseudoColumn SysDate = new PseudoColumn("SYSDATE");
}
