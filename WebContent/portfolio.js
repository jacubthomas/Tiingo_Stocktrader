let UID = localStorage.getItem("UID");

function port_transaction(i){
	let radb = document.getElementById("rad_b_"+i);
	let rads = document.getElementById("rad_s_"+i);
	if(radb.checked){
		fetch('http://localhost:8080/jharring_CSCI201_Assignment4/purchase?' + new URLSearchParams({
			Username: localStorage.getItem("Username"),
			Ticker : document.getElementById("ticker"+i).innerText,
		 	Quantity: document.getElementById("tran_quant"+i).value,
			Cost: document.getElementById("last"+i).innerText
		}), {
			method: "GET"
		})
		.then(response => response.text())
		.then(response => {
			window.location.href='portfolio.html';
		})
	}
	else if(rads.checked){
		fetch('http://localhost:8080/jharring_CSCI201_Assignment4/sale?' + new URLSearchParams({
			UID: UID,
			Ticker: document.getElementById("ticker"+i).innerText,
			Quantity: document.getElementById("tran_quant"+i).value
		}), {
			method: "GET"
		})
		    .then(response => response.text())
		    .then(response => {
		    window.location.href='portfolio.html';
		})
	}
}

fetch('http://localhost:8080/jharring_CSCI201_Assignment4/balance?' + new URLSearchParams({
			UID: UID
	}), {
		method: "GET"
	})
    .then(response => response.text())
    .then(response => {
		let userFinance = JSON.parse(response);
		document.getElementById("cb").innerText = userFinance.Balance;
		document.getElementById("tav").innerText = userFinance.AccountValue;
	})
	
fetch('http://localhost:8080/jharring_CSCI201_Assignment4/portfolio?' + new URLSearchParams({
			UID: UID
	}), {
		method: "GET"
	})
    .then(response => response.json())
    .then(response => {
        let divvy = document.getElementById("user_portfolio");
        for(var i=0; i<response.length; i++){
				 divvy.innerHTML += 
					"<table class=\"table is-fullwidth is-bordered is-striped\">" +
						"<thead>" +
							"<tr>" +
								"<th>" +
									"<span id=\"ticker" + i + "\" style=\"font-size:Large; font-weight:600; margin-right:10px;\">" + response[i].ticker+ "</span>" +
									"<span style=\"color:grey;\">" + response[i].company + "<span>" + 
								"</th>" +
							"</tr>" +
						"</thead>" +
						"<tbody> " +
							"<tr>" +
								"<td>" +
									"<div class=\"columns\">" +
										"<div class=\"column is-one-fourth\">" +
										 	"<strong style=\"float:left\">" +
										 	 	"Quantity:" +
									 	 	"</strong>" +
									 	 	"<span id=\"quant" + i + "\" style=\"float:right; margin-right:20px;\">" +
									 	 		response[i].quantity +
											"</span>" +
										"</div>" +
										"<div class=\"column is-one-fourth\">" +
											"<strong style=\"float:left;\">" +
												"Change:" +
											"</strong>" +
											"<span style=\"float:right; margin-right:20px;\">" +
												response[i].change +
											"</span>" +
										"</div>" +
									"</div>" +
									"<div class=\"columns\">" +
										"<div class=\"column is-one-fourth\">" +
											"<strong style=\"float:left;\">" +
												"Avg. Cost / Share:" +
											"</strong>" +
											"<span style=\"float:right; margin-right:20px;\">" +
												response[i].average +
											"</span>" +
										"</div>" +
										"<div class=\"column is-one-fourth\">" +
											"<strong style=\"float:left;\">" +
												"Current Price:" +
											"</strong>" +
											"<span id=\"last" + i + "\" style=\"float:right; margin-right:20px;\">" +
												response[i].current +
											"</span>" +
										"</div>" +
									"</div>" +
									"<div class=\"columns\">" +
										"<div class=\"column is-one-fourth\">" +
											"<strong style=\"float:left;\">" +
												"Total Cost:" +
											"</strong>" +
											"<span style=\"float:right; margin-right:20px;\">" +
												response[i].total +
											"</span>" +
										"</div>" +
										"<div class=\"column is-one-fourth\">" +
											"<strong style=\"float:left;\">" +
												"Market Value:" +
											"</strong>" +
											"<span style=\"float:right; margin-right:20px;\"> " +
												response[i].market +
											"</span>" +
										"</div>" +
									"</div>" +
								"</td>" +
							"</tr>" +
						"</tbody>" +
						"<tfoot>" + 
							"<tr class=\"override\">" +
								"<td class=\"box has-text-centered\" style=\"box-shadow:none; background-color:#F0F0F0; border-radius: 0px;\"> " +
									"<div class=\"columns\">" +
										"<div class=\"column\"></div>" +
										"<div class=\"column\">" +
											"Quantity" +
											 "<input type=\"number\" id=\"tran_quant" + i + "\" name=\"quantity\" min=\"1\" max=\""+ response[i].quantity + "\">" +
										"</div>" +
										"<div class=\"column\"></div>" +
									"</div>" +
									"<div class=\"columns\">" +
										"<div class=\"column\"></div>" +
										"<div class=\"column is-4 control form-group\">" +
											"<label class=\"radio\">" +
												"<input id=\"rad_b_" + i + "\" type=\"radio\" name=\"radio\" value=\"buy\">" +
													"Buy" +
											"</label>" +
											"<label class=\"radio\">" +
										   		"<input id=\"rad_s_" + i + "\" type=\"radio\" name=\"radio\" value=\"sell\">" +
											    	"Sell" +
											  "</label>" +
										"</div>" +	
										"<div class=\"column\"></div>" +
									"</div>" +
									"<div class=\"columns\">" +
										"<div class=\"column\"></div>" +
										"<div class=\"column\">"+
											"<button id=\"submit_btn_" + i + "\" type=\"button\" class=\"is-light\">Submit</button>" +
										"</div>" +
										"<div class=\"column\"></div>" +
									"</div>" +
								"</td>" + 
							"</tr>" +
						"</tfoot>" +
					"</table>";		
			}
			 for(var i=0; i<response.length; i++){
			 	let index = i;
				let temp = document.getElementById("submit_btn_"+index);		
				temp.addEventListener("click", function(){ port_transaction(index)});
			}
		})

