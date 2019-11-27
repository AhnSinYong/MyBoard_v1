import shareObject from "./component/shareObject.js"

import signUp from"./component/signUp.js"

new Vue({
    el : '#app',
    components:{
        signUp
    },
    data(){
        return {
            coverViewState:shareObject.coverView.state,
            coverViewMethod : shareObject.coverView.method,
        }
    },
    async created(){

    },

    methods:{
    }
})