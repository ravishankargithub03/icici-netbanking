package icici.netbanking.newpassword;

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

@WebServlet("/newpassword")
public class NewPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection connection;
	
    public NewPassword() 
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
			String oldPassword=request.getParameter("oldpassword");
			String newPassword=request.getParameter("newpassword");
			String rePassword=request.getParameter("repassword");
			
			if(newPassword.equals(rePassword))
			{
				
				PreparedStatement ps1 = connection.prepareStatement("select FirstName from Account where AccountNumber=? and Password=?");
				ps1.setLong(1, Login.getUserName());
				ps1.setString(2, oldPassword);
				ResultSet rs = ps1.executeQuery();
				
				if(rs.next())
				{
					PreparedStatement ps = connection.prepareStatement("update Account set Password=? where AccountNumber=? and Password=?");
					ps.setString(1, newPassword);
					ps.setLong(2, Login.getUserName());
					ps.setString(3, oldPassword);
					ps.executeUpdate();
					
					pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
					pw.println("Password Updated Successfully!!<br><br>");
					pw.println("<a href=userhome.html>Home</>");
					pw.println("</h2> </center> </body> </html>");
				}
				else
				{
					pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
					pw.println("Old Password is Rong!!<br><br>");
					pw.println("<a href=userhome.html>Home</>");
					pw.println("</h2> </center> </body> </html>");
				}
			}
			else
			{
				pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
				pw.println("New Password Mismatch!!<br><br>");
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
