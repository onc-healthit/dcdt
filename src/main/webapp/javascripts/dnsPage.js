//Function to load the button
$(document).ready(function() {
	$('#submit').click(function() {
	    var btn = $(this);
	    btn.button('loading'); // call the loading function
	    setTimeout(function() {
	        btn.button('reset'); // call the reset function
	    }, 3000);
	}); // End of the Function


//Function to select the drop down box
	$("#submit").click(function(){
	var selTest
	selTest = $('#testcase option:selected').text();
	document.getElementById("seldropDown").value = selTest;
	//alert("here is your value" +selTest)
	});// End of Function
	
});// End of page load ready

// Function to set the drop down values
var val
	function setText(select_ele){
//	this.form.prob.value=this.options[this.selectedIndex].innerHTML;this.form.solu.value=this.options[this.selectedIndex].value
		document.getElementById("comments").innerHTML = select_ele.options[select_ele.selectedIndex].value;
		val = select_ele.options[select_ele.selectedIndex].value;
		

if (val == 1) {			
  document.getElementById("comments").innerHTML = "<b>Purpose/ Description:</b>" + "<br /> " +
	 " System's DNS correctly stores and returns address-bound X.509 certificate. " + " <br /> " + "<b>Instructions:</b> " + " <br /> " + " For this test case, enter in your Direct Address. If there is an address-bound certificate associated with your Direct Address, the tool will discover it and print the result to the screen."
	 + " Also run this test case " +
		 		"using a mix of upper and lower case characters in order to verify your system can handle queries in different cases. This equates to Test Case DTS573."  + 
	 "<br /> " +
	 "<b>RTM:</b> " +"<br />" +"1,3." +"<br />" +" <b>Underlying Specification Reference:</b> " +"<br />" +"RFC 4398: Section 2.1 - Direct Applicability Statement for Secure Health Transport: Section 5.3 .";		
}else if (val == 2) {
  document.getElementById("comments").innerHTML =
	  "<b>Purpose/ Description:</b>" + "<br /> " +
		 "System's DNS correctly stores and returns domain-bound X.509 certificate. " + "<br /> " + "<b>Instructions:</b> " +" <br /> " + " For this test case, enter in your Direct Address. If there is a domain-bound certificate associated with your domain, the tool will discover it and print the result to the screen." +
		 		" Also run this test case " +
		 		"using a mix of upper and lower case characters in order to verify your system can handle queries in different cases. This equates to Test Case DTS573. " + "<br />" + "<b>RTM:</b> " + "<br />" +" 1,3. " +"<br />" +" <b>Underlying Specification Reference:</b> " +"<br />" + 
		 "RFC 4398: Section 2.1 - Direct Applicability Statement for Secure Health Transport: Section 5.3";
}
else if (val == 3){
  document.getElementById("comments").innerHTML =	
	  "<b>Purpose/ Description:</b>" + "<br /> " +
		 "System's DNS can respond CERT queries that are in different cases." + "<br /> " + "<b>Instructions:</b> " +" <br /> " + " For this test case, enter in your Direct Address (for an address-bound certificate) or an an address within your domain (for a domain-bound certificate), but make sure the cases do not match the " +
		 "configured DNS CERT resource record cases. For exmample, if your DNS entry is all lowercase characters, then enter your Direct email address in a mix of upper and lowercase. The response from the Server may take up to 15 seconds, so please be patient." + "<br />" +
		 " <b>RTM:</b> " +"<br />" +" 1,3 " +"<br />" +" <b>Underlying Specification Reference:</b> " +"<br />" +
		 " - RFC 4398: Section 2.1 - Direct Applicability Statement for Secure Health Transport: Section 5.3" ;

}
else if(val ==4 ){
  document.getElementById("comments").innerHTML =
	  "<b>Purpose/ Description:</b>" + "<br /> " +
		 "System returns address-bound X.509 certificate from LDAP server. " + "<br /> " + "<b>Instructions:</b> " +" <br /> " + " For this test case, enter in your Direct Address. " +
		 		"If there is a certificate stored in an LDAP server associated with your domain, the System will discover it and print it to the screen. Also run this test case " +
		 		"using a mix of upper and lower case characters in order to verify your system can handle queries in different cases. This equates to Test Case DTS557. " +
		 		 "<br /> " +"<b>RTM:</b> " +"<br />" +" 2,3,5,7,8,9,10,11,12,13,14,15,16,17,19,20,21,22. " +"<br />" +" <b>Underlying Specification Reference:</b> " +"<br />" +
		 "RFC 2798: Section 9.1.2 ";
}
else if(val ==5 ){
	  document.getElementById("comments").innerHTML =
		  "<b>Purpose/ Description:</b>" + "<br /> " +
			 "System's LDAP server responds correctly when cases do not match. " + "<br /> " + "<b>Instructions:</b> " +" <br /> " + " For this test case, enter in your Direct Address, but make sure the cases do not match the 'mail'" +
			 "LDAP attribute. For example, if my 'mail' attribute is 'drwho@onctest.org', enter in 'DRWHO@ONCTEST.ORG'. The response from the Server may take up to 15 seconds, so please be patient." +"<br />" +
			 " <b>RTM:</b> " +"<br />" +" 2,3,5,6,7,8,9,10,11,12,13,14,15,16,17,19,20,21,22. " +"<br />" +" <b>Underlying Specification Reference:</b> " +"<br />" +
			 " RFC 2798: Section 9.1.3" ;
	}
else if(val ==6 ){
	  document.getElementById("comments").innerHTML =
		  "<b>Purpose/ Description:</b>" + "<br /> " +
			 "System returns domain-bound X.509 certificate from LDAP server. " +" <br /> " + "<b>Instructions:</b> " +" <br /> " + " For this test case, enter in your Direct Address. If there is a domain-bound certificate associated with your domain stored in an LDAP server associated to your domain, the tool will discover it and print the result to the screen." +
			 		" Also run this test case " +
		 		"using a mix of upper and lower case characters in order to verify your system can handle queries in different cases. This equates to Test Case DTS557. " + "<br /> " +"<b>RTM:</b> " +"<br />" +" 2,3,5,7,8,9,10,11,12,13,14,15,16,17,19,20,21,22. " +"<br />" +" <b>Underlying Specification Reference:</b> " +"<br />" +
			 "RFC 4515: Section 3";
	}
else if(val ==7 ){
	  document.getElementById("comments").innerHTML =
		  "<b>Purpose/ Description:</b>" + "<br /> " +
			 "System's LDAP rejects a mail attribute that they don't have. " + "<br /> " + "<b>Instructions:</b> " +" <br /> " + " For this test case, enter a Direct Address not on your LDAP Server. The response from the Server may take up to "+
			 "15 seconds, so please be patient." +"<br />" +
			 " <b>RTM:</b> " +"<br />" +" 2,3,5,6,7,8,9,10,11,12,13,14,15,16,17,19,20,21,22. " +"<br />" +" <b>Underlying Specification Reference:</b> " +"<br />" +
			 " RFC 4511: Section 4.5.2- RFC 4511: Appendix A.2";
	}
	
	}//end of if	

//	function submitPatiently() {  
//		var control1 = document.getElementById('waitDiv')
//		//To display the control
//		control1.style.display = 'block'
//		 
//		   submit();  
//		}  

	