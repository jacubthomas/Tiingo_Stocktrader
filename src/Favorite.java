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


@WebServlet("/favorite")
public class Favorite extends HttpServlet {

	private static final long serialVersionUID = 6;

	@SuppressWarnings("resource")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("Username");
		String ticker = "\"" + request.getParameter("Ticker") + "\"";
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
			String query = "SELECT CID, UID from Company, User WHERE Ticker = ? AND Username = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, ticker);
			ps.setString(2, username);
			rs = ps.executeQuery();		
			int CID = -1;
			int UID = -1;
			if(rs.next()) {
				CID = rs.getInt("CID");
				UID = rs.getInt("UID");
			}
			query = "SELECT * from Favorites WHERE UID = ? AND CID = ?";
			ps = conn.prepareStatement(query);
			ps.setInt(1, UID);
			ps.setInt(2, CID);
			rs = ps.executeQuery();		
			boolean already_favorited = false;
			while(rs.next()) {
				already_favorited = true;
			}
			
			if(!already_favorited) {
				query = "INSERT into Favorites (UID, CID)" +
								" values (?, ?)";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, UID);
				ps.setInt(2, CID);
				ps.execute();		
				rs = ps.getGeneratedKeys();
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