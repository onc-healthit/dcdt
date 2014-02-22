(function ($) {
    $.extend($.fn.dcdt, {
        "testcases": $.extend(function () {
            return this;
        }, {
            "selectTestcase": function (event, form, settings) {
                var elem = $(event.target), testcaseName = elem.val(), testcase;

                form.dcdt.testcases.clearTestcaseDescription(settings);

                if (testcaseName && !$.isEmptyObject(testcase = TESTCASES.filter(function (testcase) {
                    return testcase["name"] == testcaseName;
                })) && (testcase = testcase[0])) {
                    form.dcdt.testcases.setTestcaseDescription(settings, testcase);
                }
            },
            "setTestcaseDescription": function (settings, testcase) {
                var elem = $(this);

                return elem.dcdt.testcases.testcaseDescription().append(elem.dcdt.testcases.buildTestcaseDescription(settings, testcase));
            },
            "buildTestcaseDescription": function (settings, testcase) {
                var elem = $(this), testcaseDesc = testcase["desc"], testcaseDescElem = $("<div/>");
                testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseDescriptionItem("Negative", testcase["neg"]));
                testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseDescriptionItem("Optional", testcase["opt"]));
                testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseDescriptionItem("Description", testcaseDesc["text"]));
                testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseDescriptionItem("RTM Sections", testcaseDesc["rtmSections"].join(", ")));
                testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseDescriptionItem("Underlying Specification References", testcaseDesc["specs"]));
                testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseDescriptionItem("Instructions", testcaseDesc["instructions"]));

                if (settings) {
                    var postBuildTestcaseDescCallback = settings["postBuildTestcaseDescription"];

                    if ($.isFunction(postBuildTestcaseDescCallback)) {
                        postBuildTestcaseDescCallback.apply(elem, [ settings, testcase, testcaseDesc, testcaseDescElem ]);
                    }
                }

                return testcaseDescElem;
            },
            "buildTestcaseDescriptionItem": function (testcaseDescItemLbl, testcaseDescItemValues) {
                var testcaseDescItemElem = $("<div/>"), testcaseDescItemLblElem = $("<span/>");
                testcaseDescItemLblElem.append($("<b/>").text(testcaseDescItemLbl), ": ");
                testcaseDescItemElem.append(testcaseDescItemLblElem);

                if (!$.isBoolean(testcaseDescItemValues) && !$.isNumeric(testcaseDescItemValues) && (!testcaseDescItemValues ||
                    $.isEmptyObject(testcaseDescItemValues))) {
                    testcaseDescItemLblElem.append($("<i/>").text("None"));
                } else if ($.isArray(testcaseDescItemValues)) {
                    var testcaseDescItemValuesList = $("<ul/>");

                    testcaseDescItemValues.forEach(function (testcaseDescItemValue) {
                        testcaseDescItemValuesList.append($("<li/>").append(testcaseDescItemValue));
                    });

                    testcaseDescItemElem.append(testcaseDescItemValuesList);
                } else {
                    testcaseDescItemLblElem.append(($.isBoolean(testcaseDescItemValues) || $.isNumeric(testcaseDescItemValues)) ?
                        testcaseDescItemValues.toString() : testcaseDescItemValues);
                }

                return testcaseDescItemElem;
            },
            "buildTestcaseResultSteps": function (elem, testcaseResultSteps) {
                var stepElem = $("<ol/>");
                testcaseResultSteps.forEach(function (testcaseResultStep) {
                    var resultType = testcaseResultStep["resultType"];
                    stepElem.append($("<li/>").append($("<b/>").append(testcaseResultStep["desc"]["text"])));
                    stepElem.append($.fn.dcdt.testcases.appendTestcaseResults(stepElem, "Successful: ", testcaseResultStep["successful"]));
                    stepElem.append($.fn.dcdt.testcases.appendTestcaseResults(stepElem, "Result Type: ", resultType));
                    switch(resultType){
                        case "DNS_LOOKUP":
                            stepElem.append($.fn.dcdt.testcases.appendTestcaseResults(stepElem, "DNS Record Type: ", testcaseResultStep["dnsRecordType"]));
                            break;
                        case "CERT_LOOKUP":
                            stepElem.append($.fn.dcdt.testcases.appendTestcaseResults(stepElem, "Location Type: ", testcaseResultStep["locationType"]));
                            stepElem.append($.fn.dcdt.testcases.appendTestcaseResults(stepElem, "Binding Type: ", testcaseResultStep["bindingType"]));
                            stepElem.append($.fn.dcdt.testcases.appendTestcaseResults(stepElem, "Certificate Status: ", testcaseResultStep["certStatus"]));
                            break;
                        default: break;
                    }
                });
                elem.append(stepElem);
            },
            "clearTestcaseDescription": function (settings) {
                var elem = $(this), testcaseDescElem = elem.dcdt.testcases.testcaseDescription();
                testcaseDescElem.empty();

                if (settings) {
                    var postClearTestcaseDescCallback = settings["postClearTestcaseDescription"];

                    if ($.isFunction(postClearTestcaseDescCallback)) {
                        postClearTestcaseDescCallback.apply(elem, [ settings, testcaseDescElem ]);
                    }
                }

                return testcaseDescElem;
            },
            "testcaseDescription": function () {
                return $(this).find("div#testcase-desc");
            },
            "testcaseSelect": function () {
                return $(this).find("select#testcase-select");
            },
            "appendTestcaseResults": function (elem, testcaseLbl, testcaseValue) {
                elem.append($("<b/>").text(testcaseLbl), testcaseValue + "<br/>");
            }
        })
    });
})(jQuery);
