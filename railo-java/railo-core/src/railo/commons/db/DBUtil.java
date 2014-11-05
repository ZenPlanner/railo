package railo.commons.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility for db
 */
public final class DBUtil {

	// TODO impl. this class, not used at the moment
	/**
	 * returns label matching className
	 * @param className
	 * @return label
	 */
	public static String getLabelForDriverClass(String className) {
		if("com.microsoft.jdbc.sqlserver.SQLServerDriver".equals(className))
			return "MSSQL DataBase";
		// TODO connect WS from railo. ch to get more
		return className;
	}

	public static void setAutoCommitEL(Connection conn, boolean b) {
		/*try {
			if(conn!=null){
				if(conn.getAutoCommit()==b) return;
			}
		} 
		catch (Throwable e) {}*/
		
		
		try {

			if(conn!=null) {
                System.out.println("" + System.identityHashCode(conn) + " DBUtil.setAutoCommitEL(" + b + ")");
                conn.setAutoCommit(b);
            }
        } 
        catch (Throwable e) {}
	}

	public static void setReadOnlyEL(Connection conn, boolean b) {
		try {
			if(conn!=null)conn.setReadOnly(b);
		} 
		catch (Throwable e) {}
	}

	public static void commitEL(Connection conn) {
		try {
			if(conn!=null) {
                System.out.println("" + System.identityHashCode(conn) + " DBUtil.commitEL()");
                conn.commit();
            }
		} 
		catch (Throwable e) {}
	}

	public static void setTransactionIsolationEL(Connection conn,int level) {
		try {
			if(conn!=null) {
                System.out.println("" + System.identityHashCode(conn) + " DBUtil.setTransactionIsolation()");
                conn.setTransactionIsolation(level);
            }
		} 
		catch (Throwable e) {}
	}

	public static void closeEL(Statement stat) {
		if(stat!=null) {
            try {
                stat.close();
            } catch (Throwable t) {}
        }
	}

	public static void closeEL(ResultSet rs) {
		if(rs!=null) {
            try {
                rs.close();
            } catch (Throwable t) {}
        }
	}

	public static Connection getConnection(String dsn, String user, String pass) throws SQLException {
		try {
            Connection conn = DriverManager.getConnection(dsn, user, pass);
            System.out.println("" + System.identityHashCode(conn) + " DBUtil.getConnection()");
    		return conn;
        } 
        catch (SQLException e) {
        	if(dsn.indexOf('?')!=-1) {
                String connStr=dsn+"&user="+user+"&password="+pass;
                Connection conn = DriverManager.getConnection(connStr);
                System.out.println("" + System.identityHashCode(conn) + " DBUtil.getConnection()");
                return conn;
            }
        	else throw e;
        }
	}

}
