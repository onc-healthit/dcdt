$(document).ready(function () {
    var hostingForm = $("form[name='hosting-form']");

    $("#hostingTestcases").change(function () {
        var hostingTestcaseName = $("#hostingTestcases option:selected").val();
        if(hostingTestcaseName != ""){
            var hostingTestcase = HOSTING_TESTCASES[hostingTestcaseName];
            displayHostingTestcaseDescription(hostingTestcase.testcaseDescription);
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
    desc.append("<p><b>Description: </b>" + hostingTestcaseDesc.description +"</p>");
    desc.append("<p><b>Instructions: </b>" + hostingTestcaseDesc.instructions +"</p>");
    desc.append("<p><b>RTM: </b>" + hostingTestcaseDesc.rtm +"</p>");
    desc.append("<p><b>Underlying Specification Reference: </b></p>");
    for(var i = 0; i < hostingTestcaseDesc.specifications.length; i++){
        desc.append("<ul><li>" + hostingTestcaseDesc.specifications[i] +"</li></ul>");
    }
    desc.append("<br/><br/>");
}

function clearHostingTestcaseDescriptionContents(){
    $("#hostingTestcaseDescription").empty();
}