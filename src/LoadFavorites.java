import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


@WebServlet("/loadfavorites")
public class LoadFavorites extends HttpServlet {

	private static final long serialVersionUID = 7;

	@SuppressWarnings("resource")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("Username");
		PrintWriter out = response.getWriter();
		int UID = -1;
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
			String query = "SELECT UID from User WHERE Username = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, username);
			rs = ps.executeQuery();		
			if(rs.next()) {
				UID = rs.getInt("UID");
			}
			query = "SELECT * from Favorites WHERE UID = ?";
			ps = conn.prepareStatement(query);
			ps.setInt(1, UID);
			rs = ps.executeQuery();		
			ArrayList<Integer> CIDs = new ArrayList<Integer>();
			while(rs.next()) {
				CIDs.add(rs.getInt("CID"));
			}
			
			int count = 0;
			ArrayList<Fave> faves = new ArrayList<Fave>();
			while(count < CIDs.size()) {
				query = "SELECT * from Company WHERE CID = ?";
				ps = conn.prepareStatement(query);
				ps.setInt(1, CIDs.get(count));
				rs = ps.executeQuery();		
				if(rs.next()) {
					Comp comp = new Comp(rs.getString("Ticker"), rs.getString("CompanyName"), CIDs.get(count));
					comp.ticker = comp.ticker.substring(1, comp.ticker.length()-1);
					comp.name = comp.name.substring(1, comp.name.length()-1);
					Stock stock = new Stock(comp.ticker);
					faves.add(new Fave(CIDs.get(count), stock.prev_close, stock.last, comp.ticker, comp.name));
					count++;
				}
			}
			response.setContentType("application/json");
			Gson gson = new Gson();
			String reply = gson.toJson(faves);
			out.print(reply);
			out.flush();
			out.close();	
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