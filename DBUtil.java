import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBUtil {
	
	static String driver="org.apache.derby.jdbc.EmbeddedDriver";
	static String parola="";
	static String utilizator="";
	private static Connection conn=null; 
	
	private static DBUtil instanta=null;
	
	private DBUtil()
	{
		buildConnection();
	}
	
	public static Connection getConnection()
	{
		if(instanta==null)
			instanta=new DBUtil();
		return conn;
	}
	
	public static void displaySQLExceptions(SQLException e)
	{
		while(e!=null)
		{
			System.out.println("SQL State :"+e.getSQLState());
			System.out.println("Error Code :"+e.getErrorCode());
			System.out.println("Message :"+e.getMessage());
			Throwable t=e.getCause();
			while(t!=null)
			{
				System.out.println("Cause :"+t);
				t=t.getCause();
			}
			e=e.getNextException();
		}
	}
	
	public static void buildConnection()
	{
		String url="jdbc:derby://localhost:1527/clients;create=true";
		try
		{
			conn=DriverManager.getConnection(url);
		} catch(SQLException e)
		{
			e.printStackTrace();
		} finally {
			try {
				if(!conn.isClosed())
					System.out.println("JDBC 4.0 Successfully connected");
			}catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

}
