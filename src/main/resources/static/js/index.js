import shareObject from "./component/shareObject/shareObject.js"

import signIn from "./component/signIn.js"
import signUp from "./component/signUp.js"
import board from "./component/board.js"
import writePost from "./component/writePost.js";

new Vue({
    el : '#app',
    components:{
        signIn,
        signUp,
        board,
        writePost
    },
    data(){
        return {
            coverViewState:shareObject.coverView.state,
            coverViewMethod : shareObject.coverView.method,

            loginInfo : shareObject.login.info,
            loginMethod: shareObject.login.method,
        }
    },
    async created(){
        this.loginMethod.checkLogin();
    },

    methods:{
    }
})
