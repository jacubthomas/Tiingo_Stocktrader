window.onload = function(){
	let username = localStorage.getItem("Username");
	if(username != null){
		document.getElementById("navigator").classList.add("hidden");
		document.getElementById("navigator_user").classList.remove("hidden");
		let btn1 = document.getElementById("user_hs_btn");
		let btn2 = document.getElementById("user_f_btn");
		let btn3 = document.getElementById("user_p_btn");
		let btn4 = document.getElementById("user_lo_btn");
		let curpage = window.location.pathname;
			btn4.classList.add("otherpage");
		if(curpage == "/jharring_CSCI201_Assignment4/index.html"){
			btn1.classList.add("currentpage");
			btn2.classList.add("otherpage");
			btn3.classList.add("otherpage");
			btn1.classList.remove("otherpage");
			btn2.classList.remove("currentpage");
			btn3.classList.remove("currentpage");
		} else if(curpage == "/jharring_CSCI201_Assignment4/favorites.html"){
			btn1.classList.add("otherpage")
			btn2.classList.add("currentpage");
			btn3.classList.add("otherpage");
			btn1.classList.remove("currentpage");
			btn2.classList.remove("otherpage");
			btn3.classList.remove("currentpage");
		} else if(curpage == "/jharring_CSCI201_Assignment4/portfolio.html"){
			btn1.classList.add("otherpage")
			btn2.classList.add("otherpage");
			btn3.classList.add("currentpage");
			btn1.classList.remove("currentpage");
			btn2.classList.remove("currentpage");
			btn3.classList.remove("otherpage");
		}
		
	} else {
		document.getElementById("navigator").classList.remove("hidden");
		document.getElementById("navigator_user").classList.add("hidden");
		let btn1 = document.getElementById("hs_btn");
		let btn2 = document.getElementById("ls_btn");
		let curpage = window.location.pathname;
		if(curpage == "/jharring_CSCI201_Assignment4/index.html"){
			btn1.classList.add("currentpage");
			btn2.classList.add("otherpage");
			btn1.classList.remove("otherpage");
			btn2.classList.remove("currentpage");
		} else if(curpage == "/jharring_CSCI201_Assignment4/loginsignup.html"){
			btn1.classList.add("otherpage")
			btn2.classList.add("currentpage");
			btn1.classList.remove("currentpage");
			btn2.classList.remove("otherpage");
		}
	}
}

function deleteCookies() {
    var cookies = document.cookie.split(";");
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}