(function ($) {
    $.extend($.dcdt, {
        "admin": $.extend(function () {
            return this;
        }, {
            "getServiceHub": function (serviceStatusTypeExpected) {
                return $.dcdt.beans.getBean({
                    "queryBeanSuccess": function (data, status, jqXhr) {
                        serviceHub = data["items"][0];
                    },
                    "queryBeanErrors": function (data, status, jqXhr) {
                        $.dcdt.beans.addQueryErrors(serviceHubForm, data);
                    },
                    "postQueryBean": function (jqXhr, status) {
                        $.dcdt.admin.addServiceData(serviceStatusTypeExpected, "DNS", "DNS", serviceInputDnsStatus);
                        $.dcdt.admin.addServiceData(serviceStatusTypeExpected, "LDAP", "LDAP", serviceInputLdapStatus);
                        $.dcdt.admin.addServiceData(serviceStatusTypeExpected, "MAIL", "mail", serviceInputMailStatus);
                    },
                    "preQueryBean": function (jqXhr, settings) {
                        serviceHub = null;
                        
                        $.dcdt.beans.clearBeanMessages(serviceHubForm);
                    },
                    "url": URL_ADMIN_SERVICE_HUB_GET
                });
            },
            "addServiceData": function (serviceStatusTypeExpected, serviceType, serviceTypeDisplay, serviceInput) {
                serviceInput.attr("disabled", "disabled");
                
                if (!serviceHub) {
                    serviceInput.val("UNKNOWN");
                    
                    return;
                }
                
                var serviceFieldName = serviceInput.attr("name"), serviceStatusType = serviceHub["serviceMap"][serviceType]["statusType"], serviceStatus =
                    (serviceStatusType == serviceStatusTypeExpected), serviceStatusStr = (serviceStatus ? "success" : "error"), serviceMsgs =
                    serviceHub["serviceMsgsMap"][serviceType];
                
                serviceInput.val(serviceStatusType);
                
                if (serviceStatus) {
                    if (serviceStatusType == "STARTED") {
                        $.dcdt.beans.addBeanMessageField(serviceHubForm, serviceFieldName, serviceStatusStr, ("Successfully started " + serviceTypeDisplay + " service."));
                    } else {
                        $.dcdt.beans.addBeanMessageField(serviceHubForm, serviceFieldName, serviceStatusStr, ("Successfully stopped " + serviceTypeDisplay + " service."));
                    }
                } else {
                    if (serviceStatusType == "STARTED") {
                        $.dcdt.beans.addBeanMessageField(serviceHubForm, serviceFieldName, serviceStatusStr, ("Unable to start " + serviceTypeDisplay + " service."));
                    } else {
                        $.dcdt.beans.addBeanMessageField(serviceHubForm, serviceFieldName, serviceStatusStr, ("Unable to stop " + serviceTypeDisplay + " service."));
                    }
                }
                
                if (serviceMsgs) {
                    $.each(serviceMsgs, function (serviceMsgIndex, serviceMsg) {
                        $.dcdt.beans.addBeanMessageField(serviceHubForm, serviceFieldName, serviceStatusStr, serviceMsg);
                    });
                }
            },
            "setInstanceConfig": function () {
                return $.dcdt.beans.setBean({
                    "data": $.encodeJson({
                        "@type": "request",
                        "items": [ instanceConfig ]
                    }),
                    "queryBeanSuccess": function (data, status, jqXhr) {
                        $.dcdt.admin.getInstanceConfig();
                        
                        $.dcdt.beans.addBeanMessageGlobal(instanceConfigForm, "success", "Successfully set instance configuration.");
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
                            
                            $.dcdt.admin.getServiceHub("STARTED");
                        } else {
                            instanceConfigButtonRm.attr("disabled", "disabled");
                            
                            $.dcdt.admin.getServiceHub("STOPPED");
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
    
    var serviceHubForm, serviceInputDnsStatus, serviceInputLdapStatus, serviceInputMailStatus, serviceHub;
    var instanceConfigForm, instanceConfigInputDomainName, instanceConfigInputIpAddr, instanceConfigButtons, instanceConfigButtonRm, instanceConfigButtonSet, instanceConfig;
    
    $(document).ready(function () {
        serviceHubForm = $("form[name=\"admin-service-hub\"]");
        serviceInputDnsStatus = serviceHubForm.dcdt.form.formInputs("serviceDnsStatus");
        serviceInputLdapStatus = serviceHubForm.dcdt.form.formInputs("serviceLdapStatus");
        serviceInputMailStatus = serviceHubForm.dcdt.form.formInputs("serviceMailStatus");
        
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
