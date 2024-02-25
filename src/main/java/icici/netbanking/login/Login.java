package icici.netbanking.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Login extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
    Connection connection;
    
    public Login() 
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

    private static Long userName;
    
    
	public static Long getUserName() 
	{
		return userName;
	}

	public static void setUserName(Long userName) 
	{
		Login.userName = userName;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try 
		{
			userName=Long.parseLong(request.getParameter("username"));
			String password=request.getParameter("password");
			PreparedStatement ps = connection.prepareStatement("select FirstName, LastName, image from Account where AccountNumber=? and Password=?");
			ps.setLong(1, userName);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			if(rs.next())
			{
				request.setAttribute("firstname", rs.getString("FirstName"));
				request.setAttribute("lastname", rs.getString("LastName"));
				RequestDispatcher rd = request.getRequestDispatcher("/userhome.jsp");
				rd.forward(request, response);
			
			}
			else
			{
				RequestDispatcher rd = request.getRequestDispatcher("/logininvalid.html");
				rd.forward(request, response);
			}
		} 
		catch (SQLException e) 
		{
			PrintWriter pw = response.getWriter();
			pw.println("<html> <body bgcolor=red text=yellow> <center>");
			pw.println("<h1>UserName/Password Not Inserted</h1>");
			pw.print(e);
			pw.println("</center></body></html>");
		}
	}

}
