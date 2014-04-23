<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="dcdt" uri="/META-INF/tags/tags-dcdt.tld" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<div class="testcases-intro">
    <p>
        <b>Step 1:</b> Use the table below to help you determine the required test cases for your SUT (System Under Test).
        Notice that there are two options for storage of address-bound and domain-bound certificates.
        The Hosting tool provides tests that map to these options.
        <br/><br/>
        <b>Step 2:</b> Select the option that reflects the SUT and then select the appropriate test case from the dropdown.
        <br/><br/>
        <b>Step 3:</b> Read the Description and Instructions for the selected test case. Then enter the Direct address and submit.
        Your SUT configuration may require that you select more than one test case. If so, then select one test case at a time,
        following the instructions to execute the test after each selection. Note: The results for your testcase will be displayed
        to the right of the test case description.
        <br/>
    </p>
    <table class="table table-striped">
        <thead>
            <tr>
                <td></td>
                <td><b>My system stores certificates in DNS CERT Resource Records</b></td>
                <td><b>My system stores certificates in LDAP servers</b></td>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><b>My system stores address-bound certificates</b></td>
                <td>H1</td>
                <td>H3</td>
            </tr>
            <tr>
                <td><b>My system stores domain-bound certificates</b></td>
                <td>H2</td>
                <td>H4</td>
            </tr>
        </tbody>
    </table>
</div>
<form name="form-testcases-hosting" action="about:blank" method="post" target="testcase-target">
    <div id="testcase-info" class="input-group-sm">
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-errors input-group-addon-errors-global">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-warning-sign"/>
                    </tiles:insertDefinition>
                    <strong>Invalid Hosting testcase submission</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div>
                <span class="form-cell form-cell-label">
                    <tiles:insertDefinition name="component-glyph-label">
                        <tiles:putAttribute name="attrs">for="testcase-select"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-cloud-download glyphicon-type-info"/>
                        <tiles:putAttribute name="content" value="Hosting Testcase"/>
                    </tiles:insertDefinition>:
                </span>
                <span class="form-cell form-cell-control">
                    <select id="testcase-select" class="input-sm form-control" name="testcase">
                        <option value="">-- No testcase selected --</option>
                        <c:forEach var="testcase" items="${testcases}">
                            <option value=${testcase.name}>${testcase.nameDisplay}</option>
                        </c:forEach>
                    </select>
                </span>
            </div>
        </div>
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-errors">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-warning-sign"/>
                    </tiles:insertDefinition>
                    <strong>Invalid Hosting testcase</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div id="testcase-desc"></div>
        <div class="form-group">
            <div>
                <span class="form-cell form-cell-label">
                    <tiles:insertDefinition name="component-glyph-label">
                        <tiles:putAttribute name="attrs">for="testcase-hosting-direct-addr"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-envelope glyphicon-type-info"/>
                        <tiles:putAttribute name="content" value="Direct Address"/>
                    </tiles:insertDefinition>:
                </span>
                <span class="form-cell form-cell-control">
                    <input id="testcase-hosting-direct-addr" class="input-sm form-control" name="directAddress" type="text" disabled="disabled"/>
                </span>
            </div>
        </div>
        <div class="form-group form-group-addons">
            <div class="has-error">
                <div class="input-group-addon input-group-addon-errors">
                    <tiles:insertDefinition name="component-glyph">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-warning-sign"/>
                    </tiles:insertDefinition>
                    <strong>Invalid Direct address</strong>:
                    <ul></ul>
                </div>
            </div>
        </div>
        <div class="form-group form-group-buttons">
            <span class="btn-group btn-group-sm">
                <tiles:insertDefinition name="component-glyph-button">
                    <tiles:putAttribute name="attrs">id="testcase-hosting-submit"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-ok-circle glyphicon-type-info"/>
                    <tiles:putAttribute name="content" value="Submit"/>
                </tiles:insertDefinition>
                <tiles:insertDefinition name="component-glyph-button">
                    <tiles:putAttribute name="attrs">id="testcase-hosting-reset"</tiles:putAttribute>
                    <tiles:putAttribute name="glyph-classes" value="glyphicon-refresh glyphicon-type-info"/>
                    <tiles:putAttribute name="content" value="Reset"/>
                </tiles:insertDefinition>
            </span>
        </div>
    </div>
    <div id="testcase-results" class="input-group-sm">
        <div class="form-group">
            <div>
                <span class="form-cell form-cell-label">
                    <tiles:insertDefinition name="component-glyph-label">
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-certificate glyphicon-type-info"/>
                        <tiles:putAttribute name="content" value="Results"/>
                    </tiles:insertDefinition>
                </span>
            </div>
        </div>
        <div id="testcase-results-accordion"></div>
        <i>None</i>
    </div>
</form>
<iframe id="testcase-target" class="hide" name="testcase-target" src="about:blank"></iframe> 