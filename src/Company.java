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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

@WebServlet("/company")
public class Company extends HttpServlet{
	private static final long serialVersionUID = 2;
	static String query;
	@SuppressWarnings({ "resource", "unused" })
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		String ticker_ = request.getParameter("Ticker");
		PrintWriter out = response.getWriter();
		try {	
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Assignment4?user=root&password=root");
			System.out.println("established connection");
			query = "SELECT * from Company WHERE Ticker = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, "\"" + ticker_ + "\"");
			rs = ps.executeQuery();
			if(rs.next()) {
				System.out.println("Company Exists in DB.");
				// respond with json boiiiii
				response.setContentType("application/json");
				out.println("{");
				out.println("\"CID\":" + rs.getInt("CID") + ",");
				out.println("\"ticker\":" + rs.getString("Ticker") + ",");
				out.println("\"companyname\":" + rs.getString("CompanyName") + ",");
				out.println("\"exchangecode\":" + rs.getString("ExchangeCode") + ",");
				out.println("\"startdate\":" + rs.getString("StartDate") + ",");
				out.println("\"description\":" + rs.getString("Description_"));
				out.println("}");
				out.flush();
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
					return;
				} catch (SQLException sqle) {
					System.out.println("sqle: " + sqle.getMessage());
				}
			}
		} catch(SQLException sqle) {
			System.out.println ("SQLException: " + sqle.getMessage());
		}
		try {
			// Fetch/construct company w/ tiingo query
			Comp company = new Comp(ticker_);
			
			// if company not currently tracked in DB, insert into DB
			query = "INSERT into Company(Ticker, CompanyName, ExchangeCode, StartDate, Description_)" +
							" values (?, ?, ?, ?, ?)";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, company.ticker);
				ps.setString(2, company.name);
				ps.setString(3, company.exchangeCode);
				ps.setString(4, company.startDate);
				ps.setString(5, company.description);
				ps.execute();		
				rs = ps.getGeneratedKeys();
				response.setContentType("application/json");
			query = "SELECT * from Company WHERE Ticker = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, "\"" + ticker_ + "\"");
			rs = ps.executeQuery();
			int CID = 0;
			if(rs.next()) {
				CID = rs.getInt("CID");
			}
				out.println("{");
				out.println("\"CID\":" + CID + ",");
				out.println("\"ticker\":" + company.ticker + ",");
				out.println("\"companyname\":" +  company.name + ",");
				out.println("\"exchangecode\":" + company.exchangeCode + ",");
				out.println("\"startdate\":" + company.startDate + ",");
				out.println("\"description\":" + company.description + "");
				out.println("}");
				out.flush();
				
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
