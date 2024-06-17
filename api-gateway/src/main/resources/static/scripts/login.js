window.onload = function () {
    requestBotLogin()
}

function requestBotLogin() {
    $.ajax({
        method: 'GET',
        url: './login/bot-login',
        contentType: "application/json; charset=utf8",
        async: false,
        success: function (data) {
            console.log(data)
            const script = document.createElement('script')
            script.setAttribute('src', 'https://telegram.org/js/telegram-widget.js?22')
            script.async = true;
            script.setAttribute('data-telegram-login', data.toString())
            script.setAttribute("data-size", "large")
            script.setAttribute("data-onauth", "onTelegramAuth(user)")
            script.setAttribute("data-request-access", "write")
            document.getElementById("login-form").appendChild(script);
        }
    })
}

function onTelegramAuth(user) {
    let userJson = JSON.stringify(user);
    $.ajax({
        method: 'POST',
        url: '/auth/login',
        data: userJson,
        contentType: "application/json; charset=utf8",
        success: function (response) {
            console.log("Successfully authenticated")
            console.log(response.accessToken);
            localStorage.setItem('accessToken', response.accessToken);
            localStorage.setItem('refreshToken', response.refreshToken);

            location.href = "http://" + location.hostname + "/tracker";
        },
        error: function () {
            console.log("Authentication was failed")
        }
    })
}