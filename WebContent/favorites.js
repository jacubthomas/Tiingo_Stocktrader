let username = localStorage.getItem("Username");

fetch('http://localhost:8080/jharring_CSCI201_Assignment4/loadfavorites?' + new URLSearchParams({
			Username: username
	}), {
		method: "GET"
	})
    .then(response => response.text())
    .then(response => {
		var faveData = JSON.parse(response);
		let divvy = document.getElementById("user_faves");
		for(var i=0; i<faveData.length; i++){
			if(faveData[i].isGreen){
				 divvy.innerHTML += 
					"<div class=\"columns\" style=\"margin-bottom:0;\">" +
						"<div class=\"column is-centered favorites\">" +
						"<span style=\"font-size:xlarge; font-weight:700;float:left;\">" + faveData[i].ticker + "</span>"+
						"<span style=\"font-size:xlarge; font-weight:700;float:right; color:darkgreen;\">" +faveData[i].last + "</span>" +
						"</div>" +
					"</div>" +
					"<div class=\"columns\">" +
						"<div class=\"column is-centered favorites\">" +
							"<span style=\"font-weight:400;float:left;\">" + faveData[i].name + "</span>" +
							"<span style=\"font-weight:400;float:right; color:darkgreen;\">" + 
											faveData[i].change + "(" + faveData[i].change_percent + ")%</span>" +
						"</div>" +
					"</div>";
				} else {
					 divvy.innerHTML +=
					"<div class=\"columns\" style=\"margin-bottom:0;\">" +
						"<div class=\"column is-centered favorites\">" +
						"<span style=\"font-size:xlarge; font-weight:700;float:left;\">" + faveData[i].ticker + "</span>"+
						"<span style=\"font-size:xlarge; font-weight:700;float:right; color:red;\">" +faveData[i].last + "</span>" +
						"</div>" +
					"</div>" +
					"<div class=\"columns\">" +
						"<div class=\"column is-centered favorites\">" +
							"<span style=\"font-weight:400;float:left;\">" + faveData[i].name + "</span>" +
							"<span style=\"font-weight:400;float:right; color:red;\">" + 
											faveData[i].change + "(" + faveData[i].change_percent + ")%</span>" +
						"</div>" +
					"</div>";
				}
				if(i < faveData.length-1)
					divvy.innerHTML += "<hr style=\"margin-top:1%; margin-bottom:1%;\">"
			}
	})


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