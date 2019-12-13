import shareObject from "./component/shareObject/shareObject.js"

import signIn from "./component/signIn.js"
import signUp from "./component/signUp.js"
import board from "./component/board.js"
import writePost from "./component/writePost.js"
import post from "./component/post.js"
import updatePost from "./component/updatePost.js"

new Vue({
    el : '#app',
    components:{
        signIn,
        signUp,
        board,
        writePost,
        post,
        updatePost

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
        }
    }
})
