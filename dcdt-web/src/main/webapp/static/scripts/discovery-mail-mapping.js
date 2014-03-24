(function ($) {
    $.extend($.dcdt, {
        "discoveryMailMapping": $.extend(function () {
            return this;
        }, {
            "processDiscoveryMailMapping": function () {
                return $.dcdt.beans.setBean({
                    "data": $.encodeJson({
                        "@type": "request",
                        "items": [
                            discoveryMailMapping
                        ]
                    }),
                    "queryBeanSuccess": function (data, status, jqXhr) {
                        $.dcdt.discoveryMailMapping.displayMailMappingResults(data);
                    },
                    "queryBeanErrors": function (data, status, jqXhr) {
                        $.dcdt.beans.addQueryErrors(formDiscoveryMailMapping, data);
                    },
                    "postQueryBean": function (jqXhr, status) {
                        formDiscoveryMailMapping.dcdt.form.formReady();
                    },
                    "preQueryBean": function (jqXhr, settings) {
                        $.dcdt.beans.clearBeanErrors(formDiscoveryMailMapping);
                    },
                    "url": URL_DISCOVERY_MAIL_MAPPING_ADD
                });
            },
            "displayMailMappingResults": function (data) {
                var mailMapping = data["items"][0];
                var directAddr = mailMapping["directAddr"];
                var resultsAddr = mailMapping["resultsAddr"];
                var message = mailMapping["msg"];

                mailMappingResults.empty();
                mailMappingResults.append(message);
            }
        })
    });

    var formDiscoveryMailMapping, directAddr, resultsAddr, discoveryMailMappingSubmit, discoveryMailMappingReset,
        discoveryMailMapping, mailMappingResults;

    $(document).ready(function () {
        formDiscoveryMailMapping = $("form[name=\"form-testcases-discovery-mail-mapping\"]");
        directAddr = $("input[name=\"directAddress\"]", formDiscoveryMailMapping);
        resultsAddr = $("input[name=\"resultsAddress\"]", formDiscoveryMailMapping);
        discoveryMailMappingSubmit = $("button#discovery-mail-mapping-submit");
        discoveryMailMappingReset = $("button#discovery-mail-mapping-reset");
        mailMappingResults = $("div#mail-mapping-results");

        formDiscoveryMailMapping.submit(function (event) {
            discoveryMailMapping = {
                "@type": "discoveryTestcaseMailMapping",
                "directAddr": directAddr.val(),
                "resultsAddr": resultsAddr.val()
            };
            $.dcdt.discoveryMailMapping.processDiscoveryMailMapping();

            event.preventDefault();
            event.stopPropagation();
        });

        discoveryMailMappingSubmit.click(function (event) {
            formDiscoveryMailMapping.submit();
        });

        discoveryMailMappingReset.click(function (event) {
            directAddr.val("");
            resultsAddr.val("");
            mailMappingResults.empty();
            $.fn.dcdt.form.clearErrors();
        });
    });
})(jQuery);
