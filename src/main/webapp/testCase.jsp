<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Discovery of your certificate</title>
        <script src="http://code.jquery.com/jquery-latest.min.js"></script>
        <script type="text/javascript">
        $(document).ready(function() {
            $('#form').submit(function() {
                $('#progress').show();
            });
        });
        </script>
        <style>
        #progress { 
            display: none;
            color: green; 
        }
    </style>  
        
      
    </head>
    <body>
    
        <h2>Choose a Test to Run</h2>
       <p> <select name="testid" id="testid" title="Select your Test">
        <option value="00"></option>
        <option value="500">DTS 500</option>
        <option value="501">DTS 501</option>
        <option value="502">DTS 502</option>           
        </select> </p>
        <br /><br/>
        <h2>Please Enter your direct email address based on the test case that you are running:</h2>
        <input type="text" name="emailAddress" id="emailAddress" maxlength="20" size="20" title="Enter your Email Address" /> 
        <br />
       <html:submit value="Run Test" title="Run the test" />

<div id="progress">Please wait...</div>
    </body>
</html>
