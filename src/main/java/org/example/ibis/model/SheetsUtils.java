package org.example.ibis.model;
public class SheetsUtils {

    public static String toColumnLetter(int index) {
        StringBuilder sb = new StringBuilder();
        while (index >= 0) {
            sb.append((char) ('A' + (index % 26)));
            index = (index / 26) - 1;
        }
        return sb.reverse().toString();
    }

    public static String createRange(String sheetName, int startRow, int startCol, int endRow, int endCol) {
        return String.format("'%s'!%s%d:%s%d",
                sheetName,
                toColumnLetter(startCol),
                startRow,
                toColumnLetter(endCol),
                endRow);
    }
}