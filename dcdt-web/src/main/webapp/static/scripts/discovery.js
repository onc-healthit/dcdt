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
                    
                    testcaseDescElem.append(elem.dcdt.testcases.buildTestcaseDescriptionItem($.capitalize(testcaseDiscoveryCredsType) + " Certificate(s)",
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
                    
                    testcaseDiscoveryCredDescElems.push(elem.dcdt.testcases.buildTestcaseDescriptionItem("Valid", testcaseDiscoveryCred["valid"]));
                    testcaseDiscoveryCredDescElems.push(elem.dcdt.testcases.buildTestcaseDescriptionItem("Binding Type", testcaseDiscoveryCred["bindingType"]));
                    
                    testcaseDiscoveryCredLocElems.push(elem.dcdt.testcases.buildTestcaseDescriptionItem("Type", testcaseDiscoveryCredLocType));
                    
                    if (testcaseDiscoveryCredLocType == "DNS") {
                        testcaseDiscoveryCredLocElems.push(elem.dcdt.testcases.buildTestcaseDescriptionItem("Domain",
                            settings["buildDiscoveryTestcaseDnsName"](testcaseDiscoveryCredLoc["instanceDomainConfig"]["domainName"])));
                    } else if (testcaseDiscoveryCredLocType == "LDAP") {
                        var testcaseDiscoveryCredLocInstanceLdapConfig = testcaseDiscoveryCredLoc["instanceLdapConfig"];
                        
                        testcaseDiscoveryCredLocElems.push(elem.dcdt.testcases.buildTestcaseDescriptionItem("Host",
                            settings["buildDiscoveryTestcaseDnsName"](testcaseDiscoveryCredLocInstanceLdapConfig["host"])));
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