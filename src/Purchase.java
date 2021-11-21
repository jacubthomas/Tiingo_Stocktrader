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

@WebServlet("/purchase")
public class Purchase extends HttpServlet {

	private static final long serialVersionUID =8;

	@SuppressWarnings("resource")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("Username");
		String ticker = "\"" + request.getParameter("Ticker") + "\"";
		String quant = request.getParameter("Quantity");
		String cst = request.getParameter("Cost");
		int quantity = Integer.parseInt(quant);
		double cost = Double.parseDouble(cst);
		double total = cost * quantity;
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
		DecimalFormat df = new DecimalFormat("#.##");
		try {	
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Assignment4?user=root&password=root");
			String query = "SELECT UID, Balance, CID from User, Company WHERE Username = ? AND Ticker = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, username);
			ps.setString(2, ticker);
			rs = ps.executeQuery();	
			int UID = -1;
			int CID = -1;
			double balance = -1;
			if(rs.next()) {
				UID =  rs.getInt("UID");
				CID =  rs.getInt("CID");
				balance = rs.getDouble("Balance");
				cost = Double.parseDouble(df.format(cost));
			}
			if(balance >= total) {
				query = "INSERT INTO Transactions (UID, CID, Quantity, Cost)" +
						" values(?, ?, ?, ?)";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, UID);
				ps.setInt(2, CID);
				ps.setInt(3, quantity);
				ps.setDouble(4, cost);
				ps.execute();		
				rs = ps.getGeneratedKeys();
				out.println("Successful purchase of " +  quantity + " stocks from " + ticker +
						" at " + cost + " for a grand total of " + total);
				out.flush();
				out.close();
				
				double remaining_balance = balance - total;
				remaining_balance = Double.parseDouble(df.format(remaining_balance));
				query = "UPDATE User SET Balance = ? WHERE UID = ?";
				ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ps.setDouble(1, remaining_balance);
				ps.setInt(2, UID);
				ps.execute();		
				rs = ps.getGeneratedKeys();
			}
			else {
				out.println("Not enough balance.");
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
