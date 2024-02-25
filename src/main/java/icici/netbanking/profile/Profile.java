package icici.netbanking.profile;

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



@WebServlet("/profile")
public class Profile extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	Connection connection;
	
    public Profile() 
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
    
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try 
		{
			PreparedStatement ps = connection.prepareStatement("select * from Account where AccountNumber=?");
			ps.setLong(1, Login.getUserName());
			ResultSet rs = ps.executeQuery();
			
			PrintWriter pw = response.getWriter();
			
			if(rs.next())
			{	
				pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br>");
				pw.println("<u> Profile </u><br> <br>");
				pw.println("Account Number : "+rs.getLong(1)+"<br><br>");
				pw.println("NAME : "+rs.getString("FirstName")+" "+rs.getString("LastName")+"<br> <br>");
				pw.println("FATHER NAME : "+rs.getString("FatherName")+"<br><br>");
				pw.println("MOTHER NAME : "+rs.getString("MotherName")+"<br><br>");
				pw.println("GENDER : "+rs.getString("Gender")+"<br><br>");
				pw.println("DATE OF BIRTH : "+rs.getDate("DateOfBirth")+"<br><br>");
				pw.println("AADHAR NUMBER : "+rs.getLong("AadharNumber")+"<br><br>");
				pw.println("MOBILE NUMBER : "+rs.getLong("MobileNumber")+"<br><br>");
				pw.println("EMAIL ID : "+rs.getString("EmailId")+"<br><br>");
				pw.println("PRESENT ADDRESS : "+rs.getString("PresentAddress")+"<br><br>");
				pw.println("NOMINEE NAME : "+rs.getString("NomineeName")+"<br><br>");
				pw.println("NOMINEE RELATION : "+rs.getString("NomineeRekation")+"<br><br>");
				pw.println("</h2> </center> </body> </html>");
			}
			else
			{
				pw.println("<html> <body bgcolor=magenta text=blue> <center> <h1> <br>");
				pw.println("Server Problem");
				pw.println("</h1> </center> </body> </html>");
			}
		} 
		catch (SQLException | IOException e) 
		{
			e.printStackTrace();
		}
	}

}
