<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="spring-form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="tilesx" uri="http://tiles.apache.org/tags-tiles-extras" %>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@page contentType="text/html; UTF-8" isELIgnored="false" pageEncoding="UTF-8" %>
<div id="hostingIntro">
    <p>
        <b>Step 1:</b> Use the table below to help you determine the required test cases for your SUT (System Under Test).
        Notice that there are two options for storage of address-bound and domain-bound certificates.
        The Hosting tool provides tests that map to these options.
        <br/><br/>
        <b>Step 2:</b> Select the option that reflects the SUT and then select the appropriate test case in the "Choose test case" dropdown.
        <br/><br/>
        <b>Step 3:</b> Read the Description and Instructions for the selected test case. Then enter the Direct address and submit.
        Your SUT configuration may require that you select more than one test case. If so, then select one test case at a time,
        following the instructions to execute the test after each selection.
        <br/>
    </p>
    <table class="table table-striped">
        <thead>
        <tr>
            <td></td>
            <td><b>My system stores certificates in DNS CERT resource records</b></td>
            <td><b>My system stores certificates in LDAP servers</b></td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><b>My system stores address-bound certificates</b></td>
            <td>H1, H6, H7, H8</td>
            <td>H3, H5, H9</td>
        </tr>
        <tr>
            <td><b>My system stores domain-bound certificates</b></td>
            <td>H2, H6, H7, H8</td>
            <td>H4, H5, H9</td>
        </tr>
        </tbody>
    </table>
    <br/>
    <div id="content" class="form-container">
        <spring-form:form name="hosting-form">
            <div class="form-item">
                <span class="form-item-label-container">
                    <tiles:insertDefinition name="component-glyph-label">
                        <tiles:putAttribute name="classes" value="form-item-label"/>
                        <tiles:putAttribute name="attrs">for="hostingTestcases"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-arrow-right glyphicon-type-info"/>
                        <tiles:putAttribute name="content" value="Choose test case"/>
                    </tiles:insertDefinition>:
                    <select id="hostingTestcases">
                        <option value="">-- No test case selected --</option>
                        <c:forEach var="hostingTestcase" items="${hosting}">
                            <option value=${hostingTestcase.name}>${hostingTestcase.typeDisplay}</option>
                        </c:forEach>
                    </select>
                </span>
            </div>
            <br/>
            <div id="hostingTestcaseDescription"></div>
            <div class="form-item">
                <span class="form-item-label-container">
                    <tiles:insertDefinition name="component-glyph-label">
                        <tiles:putAttribute name="classes" value="form-item-label"/>
                        <tiles:putAttribute name="attrs">for="hostingDirectAddr"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-envelope glyphicon-type-info"/>
                        <tiles:putAttribute name="content" value="Enter Direct address"/>
                    </tiles:insertDefinition>:
                </span>
                <input id="hostingDirectAddr" name="directAddr" type="text"/>
            </div>
            <br/>
            <div class="form-group form-group-buttons">
                <span class="btn-group btn-group-sm">
                    <tiles:insertDefinition name="component-glyph-button">
                        <tiles:putAttribute name="attrs">id="hostingSubmit"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon-ok-circle glyphicon-type-info"/>
                        <tiles:putAttribute name="content" value="Submit"/>
                    </tiles:insertDefinition>
                </span>
                <span class="btn-group btn-group-sm">
                    <tiles:insertDefinition name="component-glyph-button">
                        <tiles:putAttribute name="attrs">id="hostingReset"</tiles:putAttribute>
                        <tiles:putAttribute name="glyph-classes" value="glyphicon glyphicon-refresh glyphicon-type-info"/>
                        <tiles:putAttribute name="content" value="Reset"/>
                    </tiles:insertDefinition>
                </span>
            </div>
        </spring-form:form>
    </div>
</div>