$(document).ready(function () {
    var hostingForm = $("form[name='hosting-form']");

    $("#hostingTestcases").change(function () {
        var hostingTestcaseName = $("#hostingTestcases option:selected").val();
        if(hostingTestcaseName != ""){
            var hostingTestcase = HOSTING_TESTCASES[hostingTestcaseName];
            displayHostingTestcaseDescription(hostingTestcase);
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

function displayHostingTestcaseDescription(hostingTestcase){
    var hostingTestcaseDesc = hostingTestcase.description, desc = $("#hostingTestcaseDescription");
    desc.empty();
    
    appendTestcaseInfo(desc, "Description", hostingTestcaseDesc.text, false, true);
    appendTestcaseInfo(desc, "Instructions", hostingTestcaseDesc.instructions, false, true);
    appendTestcaseInfo(desc, "RTM Sections", hostingTestcaseDesc.rtmSections.join(", "), false, true);
    appendTestcaseInfo(desc, "Underlying Specification Reference", hostingTestcaseDesc.specifications, true, false);
    
    desc.append("<br/><br/>");
}

function clearHostingTestcaseDescriptionContents(){
    $("#hostingTestcaseDescription").empty();
}