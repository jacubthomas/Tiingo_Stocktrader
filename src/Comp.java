import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class Comp {

	public String name;
	public String ticker;
	public String description;
	public String startDate;
	public String exchangeCode;
	public int CID;
	public static final long serialVersionUID = 3;
	public Comp(String ticker) {
		String request = Tiingo.searchCompany(ticker);
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonElement jE = gson.fromJson(request, JsonElement.class);
			JsonObject jO = jE.getAsJsonObject();
			this.name = jO.get("name").toString();
			this.ticker = jO.get("ticker").toString();
			this.description = jO.get("description").toString();
			this.startDate =  jO.get("startDate").toString();
			this.exchangeCode = jO.get("exchangeCode").toString();
		} catch(JsonParseException jpe) {
			System.out.println(jpe.getMessage());
		}
	}
	public Comp(String ticker, String name, int CID) {
		this.ticker = ticker;
		this.name = name;
		this.CID = CID;
		this.startDate = null;
		this.exchangeCode = null;
	}
}
