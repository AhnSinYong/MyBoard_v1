const loginInfo ={
    isLogin : false,
    email : '',
    nickname :'',
    isSocial : false,
}

export default {
    info : loginInfo,
    method : {
        setLoginState(){
            loginInfo.isLogin = true;
            loginInfo.email = this.getEmail();
            loginInfo.nickname = this.getNickname();
            loginInfo.isSocial = this.isSocialId();
        },
        setLogoutState(){
            loginInfo.isLogin = false;
            loginInfo.email = '';
            loginInfo.nickname = '';
            loginInfo.isSocial = false;
        },
        logout(){
            this.deleteCookie('jwt-token');
            this.setLogoutState();

            axios.post('/api/account/signOut')
            // if(confirm(i18n('index.logout.confirm'))){
            //
            // }
        },
        getParsedJwt(){
            let token = this.getCookie('jwt-token').split('\.');
            const header = JSON.parse(atob(token[0]));
            const claim = JSON.parse(atob(token[1]));

            return {
                header : header,
                claim : claim
            }
        },
        isLogin(){
            if(this.getCookie('jwt-token')){
                this.setLoginState();
            }
            else{
                this.setLogoutState();
            }
            return loginInfo.isLogin;
        },
        getEmail(){
            const jwt = this.getParsedJwt();
            if(jwt){
                return jwt.claim.email;
            }
            return '';
        },
        getNickname(){
            const jwt = this.getParsedJwt();
            if(jwt){
                return jwt.claim.nickname;
            }
            return '';
        },
        isSocialId(){
            const jwt = this.getParsedJwt();
            if(jwt){
                return jwt.claim.isSocial;
            }
            return false;
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
