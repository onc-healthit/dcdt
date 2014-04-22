(function ($) {
    $.extend($.dcdt, {
        "hosting": $.extend(function () {
            return this;
        }, {
            "processHostingTestcase": function ()
            {
                return $.dcdt.beans.setBean({
                    "data": $.encodeJson({
                        "@type": "request",
                        "items": [
                            testcaseHostingSubmission
                        ]
                    }),
                    "queryBeanSuccess": function (data, status, jqXhr) {
                        var testcaseHostingResult = data["items"][0];
                        var testcaseHostingSuccess = testcaseHostingResult["success"];
                        var testcaseHostingSuccessStr = (testcaseHostingSuccess ? "success" : "error");
                        var testcaseHostingName = testcaseHostingSubmission["testcase"];

                        var testcaseHostingResultHeaderElem = $("<h3/>");
                        testcaseHostingResultHeaderElem.enableClass("testcase-hosting-result-header");
                        testcaseHostingResultHeaderElem.enableClass(("testcase-hosting-result-header-" + testcaseHostingSuccessStr));
                        testcaseHostingResultHeaderElem.append($.fn.dcdt.testcases.buildTestcaseItem("Testcase", testcaseHostingName));
                        testcaseHostingResultHeaderElem.append($.fn.dcdt.testcases.buildTestcaseItem("Direct Address",
                            testcaseHostingSubmission["directAddr"]));
                        testcaseHostingResultsAccordion.append(testcaseHostingResultHeaderElem);

                        var testcaseHostingCertInfo = testcaseHostingResult["discoveredCertInfo"],
                            testcaseHostingCert = (testcaseHostingCertInfo ? testcaseHostingCertInfo["cert"] : null);

                        var testcaseHostingResultBodyElem = $("<div/>");
                        testcaseHostingResultBodyElem.append($.fn.dcdt.testcases.buildTestcaseItem("Success", testcaseHostingSuccess));
                        testcaseHostingResultBodyElem.append($.fn.dcdt.testcases.buildTestcaseItem("Processing Message(s)", testcaseHostingResult["procMsgs"]));
                        testcaseHostingResultBodyElem.append($.fn.dcdt.testcases.buildTestcaseSteps("Processed Step(s)", testcaseHostingResult["procSteps"]));
                        testcaseHostingResultBodyElem.append($.fn.dcdt.testcases.buildTestcaseItem("Discovered Certificate",
                            (testcaseHostingCert ? $("<pre/>").enableClass("testcase-hosting-cert").text(testcaseHostingCert.replace(/ {4}/g, "  ")) : null)));
                        testcaseHostingResultsAccordion.append(testcaseHostingResultBodyElem);

                        testcaseHostingResultsAccordion.accordion("refresh");
                        testcaseHostingResultsAccordion.accordion({ "active": -1 });
                        
                        $("h3.testcase-hosting-result-header", testcaseHostingResultsAccordion).each(function () {
                            var testcaseHostingResultHeaderElem = $(this);
                            
                            var testcaseHostingResultHeaderIcon = $("span.ui-accordion-header-icon", testcaseHostingResultHeaderElem);
                            testcaseHostingResultHeaderIcon.disableClass("ui-icon");
                            testcaseHostingResultHeaderIcon.enableClass("glyphicon");
        
                            if (testcaseHostingResultHeaderElem.hasClass("testcase-hosting-result-header-success")) {
                                testcaseHostingResultHeaderIcon.enableClass("glyphicon-ok-circle");
                                testcaseHostingResultHeaderIcon.enableClass("glyphicon-type-success");
                            } else {
                                testcaseHostingResultHeaderIcon.enableClass("glyphicon-remove-circle");
                                testcaseHostingResultHeaderIcon.enableClass("glyphicon-type-error");
                            }
                        });
                    },
                    "queryBeanErrors": function (data, status, jqXhr) {
                        $.dcdt.beans.addQueryErrors(formTestcasesHosting, data);
                    },
                    "postQueryBean": function (jqXhr, status) {
                        formTestcasesHosting.dcdt.form.formReady();
                    },
                    "preQueryBean": function (jqXhr, settings) {
                        $.dcdt.beans.clearBeanErrors(formTestcasesHosting);
                    },
                    "url": URL_HOSTING_PROCESS
                });
            }
        })
    });

    var formTestcasesHosting, testcasesHostingSelect, testcaseHostingDirectAddr, testcaseHostingSubmit, testcaseHostingReset, testcaseHostingSubmission,
        testcaseHostingResults, testcaseHostingResultsAccordion;

    $(document).ready(function () {
        formTestcasesHosting = $("form[name=\"form-testcases-hosting\"]");
        testcasesHostingSelect = $("select#testcase-select", formTestcasesHosting);
        testcaseHostingDirectAddr = $("input#testcase-hosting-direct-addr", formTestcasesHosting);
        testcaseHostingSubmit = $("button#testcase-hosting-submit", formTestcasesHosting);
        testcaseHostingReset = $("button#testcase-hosting-reset", formTestcasesHosting);
        testcaseHostingResults = $("div#testcase-results", formTestcasesHosting);
        testcaseHostingResultsAccordion = $("div#testcase-results-accordion", testcaseHostingResults);

        testcaseHostingResultsAccordion.accordion({
            "collapsible": true,
            "heightStyle": "content",
            "icons": {
                "activeHeader": "",
                "header": ""
            }
        });
        testcaseHostingResultsAccordion.empty();

        formTestcasesHosting.submit(function (event) {
            testcaseHostingSubmission = {
                "@type": "hostingTestcaseSubmission",
                "directAddr": testcaseHostingDirectAddr.val(),
                "testcase": testcasesHostingSelect.val()
            };
            
            $.dcdt.hosting.processHostingTestcase();
        });

        testcasesHostingSelect.change(function (event) {
            $(event.target).dcdt.testcases.selectTestcase(event, formTestcasesHosting, {
                "postBuildTestcaseDescription": function (settings, testcase, testcaseDesc, testcaseDescElem) {
                    var elem = $(this);

                    testcaseDescElem.prepend(elem.dcdt.testcases.buildTestcaseItem("Binding Type", testcase["bindingType"]),
                        elem.dcdt.testcases.buildTestcaseItem("Location Type", testcase["locType"]));
                    
                    testcaseHostingDirectAddr.removeAttr("disabled");
                }
            });
        });

        testcaseHostingSubmit.click(function (event) {
            formTestcasesHosting.submit();
        });

        testcaseHostingReset.click(function (event) {
            testcaseHostingDirectAddr.val("");
            testcasesHostingSelect.val("");
            testcaseHostingResultsAccordion.empty();
            $.fn.dcdt.testcases.clearTestcaseDescription({
                "postClearTestcaseDescription": function () {
                    testcaseHostingDirectAddr.attr("disabled", "disabled");
                }
            });
            $.fn.dcdt.form.clearErrors();
        });
    });
})(jQuery);