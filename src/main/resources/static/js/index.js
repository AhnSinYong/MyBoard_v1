import shareObject from "./component/shareObject.js"

import signUp from"./component/signUp.js"

new Vue({
    el : '#app',
    components:{
        signUp
    },
    data(){
        return {
            coverView: shareObject.coverView
        }
    },
    async created(){

    },

    methods:{
        showSignUpView(){
            this.coverView.showSignUpView();
        }
    }
})