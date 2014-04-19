(function ($) {
    $.extend($.dcdt, {
        "hosting": $.extend(function () {
            return this;
        }, {
            "processHostingTestcase": function () {
                return $.dcdt.beans.setBean({
                    "data": $.encodeJson({
                        "@type": "request",
                        "items": [
                            hostingTestcaseSubmission
                        ]
                    }),
                    "queryBeanSuccess": function (data, status, jqXhr) {
                        $.dcdt.hosting.displayHostingTestcaseResults(data);
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
            },
            "displayHostingTestcaseResults": function (data) {
                var hostingTestcaseResult = data["items"][0];
                var hostingTestcase = hostingTestcaseSubmission["testcase"];
                var directAddr = hostingTestcaseSubmission["directAddr"];
                var testcase = TESTCASES.filter(function (testcase) {
                    return testcase["name"] == hostingTestcase;
                });
                testcase = testcase[0];

                var procSteps = hostingTestcaseResult["procSteps"];
                var success = hostingTestcaseResult["success"];
                var msgs = hostingTestcaseResult["msgs"];
                var discoveredCertInfo = hostingTestcaseResult["discoveredCertInfo"], discoveredCert = (discoveredCertInfo ? discoveredCertInfo["cert"] : null);

                var header = $("<h3/>");
                $.fn.dcdt.testcases.appendTestcaseResults(header, "Testcase: ", hostingTestcase);
                $.fn.dcdt.testcases.appendTestcaseResults(header, "Direct Address: ", directAddr);
                
                var result = $("<div/>");
                $.fn.dcdt.testcases.appendTestcaseResults(result, "Success: ", success, true);
                $.fn.dcdt.testcases.appendTestcaseResults(result, "Message(s): ", (msgs || null), true);
                $.fn.dcdt.testcases.buildTestcaseSteps(result, procSteps);
                $.fn.dcdt.testcases.appendTestcaseResults(result, "Discovered Certificate: ", (!$.isNull(discoveredCert) ? 
                    $("<pre/>").text(discoveredCert) : null), true);

                hostingTestcaseResults.append(header);
                hostingTestcaseResults.append(result);
                
                hostingTestcaseResults.accordion("refresh");
                hostingTestcaseResults.accordion({ "active": -1 });
            }
        })
    });

    var formTestcasesHosting, testcasesHostingSelect, testcaseHostingDirectAddr,testcaseHostingSubmit, testcaseHostingReset,
        hostingTestcaseSubmission, hostingTestcaseResults;

    $(document).ready(function () {
        formTestcasesHosting = $("form[name=\"form-testcases-hosting\"]");
        testcasesHostingSelect = $("select#testcase-select", formTestcasesHosting);
        testcaseHostingDirectAddr = $("input[name=\"directAddress\"]", formTestcasesHosting);
        testcaseHostingSubmit = $("button#testcase-hosting-submit");
        testcaseHostingReset = $("button#testcase-hosting-reset");
        hostingTestcaseResults = $("div#testcase-results-accordion");

        $("#testcase-results-accordion").accordion({
            collapsible: true,
            heightStyle: "content"
        });

        formTestcasesHosting.submit(function (event) {
            hostingTestcaseSubmission = {
                "@type": "hostingTestcaseSubmission",
                "directAddr": testcaseHostingDirectAddr.val(),
                "testcase": testcasesHostingSelect.val()
            };
            $.dcdt.hosting.processHostingTestcase();

            event.preventDefault();
            event.stopPropagation();
        });

        testcasesHostingSelect.change(function (event) {
            $(event.target).dcdt.testcases.selectTestcase(event, formTestcasesHosting, {
                "postBuildTestcaseDescription": function (settings, testcase, testcaseDesc, testcaseDescElem) {
                    var elem = $(this);

                    testcaseDescElem.prepend(elem.dcdt.testcases.buildTestcaseDescriptionItem("Binding Type", testcase["bindingType"]),
                        elem.dcdt.testcases.buildTestcaseDescriptionItem("Location Type", testcase["locType"]));
                }
            });
        });

        testcaseHostingSubmit.click(function (event) {
            formTestcasesHosting.submit();
        });

        testcaseHostingReset.click(function (event) {
            testcaseHostingDirectAddr.val("");
            testcasesHostingSelect.val("");
            hostingTestcaseResults.empty();
            $.fn.dcdt.testcases.clearTestcaseDescription();
            $.fn.dcdt.form.clearErrors();
        });
    });
})(jQuery);