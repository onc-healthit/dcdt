$(document).ready(function () {
    var formTestcasesDiscovery = $("form[name=\"form-testcases-discovery\"]"), testcasesDiscoverySelect = $("select#testcase-select", formTestcasesDiscovery),
        testcaseDiscoveryDirectAddr = $("div#testcase-discovery-direct-addr", formTestcasesDiscovery),
        testcaseDiscoveryDirectAddrContent = $("span:last-of-type", testcaseDiscoveryDirectAddr);
    
    testcasesDiscoverySelect.change(function (event) {
        $(event.target).dcdt.testcases.selectTestcase(event, formTestcasesDiscovery, {
            "postBuildTestcaseDescription": function (settings, testcase, testcaseDesc, testcaseDescElem) {
                var elem = $(this);
                
                [ "target", "background" ].forEach(function (testcaseDiscoveryCredsType) {
                    var testcaseDiscoveryCreds = testcase[testcaseDiscoveryCredsType + "Creds"], testcaseDiscoveryCredDescElems = [];
                    
                    if (testcaseDiscoveryCreds) {
                        testcaseDiscoveryCreds.forEach(function (testcaseDiscoveryCred) {
                            testcaseDiscoveryCredDescElems.push(settings["buildDiscoveryTestcaseCredentialDescription"](settings, testcaseDiscoveryCred));
                        });
                    }
                    
                    testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseItem($.capitalize(testcaseDiscoveryCredsType) + " Certificate(s)",
                        testcaseDiscoveryCredDescElems));
                });
                
                testcaseDiscoveryDirectAddrContent.append(settings["buildDiscoveryTestcaseDnsName"](testcase["mailAddr"]));
                testcaseDiscoveryDirectAddr.show();
            },
            "buildDiscoveryTestcaseCredentialDescription": function (settings, testcaseDiscoveryCred) {
                var elem = $(this), testcaseDiscoveryCredDescElems = [];
                
                if (testcaseDiscoveryCred) {
                    var testcaseDiscoveryCredDesc = testcaseDiscoveryCred["desc"], testcaseDiscoveryCredLoc = testcaseDiscoveryCred["loc"],
                        testcaseDiscoveryCredLocType = testcaseDiscoveryCredLoc["type"], testcaseDiscoveryCredLocElems = [];
                    
                    testcaseDiscoveryCredDescElems.push(elem.dcdt.testcases.buildTestcaseItem("Valid", testcaseDiscoveryCred["valid"]));
                    testcaseDiscoveryCredDescElems.push(elem.dcdt.testcases.buildTestcaseItem("Binding Type", testcaseDiscoveryCred["bindingType"]));
                    
                    testcaseDiscoveryCredLocElems.push(elem.dcdt.testcases.buildTestcaseItem("Type", testcaseDiscoveryCredLocType));
                    testcaseDiscoveryCredLocElems.push(elem.dcdt.testcases.buildTestcaseItem("Mail Address", testcaseDiscoveryCredLoc["mailAddr"]));
                    
                    if (testcaseDiscoveryCredLocType == "LDAP") {
                        var testcaseDiscoveryCredLocLdapConfig = testcaseDiscoveryCredLoc["ldapConfig"];
                        
                        testcaseDiscoveryCredLocElems.push(elem.dcdt.testcases.buildTestcaseItem("Host",
                            testcaseDiscoveryCredLocLdapConfig["host"]));
                        testcaseDiscoveryCredLocElems.push(elem.dcdt.testcases.buildTestcaseItem("Port",
                            testcaseDiscoveryCredLocLdapConfig["port"]));
                    }
                    
                    testcaseDiscoveryCredDescElems.push(elem.dcdt.testcases.buildTestcaseItem("Location", testcaseDiscoveryCredLocElems));
                    
                    testcaseDiscoveryCredDescElems.push(elem.dcdt.testcases.buildTestcaseItem("Description", testcaseDiscoveryCredDesc["text"]));
                    testcaseDiscoveryCredDescElems = elem.dcdt.testcases.buildTestcaseItem(testcaseDiscoveryCred["nameDisplay"],
                        testcaseDiscoveryCredDescElems);
                }
                
                return testcaseDiscoveryCredDescElems;
            },
            "buildDiscoveryTestcaseDnsName": function (testcaseDiscoveryDnsName) {
                if (!testcaseDiscoveryDnsName) {
                    return null;
                }
                
                var testcaseDiscoveryDnsNameElem = $("<span/>");
                testcaseDiscoveryDnsNameElem.text(testcaseDiscoveryDnsName);
                
                if (testcaseDiscoveryDnsName.lastIndexOf(".") == (testcaseDiscoveryDnsName.length - 1)) {
                    testcaseDiscoveryDnsNameElem.append($("<i/>").text("<domain>"));
                }
                
                return testcaseDiscoveryDnsNameElem;
            },
            "postClearTestcaseDescription": function (settings, testcaseDescElem) {
                testcaseDiscoveryDirectAddrContent.empty();
                testcaseDiscoveryDirectAddr.hide();
            }
        });
    });
});
