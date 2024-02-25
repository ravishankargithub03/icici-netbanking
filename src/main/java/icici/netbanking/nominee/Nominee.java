package icici.netbanking.nominee;

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

@WebServlet("/nominee")
public class Nominee extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection connection;
	
    public Nominee() 
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
			String oldNominee=request.getParameter("oldnominee");
			String newNominee=request.getParameter("newnominee");
			String nomineeRelation=request.getParameter("nomineerelation");
			
			
			PreparedStatement ps1 = connection.prepareStatement("select FirstName from Account where NomineeName=?");
			ps1.setString(1, oldNominee);
			ResultSet rs = ps1.executeQuery();
			
			if(rs.next())
			{
				PreparedStatement ps = connection.prepareStatement("update Account set NomineeName=?, NomineeRelation=? where AccountNumber=? and NomineeName=?");
				ps.setString(1, newNominee);
				ps.setString(2, nomineeRelation);
				ps.setLong(3, Login.getUserName());
				ps.setString(4, oldNominee);
				ps.executeUpdate();
				
				pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
				pw.println("Nominee Updated Successfully!!<br><br>");
				pw.println("<a href=userhome.html>Home</>");
				pw.println("</h2> </center> </body> </html>");
			}
			else
			{
				pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
				pw.println("Old Nominee Name is Rong!!<br><br>");
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
