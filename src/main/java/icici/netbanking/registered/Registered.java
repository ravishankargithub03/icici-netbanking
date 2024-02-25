package icici.netbanking.registered;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import icici.netbanking.registration.Registration;

@WebServlet("/registered")
public class Registered extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public Registered() 
    {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try 
		{
			PrintWriter pw = response.getWriter();
			pw.println("<html> <body bgcolor=lightgreen text=red> <center> <h1>");
			pw.println("Your Account Number/Username : "+Registration.getAccountNumber()+"<br>");
			pw.println("<a href=login.html>Loging</a>");
			pw.println("</h1> </center> </body> </html>");
			request.setAttribute("accountnumber", Registration.getAccountNumber());
		} 
		catch (IOException e) 
		{
			PrintWriter pw = response.getWriter();
			pw.println("<html> <body bgcolor=red text=yellow> <center>");
			pw.println("<h1>Data is not inserted Please Fill form Correctly</h1>");
			pw.print(e);
			pw.println("</center></body></html>");
		}
	}
}
