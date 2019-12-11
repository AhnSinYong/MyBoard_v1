import shareObject from "./component/shareObject/shareObject.js"

import signIn from "./component/signIn.js"
import signUp from "./component/signUp.js"
import board from "./component/board.js"
import writePost from "./component/writePost.js"
import post from "./component/post.js"

new Vue({
    el : '#app',
    components:{
        signIn,
        signUp,
        board,
        writePost,
        post,
    },
    data(){
        return {
            coverViewState:shareObject.coverView.state,
            coverViewMethod : shareObject.coverView.method,

            loginInfo : shareObject.login.info,
            loginMethod: shareObject.login.method,

            delivery:{
                post:{
                    boardId:''
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
        }
    }
})
