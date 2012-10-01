<%@ page session="false"%>
<jsp:include page="ajaxCall.jsp" flush="true">
  	<jsp:param name="title"
		value="Home Page" />
	<jsp:param name="pgDesc"
		value="Direct Certificate Discovery Testing Tool - Header" />
	<jsp:param name="pgKey"
		value="Direct Certificate Discovery Testing Tool - Header" />
	<jsp:param name="header" value="" />
</jsp:include>

<head>
<meta name="ROBOTS" content="none,noindex,nofollow" />
<meta http-equiv="Content-Language" content="en-US" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta http-equiv="Expires" content="Sat, 01 Jan 2000 23:59:00 GMT" />
<meta http-equiv="PRAGMA" content="NO-CACHE" />
<title>Direct Certificate Discovery Tool</title>
<!--  <link rel="stylesheet" href="/ModularSpecPhase3_Tool/images/css/bootstrap-responsive.css" type="text/css" />
<link rel="stylesheet" href="/ModularSpecPhase3_Tool/images/css/bootstrap-responsive.min.css" type="text/css" />-->
<link rel="stylesheet" href="/ModularSpecPhase3_Tool/images/css/bootstrap.css" type="text/css" />
<!-- <link rel="stylesheet" href="/ModularSpecPhase3_Tool/images/css/bootstrap.min.css" type="text/css" /> -->
<link rel="stylesheet" href="/ModularSpecPhase3_Tool/images/css/msp3.css" type="text/css" />
<script type="text/javascript" src="ModularSpecPhase3_Tool/images/js/bootstrap.js"></script>
<script type="text/javascript" src="ModularSpecPhase3_Tool/images/js/bootstrap.min.js"></script>
<script type="text/javascript" src="ModularSpecPhase3_Tool/images/js/bootstrap-tab.js"></script>
<!--  <script src="twitter-bootstrap-v2/docs/assets/js/bootstrap-dropdown.js"></script>-->
<link rel="image" href="/ModularSpecPhase3_Tool/images/img/glyphicons-halflings-white.png" type="image" />
<link rel="image" href="/ModularSpecPhase3_Tool/images/img/glyphicons-halflings.png" type="image" />
<link rel="shortcut icon" href="images/favicon.ico" />
</head>
<script type="text/javascript">
        $(document).ready(function () {
            $('.dropdown-toggle').dropdown();
            alert('called the jquery');
        });
</script>


 <!-- TOP NAVIGATION -->
 <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
     
        <div class="container">
           <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <!--  <a class="brand" href="home.jsp">Direct Certificate Discovery Testing Tool</a>-->
          <a href="home.jsp"><img src="images/DCDT_logo.png" class="brand" title="DCDT logo image"></a>
          <div class="nav-collapse collapse">
            <ul class="nav">
              <li class="active" id ="home"><a href="home.jsp"><i class="icon-home icon-white"></i> Home</a></li>
              <li id="hosting"><a href="dns.jsp">Hosting</a></li>
              <li id="discovery"><a href="download.jsp">Discovery</a></li>
             <li class="dropdown">
                 </li>
                  </ul>
                  <ul class="nav pull-right">
                    <li id="fat-menu" class="dropdown">
                      <a href="#" id="drop3" role="button" class="dropdown-toggle" data-toggle="dropdown">About <b class="caret"></b></a>
                      <ul class="dropdown-menu" id="" role="menu" aria-labelledby="drop3">
                        <li><a tabindex="-1" href="#">User's Guide</a></li>
                        <li><a tabindex="-1" target="_blank" href="http://code.google.com/p/direct-certificate-discovery-tool/wiki/Frequently_Asked_Questions">FAQ</a></li>
                        <li class="divider"></li>
                        <li><a tabindex="-1" target="_blank" href="http://code.google.com/p/direct-certificate-discovery-tool/">Code Repository</a></li>
                      </ul>
                    </li>
                   </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
      </div>
    <br /><br /><br /><br />
    
    
 
