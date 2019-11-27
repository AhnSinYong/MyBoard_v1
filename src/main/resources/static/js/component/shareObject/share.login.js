const loginInfo ={
    isLogin : false
}

function getCookie(name) {
    let value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return value? value[2] : null;
}
function deleteCookie(name) {
    document.cookie = name + '=; expires=Thu, 01 Jan 1999 00:00:10 GMT;';
}

export default {
    info : loginInfo,
    method : {
        setLoginState(){
            loginInfo.isLogin = true;
        },
        setLogoutState(){
            loginInfo.isLogin = false;
        },
        logout(){
            if(confirm("Do you want logout?")){
                deleteCookie('jwt-token');
                this.setLogoutState();

            }
        },
        getParsedJwt(){
            return JSON.parse(atob(getCookie('jwt-token')));
        }
    }
}
