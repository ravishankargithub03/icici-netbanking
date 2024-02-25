package icici.netbanking.datechoice;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import icici.netbanking.login.Login;


@WebServlet("/datechoice")
public class DateChoice extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	Connection connection;
	
    public DateChoice() 
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
			SimpleDateFormat sDF = new SimpleDateFormat("yyyy-MM-dd");
			
			Date date = sDF.parse(request.getParameter("firstdate"));
			sDF = new SimpleDateFormat("dd-MMM-yy");
			String firstDate = sDF.format(date);
			
			SimpleDateFormat sDF1 = new SimpleDateFormat("yyyy-MM-dd");
			date = sDF1.parse(request.getParameter("seconddate"));
			sDF1 = new SimpleDateFormat("dd-MMM-yy");
			String secondDate = sDF.format(date);
			
			PreparedStatement ps = connection.prepareStatement("select * from Statement"+Login.getUserName()+" where TransactionDate between ? and ? order by transactionid desc");
			
			ps.setString(1, firstDate);
			ps.setString(2, secondDate);
			ResultSet rs = ps.executeQuery();
			
			pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h3> <br><br>");
			
			if(rs.next())
			{
				do
				{
					pw.println(rs.getLong("TransactionId"));
					pw.println(rs.getDouble("Withdrawal"));
					pw.println(rs.getDouble("Deposit"));
					pw.println(rs.getDouble("TotalAmount"));
					pw.println(rs.getDate("Transactiondate")+"<br>");
				}
				while(rs.next());
			}
			else
			{
				pw.println("Record Note Present");
			}
			pw.println("<br><br><a href=userhome.html>Home</>");
			pw.println("</h2> </center> </body> </html>");
		} 
		catch (ParseException | SQLException e) 
		{
			pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
			pw.println(e);
			pw.println("</h2> </center> </body> </html>");
		}
	}

}
