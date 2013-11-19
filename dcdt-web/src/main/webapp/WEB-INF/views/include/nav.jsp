<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <div class="brand">
                <a href="${urlHome}">
                    <img src="${urlStaticImages}/dcdt-logo-48x48.png"/>
                    <spring:message code="dcdt.web.title"/>
                </a>
            </div>
            <ul class="nav">
                <li id="home">
                    <a href="${urlHome}"><i class="icon-home icon-white"></i> <strong>Home</strong></a>
                </li>
                <li id="hosting">
                    <a href="${urlHosting}"><strong>Hosting</strong></a>
                </li>
                <li id="discovery">
                    <a href="${urlDiscovery}"><strong>Discovery</strong></a>
                </li>
            </ul>
            <ul class="nav pull-right">
                <li id="fat-menu" class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown"><strong>About</strong> <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li>
                            <a target="_blank" href="${urlWikiUserGuide}">User's Guide</a>
                        </li>
                        <li>
                            <a target="_blank" href="${urlWikiFaq}">FAQ</a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a target="_blank" href="${urlGoogleCodeProject}">Project Site</a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>