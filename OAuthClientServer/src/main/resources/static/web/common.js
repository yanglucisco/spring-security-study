function test(){
    alert('测试');
}

// 生成 code_verifier
function generateCodeVerifier() {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~';
    let result = '';
    for (let i = 0; i < 64; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
}

// 计算 code_challenge（S256）
// 计算 code_challenge（S256）
function generateCodeChallenge(codeVerifier) {
    const encoder = new TextEncoder();
    const data = encoder.encode(codeVerifier);
    const digest =  window.crypto.subtle.digest('SHA-256', data);
    return btoa(String.fromCharCode(...new Uint8Array(digest)))
        .replace(/\+/g, '-')
        .replace(/\//g, '_')
        .replace(/=+$/, '');
}

function startAuthro(){
const codeVerifier = generateCodeVerifier();
const codeChallenge =  generateCodeChallenge(codeVerifier);
localStorage.setItem('pkce_verifier', codeVerifier); // 临时存储

// 重定向到授权端点
window.location.href = `http://auth-server:9000/oauth2/authorize?
    response_type=code&
    client_id=pkce-client&
    redirect_uri=${encodeURIComponent('http://localhost:8080/web/callback.html')}&
    scope=openid pkce&
    code_challenge=${codeChallenge}&
    code_challenge_method=S256`;

}

async function generateHash(text) {
    // 将文本编码为 UTF-8 字节数组
    const encoder = new TextEncoder();
    const data = encoder.encode(text);

    // 生成哈希值
    const hashBuffer = await window.crypto.subtle.digest('SHA-256', data);

    // 将哈希值转换为十六进制字符串
    const hashArray = Array.from(new Uint8Array(hashBuffer));
    const hashHex = hashArray.map(byte => byte.toString(16).padStart(2, '0')).join('');

    return hashHex;
}

// 使用示例
generateHash('Hello, World!').then(hash => {
    console.log('Hash:', hash);
});

async function sendPostRequest() {
    const apiUrl = 'http://auth-client:8080/test_post'; // 替换为你的API地址
    const requestData = { key: 'value' }; // 需要发送的数据
    try {
        const response = await fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json' // 设置请求头为JSON格式
            },
            body: JSON.stringify(requestData) // 将数据转为JSON字符串
        });
        if (response.ok) {
            const data = await response.json(); // 解析响应数据
            console.log('Response:', data);
        } else {
            throw new Error(`Server responded with status ${response.status}`);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}



