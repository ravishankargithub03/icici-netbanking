package icici.netbanking.registration.copy;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/registration")
public class Registration extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	Connection connection;
       
    public Registration() 
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
	
	private String gender;
	private static Long accountNumber;
	
	
	public static Long getAccountNumber() 
	{
		return accountNumber;
	}

	public static void setAccountNumber(Long accountNumber) 
	{
		Registration.accountNumber = accountNumber;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
			try {
				Long min = 1260000000000000L;
				Long max = 1260999999999999L;
				Random ran = new Random();
				accountNumber = ran.nextLong(max-min)+min;
				
				String firstName = request.getParameter("firstname");
				String lastName = request.getParameter("firstname");
				String fatherName = request.getParameter("fathername");
				String motherName = request.getParameter("mothername");
				
				if(request.getParameter("gender")!=null)
				{
					gender=request.getParameter("gender");
				}
				SimpleDateFormat sDF = new SimpleDateFormat("yyyy-MM-dd");
				
				Date date = sDF.parse(request.getParameter("dateofbirth"));
				sDF = new SimpleDateFormat("dd-MMM-yy");
				String dateOfBirth = sDF.format(date);
 
				Long aadharNumber = Long.parseLong(request.getParameter("aadharnumber"));
				Long mobileNumber = Long.parseLong(request.getParameter("mobilenumber"));
				String emailId = request.getParameter("emailid");
				String address = request.getParameter("address");
				String nomineeName = request.getParameter("nomineename");
				String nomineeRelation = request.getParameter("nomineerelation");
				byte[] image = request.getParameter("image").getBytes();
				
				PreparedStatement ps = connection.prepareStatement("insert into Account values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				ps.setLong(1, accountNumber);
				ps.setString(2, firstName);
				ps.setString(3, lastName);
				ps.setString(4, fatherName);
				ps.setString(5, motherName);
				ps.setString(6, gender);
				ps.setString(7, dateOfBirth);
				ps.setLong(8, aadharNumber);
				ps.setLong(9, mobileNumber);
				ps.setString(10, emailId);
				ps.setString(11, address);
				ps.setString(12, nomineeName);
				ps.setString(13, nomineeRelation);
				ps.setBytes(14, image);
				ps.setString(15, null);
				ps.setDouble(16, 0.0);
				
				ps.executeUpdate();
				
				RequestDispatcher rd = request.getRequestDispatcher("/password.html");
				rd.forward(request, response);
				
			} 
			catch (NumberFormatException | ParseException | IOException | SQLException e) 
			{
				PrintWriter pw = response.getWriter();
				pw.println("<html> <body bgcolor=red text=yellow> <center>");
				pw.println("<h1>Data is not inserted Please Fill form Correctly</h1>");
				pw.print(e);
				pw.println("</center></body></html>");
			}
		
	}
}
