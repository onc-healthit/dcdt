
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Modular Spec Phase 3</title>
    </head>

<style type="text/css">

    #navigation{
     width:100%;
     height:30px;
     background-color:#A4A4A4;
    }
    
    #navigation ul{
    margin:0px; 
    padding:0px;
    }

    #navigation ul li{
    display:inline;
    height:30px; 
    float:left;
    list-style:none;
    }
    
    #navigation ul li{
    display:inline;
    height:30px;
    float:left;
    list-style:none;
    margin-left:15px;
    position:relative;
    }
    
    #navigation li a{
    color:#fff;
    text-decoration:none;
    }

    #navigation li a:hover{
    text-decoration:underline;
    position: relative;
    }
    
    #navigation li ul{
    margin:0px;
    padding:0px;
    display:none;
    position:absolute;
    left:0px; 
    top:30px;
    background-color:#58ACFA;
    }

    #navigation li:hover ul{
    display:block;
    width:220px;
    }

     #navigation li li{
     list-style:none;
     display:list-item;
     width:100%;
    }
    
    #navigation li li a{
    color:#fff;
    text-decoration:none;
    }

   #navigation li li a:hover{
   text-decoration:underline;
   }    
</style>

<body>
<table width="100%" border="0">
  <tr>
    <td colspan="2" style="background-color:#58ACFA;">
       <h1>Discover Testing Tool</h1>
   </td>
  </tr>
</table>

  <div id="navigation">
  <ul>
	<li><a href="welcome.jsp">Home </a></li>
	<li><a href="/steps.jsp>">Steps For Testing </a>
	<ul>
	<li><a href="">Check Your Local Certificate</a></li>
    </ul></li>         
	<li><a href="">Discovery </a>
	<ul>
	<li><a href="">Certificate Discovery for Direct</a></li>
	</ul></li>
	<li><a href="">Messaging </a></li>
	<li><a href="">Test Case Descriptions </a></li>
	<li><a href="">About Direct</a></li>
 </ul>      
</div>

<div id="content" >
  <br /><br />  
      <p> Please Select One: <br /> 
         <a href="dns.jsp"><strong>Certificate LookUp</strong></a><br />
         <a href="emailSet.jsp"><strong>Set Results Email</strong></a>           
      </p>
   </div>
 <br /> <br /> <br /> <br />
 <div id="footer" colspan="2" style="background-color:#58ACFA;text-align:center;">
Copyright © </div>





</body>
    
</html>
