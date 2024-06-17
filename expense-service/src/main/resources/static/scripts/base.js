var accessToken = localStorage.getItem('accessToken');
var refreshToken = localStorage.getItem('refreshToken');

// Check if tokens exist
if (accessToken && refreshToken) {
    // Set up AJAX requests to include the Authorization header globally
    $.ajaxSetup({
        beforeSend: function (xhr) {
            xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
        }
    });
}