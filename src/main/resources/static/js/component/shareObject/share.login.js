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

                axios.post('/api/account/signOut')

            }
        },
        getParsedJwt(){
            let token = getCookie('jwt-token').split('\.');
            const header = JSON.parse(atob(token[0]));
            const claim = JSON.parse(atob(token[1]));

            return {
                header : header,
                claim : claim
            }
        },
        isLogin(){
            let state = false;
            if(this.getCookie('jwt-token'))
                state = true;
            return state;
        },
        checkLogin(){
            if(this.isLogin()){
                this.setLoginState()
                return;
            }
            this.setLogoutState();
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
