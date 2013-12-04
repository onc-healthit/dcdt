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
            // TODO: implement UI error reporting
            console.error("Unable to set instance configuration (status=" + textStatus + ")" + ((errorThrown) ? (":\n" + errorThrown) : "."));
        },
        "success": function (data, textStatus, jqXhr) {
            console.info("Successfully (status=" + textStatus + ") set instance configuration:\n" + $.encodeJson(data));
            
            getInstanceConfiguration();
        },
        "type": "POST",
        "url": urlAdminInstanceSet
    }, querySettings));
}

function removeInstanceConfiguration(querySettings) {
    queryInstanceConfiguration($.extend({
        "error": function (jqXHR, textStatus, errorThrown) {
            // TODO: implement UI error reporting
            console.error("Unable to remove instance configuration (status=" + textStatus + ")" + ((errorThrown) ? (":\n" + errorThrown) : "."));
        },
        "success": function (data, textStatus, jqXhr) {
            console.info("Successfully (status=" + textStatus + ") removed instance configuration:\n" + $.encodeJson(data));
            
            getInstanceConfiguration();
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
                dirInput.val(instanceConfig["directory"]);
                domainInput.val(instanceConfig["domain"]);
                
                rmButton.show();
            } else {
                rmButton.hide();
            }
        },
        "error": function (jqXHR, textStatus, errorThrown) {
            // TODO: implement UI error reporting
            console.error("Unable to get instance configuration (status=" + textStatus + ")" + ((errorThrown) ? (":\n" + errorThrown) : "."));
        },
        "success": function (data, textStatus, jqXhr) {
            console.info("Successfully (status=" + textStatus + ") got instance configuration:\n" + $.encodeJson(data));
            
            instanceConfig = data["items"][0];
        },
        "url": urlAdminInstance
    }, querySettings));
}

function queryInstanceConfiguration(querySettings) {
    $.ajax($.extend({
        "dataType": "json"
    }, querySettings));
}

var formInstance;
var dirInput, domainInput;
var rmButton, setButton;
var instanceConfig;

$(document).ready(function () {
    formInstance = $("form[name=\"admin-instance-form\"]");
    dirInput = $("input#admin-instance-dir", formInstance);
    domainInput = $("input#admin-instance-domain", formInstance);
    rmButton = $("button#admin-instance-rm", formInstance);
    setButton = $("button#admin-instance-set", formInstance);
    
    rmButton.click(function (event) {
        removeInstanceConfiguration();
    });
    
    setButton.click(function (event) {
        instanceConfig = {
            "@type": "instanceConfig",
            "directory": dirInput.val(),
            "domain": domainInput.val()
        };
        
        setInstanceConfiguration();
    });
    
    getInstanceConfiguration();
});
