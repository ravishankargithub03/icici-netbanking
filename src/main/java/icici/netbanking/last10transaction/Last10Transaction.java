package icici.netbanking.last10transaction;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import icici.netbanking.login.Login;

@WebServlet("/last10transaction")
public class Last10Transaction extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	Connection connection;
	
    public Last10Transaction() 
    {
        super();
    }

    @Override
    public void init() throws ServletException 
    {
    	try 
    	{
			ServletContext sc = getServletContext();
			String driverClass = sc.getInitParameter("driver");
			String uRL = sc.getInitParameter("url");
			String userName = sc.getInitParameter("username");
			String password = sc.getInitParameter("password");
			Class.forName(driverClass);
			connection = DriverManager.getConnection(uRL,userName,password);
		} 
    	catch (ClassNotFoundException | SQLException e) 
    	{
			e.printStackTrace();
		}
    }
    
    @Override
    public void destroy() 
    {
    	try 
    	{
			connection.close();
		} 
    	catch (SQLException e) 
    	{
			e.printStackTrace();
		}
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PrintWriter pw = response.getWriter();
		try 
		{
			PreparedStatement ps = connection.prepareStatement("select * from Statement"+Login.getUserName()+"  order by transactionid desc");
			ResultSet rs = ps.executeQuery();
			
			pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h3> <br><br>");
			for(Integer i=1; i<=10;i++)
			{
				if(rs.next())
				{
					
					pw.println(rs.getLong("TransactionId"));
					pw.println(rs.getDouble("Withdrawal"));
					pw.println(rs.getDouble("Deposit"));
					pw.println(rs.getDouble("TotalAmount"));
					pw.println(rs.getDate("Transactiondate")+"<br>");
				}
			}
			pw.println("<br><br><a href=userhome.html>Home</>");
			pw.println("</h2> </center> </body> </html>");
		} 
		catch (SQLException e) 
		{
			pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
			pw.println(e);
			pw.println("</h2> </center> </body> </html>");
		}
	}

}
