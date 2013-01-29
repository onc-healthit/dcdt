<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<div class="container">
	<h2>Welcome to the Direct Certificate Discovery Testing Tool</h2>
	<br/>
	
	<p><strong>Purpose of this Tool</strong></p>
	<p>
		The Direct Certificate Discovery Tool (DCDT) was created to support automated testing of systems that plan to enact the Certificate Discovery and Provider Directory Implementation Guide, approved as normative specification by the Direct community, as of July 9, 2012.
		It is based on the written test package and requirement traceability matrix created by the Modular Specifications project under the direction of the Office of the National Coordinator (ONC) and National Institute of Standards and Technology (NIST).
	</p>

	<p><strong>Future Plans</strong></p>
	<p>
		The tool fulfills Meaningful Use Stage 2 (MU2) and will be rolled into NIST's overall testing toolkit over time.
		Feedback from community usage will be prioritized as received, and the tool will have additional releases scheduled as needed.
	</p>
	
	<p><strong>How to Use this Tool</strong></p>
	Our tool is divided into two main testing areas:<br/>  
	<ul>
		<li>
			<html:link action="/hosting">Hosting</html:link> allows a System Under Test (SUT) to verify that their certificates are hosted correctly, and discoverable by other Direct implementations.
		</li>
		<li>
			<html:link action="/discovery">Discovery</html:link> allows a SUT to verify that they can discover certificates in other Direct implementations by using them to send Direct messages.
		</li>
	</ul>
	<p>
		Both areas contain details on the underlying test cases and how to run them.
		There is also a <a href="http://code.google.com/p/direct-certificate-discovery-tool/wiki/User_Guide_2_1">User's Guide</a>.<br/>
	</p>
</div>