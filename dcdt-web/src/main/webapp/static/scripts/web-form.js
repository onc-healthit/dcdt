(function ($) {
    $.extend($.fn.dcdt, {
        "form": $.extend(function () {
            return this;
        }, {
            "addMessageField": function (fieldName, level, msg, preFormatted) {
                var elem = $(this);
                
                elem.dcdt.form.formGroupsFields(fieldName).find("div:first-child").enableClass("has-" + level);
                
                return elem.dcdt.form.addMessage(elem.dcdt.form.messagesAddonsFields(level, fieldName), msg, preFormatted);
            },
            "addMessageGlobal": function (level, msg, preFormatted) {
                var elem = $(this);
                
                return elem.dcdt.form.addMessage(elem.dcdt.form.messagesAddonsGlobal(level), msg, preFormatted);
            },
            "addMessage": function (msgsAddons, msg, preFormatted) {
                if (!msg) {
                    return msgsAddons;
                }
                
                msgsAddons.enableClass("input-group-addon-active");
                
                var msgsAddonsListItem = $("<li/>");
                
                if (preFormatted) {
                    msgsAddonsListItem.append($("<pre/>").text(msg));
                } else {
                    msgsAddonsListItem.text(msg);
                }
                
                msgsAddons.find("ul").append(msgsAddonsListItem);
                
                return msgsAddons;
            },
            "clearMessages": function () {
                var elem = $(this), msgsAddons = elem.dcdt.form.messagesAddons();
                msgsAddons.disableClass("input-group-addon-active");
                msgsAddons.find("ul").empty();
                
                elem.dcdt.form.formGroupsFields(null).find("div:first-child").removeClass();
                
                return msgsAddons;
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
            "messagesAddonsFields": function (level, fieldName) {
                var msgsAddonsFields = $(this).dcdt.form.messagesAddons(level, ":not(.input-group-addon-msgs-global)");
                
                return fieldName ? msgsAddonsFields.filter(function () {
                    return $(this).parent().parent().prev().find("div:first-child span.form-cell.form-cell-control .form-control[name=\"" + fieldName + "\"]").length > 0;
                }) : msgsAddonsFields;
            },
            "messagesAddonsGlobal": function (level) {
                return $(this).dcdt.form.messagesAddons(level, ".input-group-addon-msgs-global");
            },
            "messagesAddons": function (level, selector) {
                var msgsAddons = $(this).dcdt.form.formGroupsAddons().find("div.input-group-addon-msgs");
                msgsAddons = (level ? msgsAddons.filter(function () {
                    return $(this).parent().hasClass("has-" + level);
                }) : msgsAddons);
                
                return selector ? msgsAddons.filter(selector) : msgsAddons;
            },
            "formInputs": function (fieldName) {
                return $(this).dcdt.form.formGroupsFields(fieldName).find(".form-control");
            },
            "formGroupsFields": function (fieldName) {
                var formGroupsFields = $(this).dcdt.form.formGroups(":not(.form-group-addons)");
                
                return fieldName ? formGroupsFields.has("div:first-child span.form-cell.form-cell-control .form-control[name=\"" + fieldName + "\"]") : formGroupsFields;
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
