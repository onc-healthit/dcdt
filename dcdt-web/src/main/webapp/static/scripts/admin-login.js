$(document).ready(function () {
    var formLogin = $("form[name=\"admin-login\"]");
    
    $("button#admin-login-process").click(function (event) {
        formLogin.submit();
    });
});
