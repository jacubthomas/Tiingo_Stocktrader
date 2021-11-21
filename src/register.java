

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/register")
public class register extends HttpServlet {
	private static final long serialVersionUID = 2;

	@SuppressWarnings("resource")
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("User");
		String password = request.getParameter("Pass");
		String email = request.getParameter("Email");
		double balance = 50000.00;
		double accountvalue = 50000.00;
		PrintWriter out = response.getWriter();
		try {
		    Class.forName("com.mysql.cj.jdbc.Driver");

		} 
		catch (ClassNotFoundException e) {
		    e.printStackTrace();
		} 
		Connection conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {	
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Assignment4?user=root&password=root");
			String query = "SELECT * from User WHERE Username = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, username);
			rs = ps.executeQuery();
			if(rs.next()) {
				System.out.println("Username Exists.");
				response.setContentType("text/plain");
				out.print("Username Exists");
				return;
			}
			query = "INSERT into User (Username, Password, Email, Balance, AccountValue)" +
							" values (?, ?, ?, ?, ?)";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, email);
			ps.setDouble(4, balance);
			ps.setDouble(5, accountvalue);
			ps.execute();		
			rs = ps.getGeneratedKeys();
			response.setContentType("application/json");
			
			query = "SELECT UID from User WHERE Username = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, username);
			rs = ps.executeQuery();
			if(rs.next()) {
				out.println("{");
				out.println("\"username\":" + "\"" + username + "\",");
				out.println("\"password\":" + "\"" + password + "\",");
				out.println("\"email\":" + "\"" + email + "\",");
				out.println("\"balance\":" + "\"" + balance + "\",");
				out.println("\"account value\":" + "\"" + accountvalue + "\",");
				out.println("\"UID\":" + "\"" + rs.getInt("UID") + "\"");
				out.println("}");
				out.flush();
			}
			
		}catch(SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
	}

}