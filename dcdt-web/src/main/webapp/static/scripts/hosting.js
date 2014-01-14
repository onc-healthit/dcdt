$(document).ready(function () {
    var hostingForm = $("form[name='hosting-form']");

    $("#hostingTestcases").change(function () {
        var hostingTestcaseName = $("#hostingTestcases option:selected").val();
        if(hostingTestcaseName != ""){
            var hostingTestcase = HOSTING_TESTCASES[hostingTestcaseName];
            displayHostingTestcaseDescription(hostingTestcase.description);
        }
        else{
            clearHostingTestcaseDescriptionContents();
        }
    });

    $("#hostingReset").click(function () {
        hostingForm[0].reset();
        clearHostingTestcaseDescriptionContents();
    });
});

function displayHostingTestcaseDescription(hostingTestcaseDesc){
    var desc = $("#hostingTestcaseDescription");
    desc.empty();
    appendTestcaseInfo(desc, "Description", hostingTestcaseDesc.text, false, true);
    appendTestcaseInfo(desc, "Instructions", hostingTestcaseDesc.instructions, false, true);
    appendTestcaseInfo(desc, "RTM", hostingTestcaseDesc.rtm, false, true);
    appendTestcaseInfo(desc, "Underlying Specification Reference", hostingTestcaseDesc.specifications, true, false);
    desc.append("<br/><br/>");
}

function clearHostingTestcaseDescriptionContents(){
    $("#hostingTestcaseDescription").empty();
}