package icici.netbanking.mobile;

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

@WebServlet("/mobile")
public class Mobile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection connection;
	
    public Mobile() 
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
    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PrintWriter pw = response.getWriter();
		try 
		{
			Long oldMobile=Long.parseLong(request.getParameter("oldnumber"));
			Long newMobile=Long.parseLong(request.getParameter("newnumber"));
			
			if(request.getParameter("oldnumber")!=null&&request.getParameter("newnumber")!=null)
			{
				PreparedStatement ps1 = connection.prepareStatement("select FirstName from Account where MobileNumber=?");
				ps1.setLong(1, oldMobile);
				ResultSet rs = ps1.executeQuery();
				
				if(rs.next())
				{
					PreparedStatement ps = connection.prepareStatement("update Account set MobileNumber=? where AccountNumber=? and MobileNumber=?");
					ps.setLong(1, newMobile);
					ps.setLong(2, Login.getUserName());
					ps.setLong(3, oldMobile);
					ps.executeUpdate();
					
					pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
					pw.println("Mobile Number Updated Successfully!!<br><br>");
					pw.println("<a href=userhome.html>Home</>");
					pw.println("</h2> </center> </body> </html>");
				}
				else
				{
					pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
					pw.println("Old Mobile Number Rong!!<br><br>");
					pw.println("<a href=userhome.html>Home</>");
					pw.println("</h2> </center> </body> </html>");
				}
			}
			else
			{
				pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
				pw.println("You Not Enter Mobile Number!!<br><br>");
				pw.println("<a href=userhome.html>Home</>");
				pw.println("</h2> </center> </body> </html>");
			}
		} 
		catch (SQLException e) 
		{
			pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
			pw.println(e);
			pw.println("</h2> </center> </body> </html>");
		}
	}
}
