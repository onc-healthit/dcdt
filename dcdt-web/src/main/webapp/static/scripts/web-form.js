(function ($) {
    $.extend($.fn.dcdt, {
        "form": $.extend(function () {
            return this;
        }, {
            "addErrorField": function (fieldName, errorMsg, preFormatted) {
                var elem = $(this);
                
                elem.dcdt.form.formGroupsFields(fieldName).find("div:first-child").enableClass("has-error");
                
                return elem.dcdt.form.addError(elem.dcdt.form.errorsAddonsFields(fieldName), errorMsg, preFormatted);
            },
            "addErrorGlobal": function (errorMsg, preFormatted) {
                var elem = $(this);
                
                return elem.dcdt.form.addError(elem.dcdt.form.errorsAddonsGlobal(), errorMsg, preFormatted);
            },
            "addError": function (errorsAddons, errorMsg, preFormatted) {
                errorsAddons.enableClass("input-group-addon-active");
                
                var errorsAddonsListItem = $("<li/>");
                
                if (preFormatted) {
                    errorsAddonsListItem.append($("<pre/>").text(errorMsg));
                } else {
                    errorsAddonsListItem.text(errorMsg);
                }
                
                errorsAddons.find("ul").append(errorsAddonsListItem);
                
                return errorsAddons;
            },
            "clearErrors": function () {
                var elem = $(this), errorsAddons = elem.dcdt.form.errorsAddons();
                errorsAddons.disableClass("input-group-addon-active");
                errorsAddons.find("ul").empty();
                
                elem.dcdt.form.formGroupsFields(null).find("div:first-child").removeClass();
                
                return errorsAddons;
            },
            "formReady": function () {
                var elem = $(this);
                elem.dcdt.form.formInputs().removeAttr("disabled");
                
                var formButtons = elem.dcdt.form.formButtons();
                formButtons.removeAttr("disabled");
                formButtons.find("span.glyphicon").removeClass("fa").removeClass("fa-cog").removeClass("fa-spin");
                
                return elem;
            },
            "formWait": function (formButton) {
                var elem = $(this);
                elem.dcdt.form.formInputs().attr("disabled", "disabled");
                elem.dcdt.form.formButtons().attr("disabled", "disabled");
                
                formButton.find("span.glyphicon").addClass("fa").addClass("fa-cog").addClass("fa-spin");
                
                return elem;
            },
            "formButtons": function (selector) {
                var formButtons = $(this).find("button.btn");
                
                return selector ? formButtons.filter(selector) : formButtons;
            },
            "errorsAddonsFields": function (fieldName) {
                var errorsAddonsFields = $(this).dcdt.form.errorsAddons(":not(.input-group-addon-errors-global)");
                
                return fieldName ? errorsAddonsFields.filter(function () {
                    return $(this).parent().parent().prev().find("div:first-child span.form-cell.form-cell-control *.form-control[name=\"" + fieldName +"\"]")
                        .length > 0;
                }) : errorsAddonsFields;
            },
            "errorsAddonsGlobal": function () {
                return $(this).dcdt.form.errorsAddons(".input-group-addon-errors-global");
            },
            "errorsAddons": function (selector) {
                var errorsAddons = $(this).dcdt.form.formGroupsAddons().find("div.has-error div.input-group-addon-errors");
                
                return selector ? errorsAddons.filter(selector) : errorsAddons;
            },
            "formInputs": function (fieldName) {
                return $(this).dcdt.form.formGroupsFields(fieldName).find("input.form-control");
            },
            "formGroupsFields": function (fieldName) {
                var formGroupsFields = $(this).dcdt.form.formGroups(":not(.form-group-addons)");
                
                return fieldName ? formGroupsFields.has("div:first-child span.form-cell.form-cell-control *.form-control[name=\"" + fieldName +"\"]") : 
                    formGroupsFields;
            },
            "formGroupsAddons": function (selector) {
                var formGroupsAddons = $(this).dcdt.form.formGroups(".form-group-addons");
                
                return selector ? formGroupsAddons.filter(selector) : formGroupsAddons;
            },
            "formGroups": function (selector) {
                var formGroups = $(this).dcdt.form.inputGroups().find("div.form-group");
                
                return selector ? formGroups.filter(selector) : formGroups;
            },
            "inputGroups": function () {
                return $(this).find("div.input-group-sm");
            }
        })
    });
})(jQuery);
