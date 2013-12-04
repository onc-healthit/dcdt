$(document).ready(function () {
    $.extend(jQuery, {
        "encodeJson": function (value, replacer, space) {
            return JSON.stringify(value, replacer, (space ? space : "    "));
        }
    });
});
