//import { initiateAuthFlow } from './OAuth2Common.js'

function init(){
    let isFromAuth = isFromAuthServer();
    if(isFromAuth.r === true){
        //如果是从授权服务器跳转回来的，则开始获取token
//        sessionStorage.setItem("token", "token");
//        exchangeToken();
        console.log(isFromAuth.code);
        console.log('从授权服务器回调回来的');
        exchangeToken(isFromAuth.code);
        loadContent();
    }
    else{
        //不是从授权服务器跳转回来的
        //如果没有获取到token，则转到授权服务器登录页面
            if(!sessionStorage.getItem("token")){
//                initiateAuthFlow();
                    location.href = 'http://auth-server:9000/oauth2/authorize?response_type=code&client_id=pkce-client&scope=openid pkce&redirect_uri=http://auth-gateway:10000/index.html&code_challenge_method=S256&code_challenge=hQqHvGROSi0bvuXVAUXnSj1ZN1p1pDTpnKy5HZvvAso';

                return;
        //            location.href = "http://www.bing.com.cn";
            }
            //已经获取到token了，直接显示内容
            else{
                loadContent();
            }
    }
}

function init1(){
const queryString = window.location.search;
const params = new URLSearchParams(queryString);

const code = params.get('code'); // "alice"
const state = params.get('state'); // "25"

console.log('state:', state);
console.log('code:', code);

exchangeToken(code);
//    location.href = "http://auth-server:9000/oauth2/authorize?response_type=code&client_id=pkce-client&scope=openid pkce&redirect_uri=http://auth-gateway:10000/index.html&code_challenge_method=S256&code_challenge=hQqHvGROSi0bvuXVAUXnSj1ZN1p1pDTpnKy5HZvvAso";
}

function init2(){
const queryString = window.location.search;
const params = new URLSearchParams(queryString);
    const code = params.get('code');
    if(code){
        exchangeToken(code);
    }
    else
    {
        location.href = 'http://auth-server:9000/oauth2/authorize?response_type=code&client_id=pkce-client&scope=openid pkce&redirect_uri=http://auth-gateway:10000/index.html&code_challenge_method=S256&code_challenge=hQqHvGROSi0bvuXVAUXnSj1ZN1p1pDTpnKy5HZvvAso';
    }
}

function loadContent(){
    console.log('已经获取到了token，可以展示内容了');
}

function test(){
    sessionStorage.setItem("test", "测试");
}

function test1(){
    console.log(sessionStorage.getItem("test"));
    if(!sessionStorage.getItem("test1")){
        location.href = "http://www.bing.com.cn";
    }
}

function isFromAuthServer(){
console.log('开始初始化');
const queryString = window.location.search;
const params = new URLSearchParams(queryString);

const code = params.get('code'); // "alice"
//const state = params.get('state'); // "25"
if(code){
    return {
            r: true,
            code: code
//            state: state
        }
    }
    return {
        r: false
    }
}

init();