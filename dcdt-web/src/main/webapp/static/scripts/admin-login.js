$(document).ready(function () {
    $("button#admin-login-process").click(function (event) {
        $(event.target).parent().submit();
    });
});
