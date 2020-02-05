import shareObject from "./component/shareObject/shareObject.js"

import signIn from "./component/signIn.js"
import signUp from "./component/signUp.js"
import board from "./component/board.js"
import writePost from "./component/writePost.js"
import post from "./component/post.js"
import updatePost from "./component/updatePost.js"
import alarm from "./component/alarm.js";

new Vue({
    el : '#app',
    components:{
        signIn,
        signUp,
        board,
        writePost,
        post,
        updatePost,
        alarm,

    },
    data(){
        return {
            coverViewState:shareObject.coverView.state,
            coverViewMethod : shareObject.coverView.method,

            loginInfo : shareObject.login.info,
            loginMethod: shareObject.login.method,
            lang : this.getParam('lang')||'ko',
            delivery:{
                post:{
                    boardId:''
                },
                updatePost:{
                    board:{}
                }
            },
        }
    },
    async created(){
        this.loginMethod.checkLogin();
    },

    methods:{
        post(data){
            this.delivery.post[data.name] = data.content;
        },
        updatePost(data){
            this.delivery.updatePost[data.name] = data.content;
        },
        setLanguage(lang){
            location.href = '?lang='+lang;
        },
        getParam(sname) {
            let params = location.search.substr(location.search.indexOf("?") + 1);
            let sval;
            params = params.split("&");
            for (let i = 0; i < params.length; i++) {
                let temp = params[i].split("=");
                if ([temp[0]] == sname) { sval = temp[1]; }}
            return sval;
        }
    }
})
