

async function exchangeToken(code) {
    const verifier = 'gJqcM5dkGg1IZAZN92hhVjaVOORXuLyfkVn1PFbNucs';// sessionStorage.getItem('pkce_verifier');
    const tokenUrl = 'http://auth-server:9000/oauth2/token';

    const body = new URLSearchParams({
        grant_type: 'authorization_code',
        code,
        redirect_uri: 'http://auth-client:8080/web/callback123.html',
        client_id: 'pkce-client',
        code_verifier: verifier // 关键：验证身份[3](@ref)[6](@ref)[10](@ref)
    });

    try {
        const response = await fetch(tokenUrl, {
            method: 'POST',
            mode: 'cors',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body
        });
        const tokens = await response.json();
        console.log('Access Token:', tokens.access_token);
    } catch (error) {
        console.error('Token exchange failed:', error);
    }
}

async function test() {

    try {
        const response = await fetch('http://auth-server:9000/test', {
            method: 'get',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
//            body
        });
        const tokens = await response.json();
        console.log('Access Token:', tokens);
    } catch (error) {
        console.error('Token exchange failed:', error);
    }
}




