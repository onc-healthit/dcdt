$(document).ready(function () {
    $("#discoveryTestcases").change(function () {
        var discoveryTestcaseName = $("#discoveryTestcases option:selected").val();
        if(discoveryTestcaseName != ""){
            var discoveryTestcase = DISCOVERY_TESTCASES[discoveryTestcaseName];
            displayMailAddress(discoveryTestcase.mailAddress);
            displayDiscoveryTestcase(discoveryTestcase);
        }
        else{
            clearDiscoveryTestcaseInfo();
        }
    });
});

function displayMailAddress(mailAddress){
    var addr = $("#directAddress");
    addr.empty();
    addr.append("<span class=\"glyphicon glyphicon-envelope glyphicon-type-info\"/> </span>");
    addr.append("<b>Direct Address: </b>" + mailAddress + ((mailAddress.lastIndexOf(".") == (mailAddress.length - 1)) ? "<i>&lt;domain&gt;</i>" : "") +
        "<br/><br/>");
}

function displayDiscoveryTestcase(discoveryTestcase){
    var discoveryTestcaseDesc = discoveryTestcase.description, desc = $("#discoveryTestcaseDescription");
    desc.empty();
    
    appendTestcaseInfo(desc, "Description", discoveryTestcaseDesc.text, false, true);
    appendTestcaseInfo(desc, "RTM Sections", discoveryTestcaseDesc.rtmSections.join(", "), false, true);
    appendTestcaseInfo(desc, "Underlying Specification Reference", discoveryTestcaseDesc.specifications, true, false);
    appendTestcaseInfo(desc, "Instructions", discoveryTestcaseDesc.instructions, false, true);
    
    var discoveryTestcaseCreds = discoveryTestcase.credentials, discoveryTestcaseBackgroundCreds = discoveryTestcaseCreds.background,
        discoveryTestcaseTargetCred = discoveryTestcaseCreds.target;
    
    appendTestcaseInfo(desc, "Target Certificate", (discoveryTestcaseTargetCred ? discoveryTestcaseTargetCred.description : "<i>None</i>"), true, true);
    appendTestcaseInfo(desc, "Background Certificates", (discoveryTestcaseBackgroundCreds ? 
        $.map(discoveryTestcaseBackgroundCreds, function (discoveryTestcaseBackgroundCred) {
            return discoveryTestcaseBackgroundCred.description;
        }) : "<i>None</i>"), $.isArray(discoveryTestcaseBackgroundCreds), false);
}

function clearDiscoveryTestcaseInfo(){
    $("#discoveryTestcaseDescription").empty();
    $("#directAddress").empty();
}