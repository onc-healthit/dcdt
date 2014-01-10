$(document).ready(function () {
    var discoveryForm = $("form[name='discovery-form']");

    $("#discoveryTestcases").change(function () {
        var discoveryTestcaseName = $("#discoveryTestcases option:selected").val();
        if(discoveryTestcaseName != ""){
            var discoveryTestcase = DISCOVERY_TESTCASES[discoveryTestcaseName];
            displayMailAddress(discoveryTestcase.mailAddress);
            displayDiscoveryTestcaseDescription(discoveryTestcase.testcaseDescription);
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
    addr.append("<b>Direct Address: </b>" + mailAddress +"<br/><br/>");
}

function displayDiscoveryTestcaseDescription(discoveryTestcaseDesc){
    var desc = $("#discoveryTestcaseDescription");
    desc.empty();
    appendTestcaseInfo(desc, "Description", discoveryTestcaseDesc.description, false, true);
    appendTestcaseInfo(desc, "Target Certificate", discoveryTestcaseDesc.targetCertificate, true, true);
    appendTestcaseInfo(desc, "Background Certificates", discoveryTestcaseDesc.backgroundCertificates, true, false);
    appendTestcaseInfo(desc, "RTM", discoveryTestcaseDesc.rtm, false, true);
    appendTestcaseInfo(desc, "Underlying Specification Reference", discoveryTestcaseDesc.specifications, true, false);
    appendTestcaseInfo(desc, "Instructions", discoveryTestcaseDesc.instructions, false, true);
}

function clearDiscoveryTestcaseInfo(){
    $("#discoveryTestcaseDescription").empty();
    $("#directAddress").empty();
}