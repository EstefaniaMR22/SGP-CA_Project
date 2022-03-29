package assets.utils;

public enum SQLStates {
    SQL_NO_CONNECTION("08S01"),
    ;

    private final String sqlState;

    public String getSqlState() {
        return sqlState;
    }

    SQLStates(String sqlState) {
        this.sqlState = sqlState;
    }
}
