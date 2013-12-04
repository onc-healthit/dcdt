function setInstanceConfiguration(querySettings) {
    queryInstanceConfiguration($.extend({
        "contentType": "application/json",
        "data": $.encodeJson({
            "@type": "request",
            "items": [
                instanceConfig
            ]
        }),
        "error": function (jqXHR, textStatus, errorThrown) {
            console.error("Unable to set (status=" + textStatus + ") instance configuration" + ((errorThrown) ? (":\n" + errorThrown) : "."));
            
            addQueryErrorGlobal("Unable to set (status=" + textStatus + ") instance configuration" + ((errorThrown) ? (":\n" + errorThrown) : "."));
        },
        "success": function (data, textStatus, jqXhr) {
            var dataJson = $.encodeJson(data);
            
            if (isQuerySuccess(data)) {
                console.info("Successfully set (status=" + textStatus + ") instance configuration:\n" + dataJson);
                
                getInstanceConfiguration();
            } else {
                console.error("Unable to set (status=" + textStatus + ") instance configuration:\n" + dataJson);
                
                addQueryErrors(data);
            }
        },
        "type": "POST",
        "url": urlAdminInstanceSet
    }, querySettings));
}

function removeInstanceConfiguration(querySettings) {
    queryInstanceConfiguration($.extend({
        "error": function (jqXHR, textStatus, errorThrown) {
            console.error("Unable to remove (status=" + textStatus + ") instance configuration" + ((errorThrown) ? (":\n" + errorThrown) : "."));
            
            addQueryErrorGlobal("Unable to remove (status=" + textStatus + ") instance configuration" + ((errorThrown) ? (":\n" + errorThrown) : "."));
        },
        "success": function (data, textStatus, jqXhr) {
            var dataJson = $.encodeJson(data);
            
            if (isQuerySuccess(data)) {
                console.info("Successfully removed (status=" + textStatus + ") instance configuration:\n" + dataJson);
                
                getInstanceConfiguration();
            } else {
                console.error("Unable to remove (status=" + textStatus + ") instance configuration:\n" + dataJson);
                
                addQueryErrors(data);
            }
        },
        "type": "POST",
        "url": urlAdminInstanceRemove
    }, querySettings));
}

function getInstanceConfiguration(querySettings) {
    queryInstanceConfiguration($.extend({
        "beforeSend": function () {
            instanceConfig = null;
        },
        "complete": function () {
            if (instanceConfig) {
                instanceConfigDirInput.val(instanceConfig["directory"]);
                instanceConfigDomainInput.val(instanceConfig["domain"]);
                
                instanceConfigRmButton.show();
            } else {
                instanceConfigRmButton.hide();
            }
        },
        "error": function (jqXHR, textStatus, errorThrown) {
            console.error("Unable to get (status=" + textStatus + ") instance configuration" + ((errorThrown) ? (":\n" + errorThrown) : "."));
            
            addQueryErrorGlobal("Unable to get (status=" + textStatus + ") instance configuration" + ((errorThrown) ? (":\n" + errorThrown) : "."));
        },
        "success": function (data, textStatus, jqXhr) {
            var dataJson = $.encodeJson(data);
            
            if (isQuerySuccess(data)) {
                console.info("Successfully got (status=" + textStatus + ") instance configuration:\n" + dataJson);
                
                instanceConfig = data["items"][0];
            } else {
                console.error("Unable to get (status=" + textStatus + ") instance configuration:\n" + dataJson);
                
                addQueryErrors(data);
            }
        },
        "url": urlAdminInstance
    }, querySettings));
}

function queryInstanceConfiguration(querySettings) {
    $.ajax($.extend({
        "dataType": "json"
    }, querySettings));
}

function isQuerySuccess(data) {
    return data["status"].toLowerCase() == "success";
}

function addQueryErrors(data) {
    var dataErrors = data["errors"];
    
    if (dataErrors) {
        var dataErrorsGlobal = dataErrors["global"];
        
        if (dataErrorsGlobal) {
            dataErrorsGlobal.forEach(function (index, item) {
                addQueryErrorGlobal(item["messages"].join("<br/>"));
            });
        }
        
        var dataErrorsFields = dataErrors["fields"];
        
        if (dataErrorsFields) {
            for (var dataErrorsFieldName in dataErrorsFields) {
                if (dataErrorsFields.hasOwnProperty(dataErrorsFieldName)) {
                    addQueryErrorField(dataErrorsFieldName, dataErrorsFields[dataErrorsFieldName]["messages"].join("<br/>"));
                }
            }
        }
    }
}

function addQueryErrorField(fieldName, errorMsg) {
    formInstanceConfig.dcdt().data("dcdt").addErrorField(fieldName, errorMsg);
}

function addQueryErrorGlobal(errorMsg) {
    formInstanceConfig.dcdt().data("dcdt").addErrorGlobal(errorMsg);
}

var formInstanceConfig;
var instanceConfigDirInput, instanceConfigDomainInput;
var instanceConfigRmButton, instanceConfigSetButton;
var instanceConfig;

$(document).ready(function () {
    formInstanceConfig = $("form[name=\"admin-instance-config\"]");
    instanceConfigDirInput = $("input#admin-instance-config-dir", formInstanceConfig);
    instanceConfigDomainInput = $("input#admin-instance-config-domain", formInstanceConfig);
    instanceConfigRmButton = $("button#admin-instance-config-rm", formInstanceConfig);
    instanceConfigSetButton = $("button#admin-instance-config-set", formInstanceConfig);
    
    instanceConfigRmButton.click(function (event) {
        removeInstanceConfiguration();
    });
    
    instanceConfigSetButton.click(function (event) {
        instanceConfig = {
            "@type": "instanceConfig",
            "directory": instanceConfigDirInput.val(),
            "domain": instanceConfigDomainInput.val()
        };
        
        setInstanceConfiguration();
    });
    
    getInstanceConfiguration();
});
