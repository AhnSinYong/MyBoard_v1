const loginInfo ={
    isLogin : false
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
                this.deleteCookie('jwt-token');
                this.setLogoutState();

            }
        },
        getParsedJwt(){
            return JSON.parse(atob(getCookie('jwt-token')));
        },

        getCookie: function (name) {
            let value = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
            return value? value[2] : null;
        },
        deleteCookie: function (name) {
            document.cookie = name + '=; expires=Thu, 01 Jan 1999 00:00:10 GMT;';
        }

    }
}
