function login(){
    var username = document.getElementById('lgn_user').value;
	var password = document.getElementById('lgn_pass').value;
    if(!(username && password)){
		alert("Please fill out all fields.")
		return
	}

    fetch('http://localhost:8080/jharring_CSCI201_Assignment4/login?' + new URLSearchParams({
		Username: username,
		Password: password
	}), {
		method: "GET"
	})
    .then(response => response.text())
    .then(response => {
        if(response === "Invalid Login"){
            alert(response)
        }else{
            var userData = JSON.parse(response) 
            localStorage.setItem("Username", userData.Username);
            localStorage.setItem("UID", userData.UID);
            window.location.href = "index.html";
        }
	})
}

function googleLogin(id, fullname, first, last, email){
    if(id == null || fullname == null || first == null || last == null || email == null){
		alert("Google login failed");
		return;
	}

    fetch('http://localhost:8080/jharring_CSCI201_Assignment4/register?' + new URLSearchParams({
		User: email,
		Pass: 12345678,
		Email: email
	}), {
		method: "GET"
	})
	    .then(response => response.text())
	    .then(response => {
	        if(response === "Username Exists"){
	           
	           
	           
	       fetch('http://localhost:8080/jharring_CSCI201_Assignment4/login?' + new URLSearchParams({
				Username: email,
				Password: 12345678
			}), {
				method: "GET"
			})
		    .then(response => response.text())
		    .then(response => {
			    var userData = JSON.parse(response);
	            localStorage.setItem("Username", userData.Username);
	            localStorage.setItem("UID", userData.UID);
	            window.location.href = "index.html";
		    })
	    }
	})
}

  function onSignIn(googleUser) {
    // Useful data for your client-side scripts:
    var profile = googleUser.getBasicProfile();
    googleLogin(profile.getId(), profile.getName(), profile.getGivenName(), profile.getFamilyName(), profile.getEmail());

    // The ID token you need to pass to your backend:
    var id_token = googleUser.getAuthResponse().id_token;
  }
  

function start() {
      gapi.load('auth2', function() {
        auth2 = gapi.auth2.init({
          client_id: '39988113297-qv68edch43s9kcle9dh71agf921flb8s.apps.googleusercontent.com',
          // Scopes to request in addition to 'profile' and 'email'
          //scope: 'additional_scope'
        });
      });
    }
