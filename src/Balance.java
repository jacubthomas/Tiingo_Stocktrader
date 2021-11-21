
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class login
 */
@WebServlet("/balance")
public class Balance extends HttpServlet {
	private static final long serialVersionUID = 10;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid = request.getParameter("UID");
		int UID = Integer.parseInt(uid);
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
			String query = "SELECT Balance, AccountValue FROM User WHERE UID = ?";
			ps = conn.prepareStatement(query);
			ps.setInt(1, UID);
			rs = ps.executeQuery();		
			response.setContentType("application/json");
			if(rs.next()) {
				double balance = Double.parseDouble(rs.getString("Balance"));
				DecimalFormat df = new DecimalFormat("#.##");
				double AV = rs.getDouble("AccountValue");
				AV = Double.parseDouble(df.format(AV));
				out.print("{");
				out.print("\"Balance\":" + "\"" + df.format(balance) + "\",");
				out.println("\"AccountValue\":" + AV);
				out.print("}");
				out.flush();
				out.close();
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