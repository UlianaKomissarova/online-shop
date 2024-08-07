import db.*;
import util.InitSqlScheme;

public class Main {
    public static void main(String[] args) {
        ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
        InitSqlScheme.initSqlScheme(connectionManager);
        InitSqlScheme.initSqlData(connectionManager);
    }
}