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

@WebServlet("/sale")
public class Sale extends HttpServlet {

	private static final long serialVersionUID = 9;

	@SuppressWarnings("resource")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uid = request.getParameter("UID");
		int UID = Integer.parseInt(uid);
		String ticker = "\"" + request.getParameter("Ticker") + "\"";
		String quant = request.getParameter("Quantity");
		int quantity = Integer.parseInt(quant);
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
			String query = "SELECT CID from Company WHERE Ticker = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, ticker);
			rs = ps.executeQuery();	
			int CID = -1;
			if(rs.next()) {
				CID =  rs.getInt("CID");
			}
			query = "SELECT * from Transactions WHERE UID = ? AND CID = ?";
			ps = conn.prepareStatement(query);
			ps.setInt(1, UID);
			ps.setInt(2, CID);
			rs = ps.executeQuery();
			ArrayList<Transacts> transacts = new ArrayList<Transacts>();
			ArrayList<Double> profits = new ArrayList<Double>();
			if(rs.next()) {
				transacts.add(new Transacts(CID, rs.getInt("TID"), rs.getInt("Quantity")));
			}
			ticker = ticker.substring(1, ticker.length()-1);
			System.err.println(ticker);
			Stock stock = new Stock(ticker);
			int tosell = quantity;
			int index = 0;
			while(tosell > 0 && index < transacts.size()) {
				Transacts transact = transacts.get(index);
				System.out.println("CID:  " + CID + ", transacts.CID: "+ transact.CID);
				if(transact.CID == CID) {
					int quaint  = transact.quantity;
					if(quaint > tosell) {
						System.out.println("Case 1: q = " + quaint + " > " + tosell);
						query = "UPDATE Transactions SET Quantity = ? WHERE TID = ?";
						ps = conn.prepareStatement(query);
						int temp = quaint - tosell;
						ps.setInt(1, temp);
						ps.setInt(2, transact.TID);
						ps.execute();
						if(stock.bid_price > 0)
							profits.add(stock.bid_price * tosell);
						else
							profits.add(stock.last * tosell);
						tosell = 0;
					} else {
						query = "DELETE FROM Transactions WHERE TID = ?";
						ps = conn.prepareStatement(query);
						ps.setInt(1, transact.TID);
						ps.execute();
						if(stock.bid_price > 0)
							profits.add(stock.bid_price * tosell);
						else
							profits.add(stock.last * tosell);
						tosell -= quaint;
					}
				}
				index++;
			}
			double account_value = 0;
			query = "SELECT AccountValue FROM User WHERE UID = ?";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, UID);
			rs = ps.executeQuery();		
			if(rs.next()) {
				account_value = rs.getDouble("AccountValue");
			}
			while(!profits.isEmpty()) {
				account_value += profits.get(0);
				profits.remove(0);
			}
			account_value = Double.parseDouble(df.format(account_value));
			query = "UPDATE User SET AccountValue = ? WHERE UID = ?";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setDouble(1, account_value);
			ps.setInt(2, UID);
			ps.execute();	
		} catch (SQLException sqle) {
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
//			
//			String query = "SELECT * from Transactions WHERE UID = ? AND CID = ?";
//			ps = conn.prepareStatement(query);
//			ps.setInt(1, UID);
//			ps.setString(2, ticker);
//			rs = ps.executeQuery();	
//			int UID = -1;
//			int CID = -1;
//			double balance = -1;
//			if(rs.next()) {
//				UID =  rs.getInt("UID");
//				CID =  rs.getInt("CID");
//				balance = rs.getDouble("Balance");
//			}
	}
	

}
