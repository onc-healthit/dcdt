$(document).ready(function () {
    var formTestcasesDiscovery = $("form[name=\"form-testcases-discovery\"]"), testcasesDiscoverySelect = $("select#testcase-select", formTestcasesDiscovery),
        testcaseDiscoveryDirectAddr = $("div#testcase-discovery-direct-addr", formTestcasesDiscovery),
        testcaseDiscoveryDirectAddrContent = $("span:last-of-type", testcaseDiscoveryDirectAddr),
        testcaseDiscoveryDirectAddrDomainPlaceholder = $("i", testcaseDiscoveryDirectAddr);
    
    testcasesDiscoverySelect.change(function (event) {
        $(event.target).dcdt.testcases.selectTestcase(event, formTestcasesDiscovery, {
            "postBuildTestcaseDescription": function (settings, testcase, testcaseDesc, testcaseDescElem) {
                var elem = $(this), buildDiscoveryTestcaseCredentialDescriptionFunc = settings["buildDiscoveryTestcaseCredentialDescription"];
                
                testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseDescriptionItem("Target Certificate",
                    [ buildDiscoveryTestcaseCredentialDescriptionFunc(testcase["targetCred"]) ]));
                
                var testcaseDiscoveryBackgroundCreds = testcase["backgroundCreds"], testcaseDiscoveryBackgroundCredDescElems = [];
                
                if (testcaseDiscoveryBackgroundCreds) {
                    testcaseDiscoveryBackgroundCreds.forEach(function (testcaseDiscoveryBackgroundCred) {
                        testcaseDiscoveryBackgroundCredDescElems.push(buildDiscoveryTestcaseCredentialDescriptionFunc(testcaseDiscoveryBackgroundCred));
                    });
                }
                
                testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseDescriptionItem("Background Certificate(s)",
                    testcaseDiscoveryBackgroundCredDescElems));
                
                var testcaseDiscoveryMailAddr = testcase["mailAddr"];
                
                testcaseDiscoveryDirectAddrContent.text(testcaseDiscoveryMailAddr);
                
                if (testcaseDiscoveryMailAddr.endsWith(".")) {
                    testcaseDiscoveryDirectAddrDomainPlaceholder.show();
                }
                
                testcaseDiscoveryDirectAddr.show();
            },
            "buildDiscoveryTestcaseCredentialDescription": function (testcaseDiscoveryCred) {
                var elem = $(this), testcaseDiscoveryCredDescElems = [];
                
                if (testcaseDiscoveryCred) {
                    var testcaseDiscoveryCredDesc = testcaseDiscoveryCred["desc"], testcaseDiscoveryCredLoc = testcaseDiscoveryCred["loc"],
                        testcaseDiscoveryCredLocType = testcaseDiscoveryCredLoc["type"], testcaseDiscoveryCredLocElems = [];
                    
                    testcaseDiscoveryCredDescElems.push(elem.dcdt.testcases.buildTestcaseDescriptionItem("Valid", testcaseDiscoveryCred["valid"]));
                    testcaseDiscoveryCredDescElems.push(elem.dcdt.testcases.buildTestcaseDescriptionItem("Binding Type", testcaseDiscoveryCred["bindingType"]));
                    
                    testcaseDiscoveryCredLocElems.push(elem.dcdt.testcases.buildTestcaseDescriptionItem("Type", testcaseDiscoveryCredLocType));
                    
                    if (testcaseDiscoveryCredLocType == "DNS") {
                        testcaseDiscoveryCredLocElems.push(elem.dcdt.testcases.buildTestcaseDescriptionItem("Domain",
                            testcaseDiscoveryCredLoc["instanceDomainConfig"]["domainName"]));
                    } else if (testcaseDiscoveryCredLocType == "LDAP") {
                        var testcaseDiscoveryCredLocInstanceLdapConfig = testcaseDiscoveryCredLoc["instanceLdapConfig"];
                        
                        testcaseDiscoveryCredLocElems.push(elem.dcdt.testcases.buildTestcaseDescriptionItem("Host",
                            testcaseDiscoveryCredLocInstanceLdapConfig["host"]));
                        testcaseDiscoveryCredLocElems.push(elem.dcdt.testcases.buildTestcaseDescriptionItem("Port",
                            testcaseDiscoveryCredLocInstanceLdapConfig["port"]));
                    }
                    
                    testcaseDiscoveryCredDescElems.push(elem.dcdt.testcases.buildTestcaseDescriptionItem("Location", testcaseDiscoveryCredLocElems));
                    
                    testcaseDiscoveryCredDescElems.push(elem.dcdt.testcases.buildTestcaseDescriptionItem("Description", testcaseDiscoveryCredDesc["text"]));
                    testcaseDiscoveryCredDescElems = elem.dcdt.testcases.buildTestcaseDescriptionItem(testcaseDiscoveryCred["nameDisplay"],
                        testcaseDiscoveryCredDescElems);
                }
                
                return testcaseDiscoveryCredDescElems;
            },
            "postClearTestcaseDescription": function (settings, testcaseDescElem) {
                testcaseDiscoveryDirectAddrContent.empty();
                testcaseDiscoveryDirectAddrDomainPlaceholder.hide();
                testcaseDiscoveryDirectAddr.hide();
            }
        });
    });
});
