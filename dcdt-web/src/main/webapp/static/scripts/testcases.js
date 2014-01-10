function appendTestcaseInfo(element, label, value, asList, isString){
    if(value == ""){
        return;
    }
    if(asList){
        if(isString){
            element.append("<p><b>" + label + ": </b></p>");
            element.append("<ul><li>" + value +"</li></ul>");
            element.append("<br/>");
        }
        else{
            element.append("<p><b>" + label + ": </b></p>");
            for(var i = 0; i < value.length; i++){
                element.append("<ul><li>" + value[i] +"</li></ul>");
            }
            element.append("<br/>");
        }
    }
    else{
        element.append("<p><b>" + label + ": </b>" + value +"</p>");
    }
}