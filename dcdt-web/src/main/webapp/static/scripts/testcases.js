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
                testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseItem("Negative", testcase["neg"]));
                testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseItem("Optional", testcase["opt"]));
                testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseItem("Description", testcaseDesc["text"]));
                testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseItem("RTM Sections", testcaseDesc["rtmSections"].join(", ")));
                testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseItem("Underlying Specification References", testcaseDesc["specs"]));
                testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseItem("Instructions", testcaseDesc["instructions"]));
                
                if (settings) {
                    var postBuildTestcaseDescCallback = settings["postBuildTestcaseDescription"];
                    
                    if ($.isFunction(postBuildTestcaseDescCallback)) {
                        postBuildTestcaseDescCallback.apply(elem, [ settings, testcase, testcaseDesc, testcaseDescElem ]);
                    }
                }
                
                return testcaseDescElem;
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
            "buildTestcaseSteps": function (testcaseStepsLbl, testcaseSteps) {
                var testcaseStepsList = $("<ol/>");
                
                testcaseSteps.forEach(function (testcaseStep) {
                    testcaseStepsList.append($("<li/>").append($.fn.dcdt.testcases.buildTestcaseItem(testcaseStep["desc"]["text"], [
                        $.fn.dcdt.testcases.buildTestcaseItem("Success", testcaseStep["success"]),
                        $.fn.dcdt.testcases.buildTestcaseItem("Message(s)", testcaseStep["msgs"]),
                        $.fn.dcdt.testcases.buildTestcaseItem("Binding Type", testcaseStep["bindingType"]),
                        $.fn.dcdt.testcases.buildTestcaseItem("Location Type", testcaseStep["locType"]) ])));
                });
                
                return $.fn.dcdt.testcases.buildTestcaseItem(testcaseStepsLbl, testcaseStepsList);
            },
            "buildTestcaseItem": function (testcaseItemLbl, testcaseItemValues) {
                var testcaseItemElem = $("<div/>"), testcaseItemLblElem = $("<span/>");
                testcaseItemLblElem.append($("<strong/>").text(testcaseItemLbl), ": ");
                testcaseItemElem.append(testcaseItemLblElem);
                
                if (!$.isBoolean(testcaseItemValues) && !$.isNumeric(testcaseItemValues) && (!testcaseItemValues || $.isEmptyObject(testcaseItemValues))) {
                    testcaseItemLblElem.append($("<i/>").text("None"));
                } else if ($.isArray(testcaseItemValues)) {
                    var testcaseItemValuesList = $("<ul/>");
                    
                    testcaseItemValues.forEach(function (testcaseItemValue) {
                        testcaseItemValuesList.append($("<li/>").append(testcaseItemValue));
                    });
                    
                    testcaseItemElem.append(testcaseItemValuesList);
                } else {
                    testcaseItemLblElem.append(($.isBoolean(testcaseItemValues) || $.isNumeric(testcaseItemValues)) ? testcaseItemValues.toString()
                        : testcaseItemValues);
                }
                
                return testcaseItemElem;
            }
        })
    });
})(jQuery);
