import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class Stock {
	public Double prev_close;
	public Double mid_price;
	public Double ask_price;
	public Double ask_size;
	public Double bid_price;
	public Double bid_size;
	public Double open;
	public Double close;
	public Double high_price;
	public Double low_price;
	public Double last;
	public int volume;
	public String date_timestamp;
	public Stock(String ticker) {
		String request = Tiingo.searchLatest(ticker);
		try {
			request = request.substring(1, request.length()-1);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonElement jE = gson.fromJson(request, JsonElement.class);
			JsonObject jO = jE.getAsJsonObject();
			if(jO.get("prevClose").isJsonNull())
				this.prev_close = -1.0;
			else
				this.prev_close = Double.parseDouble(jO.get("prevClose").toString());
			if(jO.get("mid").isJsonNull())
				this.mid_price = -1.0;
			else
				this.mid_price = Double.parseDouble(jO.get("mid").toString());
			if(jO.get("askPrice").isJsonNull())
				this.ask_price = -1.0;
			else
				this.ask_price = Double.parseDouble(jO.get("askPrice").toString());
			if(jO.get("askSize").isJsonNull())
				this.ask_size = -1.0;
			else
				this.ask_size =  Double.parseDouble(jO.get("askSize").toString());
			if(jO.get("bidPrice").isJsonNull())
				this.bid_price = -1.0;
			else
				this.bid_price = Double.parseDouble(jO.get("bidPrice").toString());
			if(jO.get("bidSize").isJsonNull())
				this.bid_size = -1.0;
			else
				this.bid_size = Double.parseDouble(jO.get("bidSize").toString());
			this.open = Double.parseDouble(jO.get("open").toString());
			this.high_price = Double.parseDouble(jO.get("high").toString());
			this.low_price = Double.parseDouble(jO.get("low").toString());
			this.last = Double.parseDouble(jO.get("last").toString());
			this.volume = Integer.parseInt(jO.get("volume").toString());
			this.date_timestamp = jO.get("timestamp").toString();
		} catch(JsonParseException jpe) {
			System.out.println(jpe.getMessage());
		}
	}
	public Stock(Double prev_close, Double last) {
		this.prev_close = prev_close;
		this.last = last;
	}

}
