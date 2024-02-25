package icici.netbanking.deposit;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import icici.netbanking.login.Login;

@WebServlet("/deposit")
public class Deposit extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	Connection connection;
	
    public Deposit() 
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
			PreparedStatement ps = connection.prepareStatement("select TotalAmount from Account where AccountNumber=?");
			ps.setLong(1, Login.getUserName());
			ResultSet rs = ps.executeQuery();
				
			if(rs.next())
			{
				Double depositAmount = Double.parseDouble(request.getParameter("amount"));
				DecimalFormat df = new DecimalFormat("0.00");
				depositAmount= Double.parseDouble(df.format(depositAmount));
				
				if(depositAmount>0)
				{
					PreparedStatement ps3 = connection.prepareStatement("select TransactionId from Statement"+Login.getUserName()+" order by TransactionId desc");
					ResultSet rs1 = ps3.executeQuery();
					
					if(rs1.next())
					{
						Long transactionId = rs1.getLong("TransactionId")+1;
						
						PreparedStatement ps1 = connection.prepareStatement("insert into Statement"+Login.getUserName()+" values(?,?,?,?,?,?)");
						ps1.setLong(1, Login.getUserName());
						ps1.setLong(2, transactionId);
						ps1.setDouble(3, depositAmount);
						ps1.setDouble(4, 0.0);
						ps1.setDouble(5, rs.getDouble("TotalAmount")+depositAmount);
						Long cTM = System.currentTimeMillis();
						Date date = new Date(cTM);
						ps1.setDate(6, date);
						ps1.executeUpdate();
						
						PreparedStatement ps2 = connection.prepareStatement("update Account set TotalAmount=? where AccountNumber=?");
						ps2.setDouble(1, rs.getDouble("TotalAmount")+depositAmount);
						ps2.setLong(2, Login.getUserName());
						ps2.executeUpdate();
							
						pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
						pw.println("Deposit Successfully Complited!!<br><br>");
						pw.println("<a href=userhome.html>Home</>");
						pw.println("</h2> </center> </body> </html>");
					}
					else
					{
						pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
						pw.println("We Have Transaction ID Problem<br><br>");
						pw.println("<a href=deposit.html>Deposit</>");
						pw.println("</h2> </center> </body> </html>");
					}
				}
				else
				{
					pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
					pw.println("Enter Positive Amount<br><br>");
					pw.println("<a href=userhome.html>Home</>");
					pw.println("</h2> </center> </body> </html>");
				}
			}
			else
			{
				pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
				pw.println("Server Problem<br><br>");
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
