$(document).ready(function () {
    var formTestcasesHosting = $("form[name=\"form-testcases-hosting\"]"), testcasesHostingSelect = $("select#testcase-select", formTestcasesHosting),
        testcaseHostingDirectAddr = $("input[name=\"testcaseHostingDirectAddr\"]", formTestcasesHosting),
        testcaseHostingSubmit = $("button#testcase-hosting-submit"), testcaseHostingReset = $("button#testcase-hosting-reset");
    
    formTestcasesHosting.submit(function (event) {
        // TODO: implement
        
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
    });
});
