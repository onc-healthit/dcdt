(function ($) {
    $.extend($.dcdt, {
        "admin": $.extend(function () {
            return this;
        }, {
            "setInstanceConfig": function () {
                return $.dcdt.beans.setBean({
                    "data": $.encodeJson({
                        "@type": "request",
                        "items": [
                            instanceConfig
                        ]
                    }),
                    "queryBeanSuccess": function (data, status, jqXhr) {
                        $.dcdt.admin.getInstanceConfig();
                        
                        $.dcdt.beans.addBeanMessageGlobal(instanceConfigForm, "success", "Successfully set instance configuration.");
                        $.dcdt.beans.addBeanMessageGlobal(instanceConfigForm, "success", ((data["items"].length > 0) ? data["items"][0]["msg"] : null));
                    },
                    "queryBeanErrors": function (data, status, jqXhr) {
                        $.dcdt.beans.addQueryErrors(instanceConfigForm, data);
                        
                        $.dcdt.beans.addBeanMessageGlobal(instanceConfigForm, "error", "Unable to set instance configuration.");
                    },
                    "postQueryBean": function (jqXhr, status) {
                        instanceConfigForm.dcdt.form.formReady();
                    },
                    "preQueryBean": function (jqXhr, settings) {
                        $.dcdt.beans.clearBeanMessages(instanceConfigForm);
                        instanceConfigForm.dcdt.form.formWait(instanceConfigButtonSet);
                    },
                    "url": URL_ADMIN_INSTANCE_CONFIG_SET
                });
            },
            "removeInstanceConfig": function () {
                return $.dcdt.beans.removeBean({
                    "queryBeanSuccess": function (data, status, jqXhr) {
                        $.dcdt.admin.getInstanceConfig();
                        
                        $.dcdt.beans.addBeanMessageGlobal(instanceConfigForm, "success", "Successfully removed instance configuration.");
                    },
                    "queryBeanErrors": function (data, status, jqXhr) {
                        $.dcdt.beans.addQueryErrors(instanceConfigForm, data);
                        
                        $.dcdt.beans.addBeanMessageGlobal(instanceConfigForm, "error", "Unable to remove instance configuration.");
                    },
                    "postQueryBean": function (jqXhr, status) {
                        instanceConfigForm.dcdt.form.formReady();
                    },
                    "preQueryBean": function (jqXhr, settings) {
                        $.dcdt.beans.clearBeanMessages(instanceConfigForm);
                        instanceConfigForm.dcdt.form.formWait(instanceConfigButtonRm);
                    },
                    "url": URL_ADMIN_INSTANCE_CONFIG_RM
                });
            },
            "getInstanceConfig": function () {
                return $.dcdt.beans.getBean({
                    "queryBeanSuccess": function (data, status, jqXhr) {
                        instanceConfig = data["items"][0];
                    },
                    "queryBeanErrors": function (data, status, jqXhr) {
                        $.dcdt.beans.addQueryErrors(instanceConfigForm, data);
                    },
                    "postQueryBean": function (jqXhr, status) {
                        var instanceConfigDomainName, instanceConfigIpAddr;
                        
                        if (instanceConfig) {
                            instanceConfigInputDomainName.val(instanceConfigDomainName = instanceConfig["domainName"]);
                            instanceConfigInputIpAddr.val(instanceConfigIpAddr = instanceConfig["ipAddr"]);
                        } else {
                            instanceConfigInputDomainName.val("");
                            instanceConfigInputIpAddr.val("");
                        }

                        if (instanceConfigDomainName && instanceConfigIpAddr) {
                            instanceConfigButtonRm.removeAttr("disabled");
                        } else {
                            instanceConfigButtonRm.attr("disabled", "disabled");
                        }
                    },
                    "preQueryBean": function (jqXhr, settings) {
                        instanceConfig = null;
                        
                        $.dcdt.beans.clearBeanMessages(instanceConfigForm);
                    },
                    "url": URL_ADMIN_INSTANCE_CONFIG_GET
                });
            }
        })
    });
    
    var instanceConfigForm, instanceConfigInputDomainName, instanceConfigInputIpAddr, instanceConfigButtons, instanceConfigButtonRm, instanceConfigButtonSet,
        instanceConfig;
    
    $(document).ready(function () {
        instanceConfigForm = $("form[name=\"admin-instance-config\"]");
        instanceConfigInputDomainName = instanceConfigForm.dcdt.form.formInputs("domainName");
        instanceConfigInputIpAddr = instanceConfigForm.dcdt.form.formInputs("ipAddress");
        instanceConfigButtons = instanceConfigForm.dcdt.form.formButtons();
        instanceConfigButtonRm = instanceConfigForm.dcdt.form.formButtons("#admin-instance-config-rm");
        instanceConfigButtonSet = instanceConfigForm.dcdt.form.formButtons("#admin-instance-config-set");

        instanceConfigForm.submit(function (event, instanceConfigButton) {
            if (instanceConfigButton.is(instanceConfigButtonRm)) {
                $.dcdt.admin.removeInstanceConfig();
            } else if (instanceConfigButton.is(instanceConfigButtonSet)) {
                instanceConfig = {
                    "@type": "instanceConfig",
                    "domainName": instanceConfigInputDomainName.val(),
                    "ipAddr": instanceConfigInputIpAddr.val()
                };
                
                $.dcdt.admin.setInstanceConfig();
            }
            
            event.preventDefault();
            event.stopPropagation();
        });
        
        instanceConfigButtons.click(function (event) {
            var instanceConfigButton = $(event.target);
            
            if (!instanceConfigButton.attr("disabled")) {
                instanceConfigForm.trigger("submit", [ instanceConfigButton ]);
            }
        });
        
        $.dcdt.admin.getInstanceConfig();
    });
})(jQuery);
