import java.text.DecimalFormat;

public class Fave {
	public int CID;
	public Double prev_close;
	public Double last;
	public Double change;
	public Double change_percent;
	public String ticker; 
	public String name;
	public boolean isGreen;
	public Fave(int CID, Double prev_close, Double last, String ticker, String name) {
		this.CID = CID;
		this.prev_close = prev_close;
		this.last = last;
		this.ticker = ticker;
		this.name = name;
		DecimalFormat df = new DecimalFormat("#.##"); 	
		change = last - prev_close;
		change = Double.parseDouble(df.format(change));
		change_percent = (change*100)/prev_close;
		change_percent = Double.parseDouble(df.format(change_percent));
		if(change_percent < 0)
			isGreen = false;
		else
			isGreen = true;
	}
}
