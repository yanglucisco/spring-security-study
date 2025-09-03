async function test() {
$.ajax({
    url: 'http://127.0.0.1:8082/test',
    type: 'GET',
    dataType: 'json',
    crossDomain: true, // 显式声明跨域
    success: function(data) {
        console.log(data);
    },
    error: function(xhr, status, error) {
        console.error(error);
    }
});

//    try {
//        const response = await fetch('http://127.0.0.1:8082/test', {
//            method: 'get',
//            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
//            credentials: 'include'
////            body
//        });
//        const tokens = await response.json();
//        console.log('Access Token:', tokens);
//    } catch (error) {
//        console.error('Token exchange failed:', error);
//    }
}