function favoritehover(){
	let star = document.getElementById("star");
	star.classList.remove("fa-star-o");
	star.classList.add("fa-star");
	star.style.color="#6100FF";
}
function favoriteleave(){
	let star = document.getElementById("star");
	star.classList.remove("fa-star");
	star.classList.add("fa-star-o");
	star.style.color="black";
}

function searchComp(){
	var ticker = document.getElementById("searchticker").value;
	if(ticker == null){
		alert("Please enter a ticker")
		return
	}
	let tick = document.getElementById("result_ticker");
	tick.innerText="";
	let name = document.getElementById("result_name");
	name.innerText="";
	let exchange = document.getElementById("result_exchange");
	exchange.innerText="";
	let start = document.getElementById("result_start");
	start.innerText="";
	let descript =  document.getElementById("result_description");
	descript.innerText="";
	let high = document.getElementById("request_high");
	high.innerText="";
	let mid = document.getElementById("request_mid");
	mid.innerText="";
	let low = document.getElementById("request_low");
	low.innerText="";
	let ap = document.getElementById("request_ap");
	ap.innerText="";
	let op =  document.getElementById("request_op");
	op.innerText="";
	let as = document.getElementById("request_as");
	as.innerText="";
	let pc = document.getElementById("request_pc");
	pc.innerText="";
	let bp = document.getElementById("request_bp");
	bp.innerText="";
	let volume = document.getElementById("request_volume");
	volume.innerText="";
	let bs = document.getElementById("request_bs");
	bs.innerText="";
	let lp = document.getElementById("request_last");
	lp.innerText="";
	let chng = document.getElementById("request_change");
	chng.innerText="";
	let cur_ts = document.getElementById("current_timestamp");
	cur_ts.innerText="";
	let market = document.getElementById("market");
	market.innerText="";
	
	 fetch('http://localhost:8080/jharring_CSCI201_Assignment4/company?' + new URLSearchParams({
		Ticker: ticker
	}), {
		method: "GET"
	})
    .then(response => response.text())
    .then(response => {
        if(response === "Invalid ticker"){
            alert(response)
        }else{
            var compData = JSON.parse(response);
			document.getElementById("result_contain").classList.remove("hidden");
			
			txt = document.createTextNode(compData.ticker);
			tick.appendChild(txt);
			
			txt = document.createTextNode(compData.companyname);
			name.appendChild(txt);
			
			txt =  document.createTextNode(compData.exchangecode);
			exchange.appendChild(txt);
			
			txt = document.createTextNode("Start date: " + compData.startdate);
			start.appendChild(txt);
			
			txt = document.createTextNode(compData.description);
			descript.appendChild(txt);
			
		fetch('http://localhost:8080/jharring_CSCI201_Assignment4/lateststock?' + new URLSearchParams({
		Ticker: ticker,
		CID: compData.CID
	}), {
		method: "GET"
	})
    .then(response => response.text())
    .then(response => {
        if(response === "Invalid ticker"){
            alert(response)
        }else{
            var latestData = JSON.parse(response)
			
			txt = document.createTextNode(latestData.high_price);
			high.appendChild(txt);
			
			txt =  document.createTextNode(latestData.low_price);
			low.appendChild(txt);
		
			txt = document.createTextNode(latestData.open);
			op.appendChild(txt);
			
			txt = document.createTextNode(latestData.prev_close);
			pc.appendChild(txt);
			
			txt = document.createTextNode(latestData.volume);
			volume.appendChild(txt);
			
			if(localStorage.Username != null){
				document.getElementById("star").style="display:inline;";
				document.getElementById("quant_buy").style="display:inline;";
				document.getElementById("mark").style="display:inline;";
				txt = document.createTextNode(latestData.last);
				lp.appendChild(txt);
				
				txt = document.createTextNode(latestData.change +" (");
					chng.appendChild(txt);
					
					if(latestData.change > 0){
						document.getElementById("request_last").style = "color:darkgreen; font-size:x-large; font-weight:700;float:right;";
						document.getElementById("request_change").style = "color:darkgreen; font-size:large; font-weight:500;float:right;";
					} else {
						document.getElementById("request_last").style = "color:red; font-size:x-large; font-weight:700;float:right;";
						document.getElementById("request_change").style = "color:red; font-size:large; font-weight:500;float:right";
					}
					
				txt = document.createTextNode(latestData.change_percent + ")%");
				chng.appendChild(txt);
				
				// ------------------------------------------------
				
				
				if(latestData.market_status != "Closed"){
				
					let right = document.getElementsByClassName("right_prices");		
					for(let i=0; i<right.length; i++){
						right[i].style="display:inline;";
					}
					txt =  document.createTextNode(latestData.bid_size);
					bs.appendChild(txt);
					
					
					txt = document.createTextNode(latestData.mid_price);
					mid.appendChild(txt);
					
					txt = document.createTextNode(latestData.ask_price);
					ap.appendChild(txt);
					
					txt =  document.createTextNode(latestData.bid_price);
					bp.appendChild(txt);
					
					txt = document.createTextNode(latestData.ask_size);
					as.appendChild(txt);
					
					txt = document.createTextNode(" is " + latestData.market_status);
					market.appendChild(txt);
				} else {
				
				txt = document.createTextNode(" " + latestData.market_status + " on " + latestData.date_timestamp);
				market.appendChild(txt);
				
				txt = document.createTextNode(latestData.current_timestamp);
				cur_ts.appendChild(txt);
				
				document.getElementById("buy_btn").onclick="purchaseBlock()";
				}
			} else {
				document.getElementById("quant_buy").style="display:none;";	
				document.getElementById("mark").style="display:none;";	
				document.getElementById("star").style="display:none;";	
				let right = document.getElementsByClassName("right_prices");		
				for(let i=0; i<right.length; i++){
					right[i].style="display:none;";
				}
			}
        }
	})
        }
	})
}

function favorited(){

	let username = localStorage.getItem("Username");
	let ticker = document.getElementById("searchticker").value;
	
	if(username != null){
		fetch('http://localhost:8080/jharring_CSCI201_Assignment4/favorite?' + new URLSearchParams({
					Username: username,
					Ticker: ticker
			}), {
				method: "GET"
			})
		    .then(response => response.text())
		    .then(response => {
			})
	} else {
		alert("You must be logged in to favorite a ticker.")
	}
}

function purchase(){
	let username = localStorage.getItem("Username");
	let ticker = document.getElementById("result_ticker").innerText;
	let quantity = document.getElementById("buy_quantity").value;
	let cost = document.getElementById("request_last").innerText;
	fetch('http://localhost:8080/jharring_CSCI201_Assignment4/purchase?' + new URLSearchParams({
					Username: username,
					Ticker: ticker,
					Quantity: quantity,
					Cost: cost
			}), {
				method: "GET"
			})
		    .then(response => response.text())
		    .then(response => {
            	alert(response)
        	})

}
function purchaseBlock(){
	alert("Market is currently closed. Try again later.");
}
