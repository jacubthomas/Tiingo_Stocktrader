import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


@WebServlet("/portfolio")
public class Portfolio extends HttpServlet {

	private static final long serialVersionUID = 8;

	@SuppressWarnings("resource")
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
			String query = "SELECT * from Transactions WHERE UID = ?";
			ps = conn.prepareStatement(query);
			ps.setInt(1, UID);
			rs = ps.executeQuery();
			ArrayList<Integer> CIDs = new ArrayList<Integer>();
			ArrayList<Transacts> transacts = new ArrayList<Transacts>();
			while(rs.next()) {
				int cid = rs.getInt("CID");
				if(!CIDs.contains(cid)){
					Transacts transact = new Transacts(UID, cid, rs.getInt("Quantity"), rs.getDouble("Cost"));
					transact.update1();
					transacts.add(transact);
					CIDs.add(cid);
				} else {
					Transacts transact;
					for(int i=0; i<transacts.size(); i++) {
						if(transacts.get(i).CID == cid ) {
							transact = transacts.get(i);
							transact.costs.add(rs.getDouble("Cost"));
							transact.quants.add(rs.getInt("Quantity"));
							transact.update1();
						}
					}
				}
			}
			int count = 0;
			while(count < transacts.size()) {
				Transacts transact = transacts.get(count);
				query = "SELECT Ticker, CompanyName FROM Company WHERE CID = ?";
				ps = conn.prepareStatement(query);
				ps.setInt(1, transact.CID);
				rs = ps.executeQuery();		
				if(rs.next()) {
					transact.company = rs.getString("CompanyName");
					transact.ticker = rs.getString("Ticker");
					transact.ticker = transact.ticker.substring(1, transact.ticker.length()-1);
					transact.company = transact.company.substring(1, transact.company.length()-1);
					transact.update2();
					count++;
					System.err.println(transact.ticker);
				}
			}
			response.setContentType("application/json");
			Gson gson = new Gson();
			String reply = gson.toJson(transacts);
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
