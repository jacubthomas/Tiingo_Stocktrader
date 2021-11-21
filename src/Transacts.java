import java.text.DecimalFormat;
import java.util.ArrayList;

public class Transacts {
	public int UID;
	public int TID;
	public int CID;
	public int quantity;
	public Double change;
	public Double average;
	public Double current;
	public Double total;
	public Double market;
	public String ticker;
	public String company;
	public boolean isGreen = false;
	public ArrayList<Double> costs = new ArrayList<Double>();
	public ArrayList<Integer> quants = new ArrayList<Integer>();
	public Transacts(int UID, int CID, int quantity, double cost) {
		this.CID = CID;
		quants.add(quantity);
		costs.add(cost);
	}
	public Transacts(int CID, int TID, int quantity) {
		this.CID = CID;
		this.TID = TID;
		this.quantity = quantity;
	}
	
	public void update1() {
		int temp_quantity = 0;
		double temp_total = 0;
		for(int i=0; i<costs.size(); i++) {
			temp_quantity += quants.get(i);
			temp_total += costs.get(i) * quants.get(i);
		}
		DecimalFormat df = new DecimalFormat("#.##"); 	
		quantity = temp_quantity;
		total = temp_total;
		total =  Double.parseDouble(df.format(total));
		average = total / quantity;
		average = Double.parseDouble(df.format(average));
	}
	public void update2() {
		Stock stock = new Stock(ticker);
		DecimalFormat df = new DecimalFormat("#.##"); 	
		current = stock.last;
		current = Double.parseDouble(df.format(current));
		change = average - current;
		change = Double.parseDouble(df.format(change));
		market = current * quantity;
		market = Double.parseDouble(df.format(market));
		if(change >= 0 )
			isGreen = true;
	}
}
