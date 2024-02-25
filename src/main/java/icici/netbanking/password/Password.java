package icici.netbanking.password;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import icici.netbanking.registration.Registration;

@WebServlet("/password")
public class Password extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	Connection connection;
	
    public Password() 
    {
        super();
    }
    
    public void init() throws ServletException 
	{
		try 
		{
			ServletContext sc = getServletContext();
			String driverClass = sc.getInitParameter("driver");
			String url = sc.getInitParameter("url");
			String userName = sc.getInitParameter("username");
			String password = sc.getInitParameter("password");
			Class.forName(driverClass);
			connection = DriverManager.getConnection(url,userName,password);
		} 
		catch (ClassNotFoundException | SQLException e) 
		{
			e.printStackTrace();
		}
	}
    
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
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String password = request.getParameter("password");
		Double amount = Double.parseDouble(request.getParameter("amount"));
		DecimalFormat df = new DecimalFormat("0.00");
		amount= Double.parseDouble(df.format(amount));
		
		try 
		{
			if(amount>0)
			{
				PreparedStatement ps1 = connection.prepareStatement("update Account set Password=?, TotalAmount=? where AccountNumber=?");
				ps1.setString(1, password);
				ps1.setDouble(2, amount);
				ps1.setLong(3, Registration.getAccountNumber());
				ps1.executeUpdate();
				
				
				Long min = 1234560000000000000L;
				Long max = 1264569999999999999L;
				Random ran = new Random();
				Long transactionId = ran.nextLong(max-min)+min;
				
				
				Statement stm = connection.createStatement();
				stm.execute("create table Statement"+Registration.getAccountNumber()+"(AccountNumber number(16), TransactionId number(21) primary key, Deposit number(10,2), Withdrawal number(10,2), TotalAmount number(10,2), TransactionDate date, foreign key (AccountNumber) references Account(AccountNumber))");
				PreparedStatement ps=connection.prepareStatement("insert into Statement"+Registration.getAccountNumber()+" values(?,?,?,?,?,?)");
				ps.setLong(1, Registration.getAccountNumber());
				ps.setLong(2, transactionId);
				ps.setDouble(3, amount);
				ps.setDouble(4, 0.0);
				ps.setDouble(5, amount);
				Long cTM = System.currentTimeMillis();
				Date date = new Date(cTM);
				ps.setDate(6, date);
				
				ps.executeUpdate();
				
				RequestDispatcher rd = request.getRequestDispatcher("/registered");
				rd.forward(request, response);
			}
			else
			{
				PrintWriter pw = response.getWriter();
				pw.println("<html> <body bgcolor=#b3d9d9 text=#804040> <center> <h2> <br><br>");
				pw.println("Enter Positive Amount<br><br>");
				pw.println("<a href=password.html>Back</>");
				pw.println("</h2> </center> </body> </html>");
			}
		} 
		catch (SQLException e) 
		{
			PrintWriter pw = response.getWriter();
			pw.println("<html> <body> <center>");
			pw.println("<h1>Data is not inserted Please Fill form Correctly</h1>");
			pw.print(e);
			pw.println("</center></body></html>");
		}
	}

}
