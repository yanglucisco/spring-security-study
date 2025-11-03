
async function exchangeToken(code) {
    const verifier = '8SkwXEJUZJVQLScWYs8nV9bhv4GfvnHmc9iuApguEwY';// sessionStorage.getItem('pkce_verifier');
    const tokenUrl = 'http://auth-gateway:10000/oauth2/token';

    const body = new URLSearchParams({
        grant_type: 'authorization_code',
        code,
        redirect_uri: 'http://auth-gateway:10000/index.html',
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
        sessionStorage.setItem("token", tokens.access_token);
    } catch (error) {
        console.error('Token exchange failed:', error);
    }
}

async function test() {

    try {
        const response = await fetch('http://auth-gateway:10000/test', {
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

function generateCodeVerifier() {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~';
    let result = '';
    // 生成43-128字符的随机字符串（推荐长度64）
    for (let i = 0; i < 64; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
}

async function generateCodeChallenge(codeVerifier) {
    const encoder = new TextEncoder();
    const data = encoder.encode(codeVerifier);
    const digest = await window.crypto.subtle.digest('SHA-256', data);
    // 计算 SHA-256 哈希
//    const digest = CryptoJS.SHA256(data).toString();
    console.log(digest);
    let r = base64UrlEncode(digest);
    return r;
}

function base64UrlEncode(buffer) {
    let r = btoa(String.fromCharCode(...new Uint8Array(buffer)))
        .replace(/\+/g, '-')
        .replace(/\//g, '_')
        .replace(/=/g, '');
        debugger
    return r;

//    return btoa(String.fromCharCode(...new Uint8Array(hash)))
//    .replace(/\+/g, '-')
//    .replace(/\//g, '_')
//    .replace(/=+$/, '');
}
//<!--http://auth-server:9000/oauth2/authorize?
//response_type=code
//&client_id=pkce-client
//&scope=openid pkce
//&redirect_uri=http://auth-gateway:10000/callback.html
//&code_challenge_method=S256
//&code_challenge=t1RX1ya8v4PfqZQxoS594rIMj3AiANS_PUotENUffO0-->

async function initiateAuthFlow() {
    const codeVerifier = '8SkwXEJUZJVQLScWYs8nV9bhv4GfvnHmc9iuApguEwY';// generateCodeVerifier();
    const codeChallenge = 'hQqHvGROSi0bvuXVAUXnSj1ZN1p1pDTpnKy5HZvvAso';// generateCodeChallenge(codeVerifier);

    // 存储 verifier 供后续使用（推荐 sessionStorage）
    sessionStorage.setItem('pkce_verifier', codeVerifier);

    // 构造授权 URL
    const authUrl = new URL('http://auth-server:9000/oauth2/authorize');
    authUrl.searchParams.append('response_type', 'code');
    authUrl.searchParams.append('client_id', 'pkce-client');
    authUrl.searchParams.append('redirect_uri', encodeURIComponent('http://auth-gateway:10000/index.html'));
    authUrl.searchParams.append('code_challenge', codeChallenge);
    authUrl.searchParams.append('code_challenge_method', 'S256');
    authUrl.searchParams.append('scope', 'openid pkce');

    // 跳转到授权服务器
    let urlS = authUrl.toString();
    console.log(authUrl.toString());
    window.location.href = 'http://auth-server:9000/oauth2/authorize?response_type=code&client_id=pkce-client&scope=openid pkce&redirect_uri=http://auth-gateway:10000/index.html&code_challenge_method=S256&code_challenge=hQqHvGROSi0bvuXVAUXnSj1ZN1p1pDTpnKy5HZvvAso';// authUrl.toString();
}





