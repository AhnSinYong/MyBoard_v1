import shareObject from "./shareObject.js";

export default Vue.component('sign-up',{
    template:
        `<div class="cover-view">
            <div class="component-sign-up margin-auto-center">
                <div>
                    <span> Please fill out these items </span>
                </div>
                <div class="margin-auto-center">
                    <input v-model="nickname" class="display-block" type="text" placeholder="nickname">
                    <input v-model="email" class="display-block" type="text" placeholder="email">
                    <input v-model="password" class="display-block" type="password" placeholder="password">
                    <input v-model="passwordCheck" class="display-block" type="password" placeholder="password-check">
                </div>
                <div>
                    <input type="button" value="sign up" @click="signUp()">
                </div>
            </div>
        </div>`,
    components: {

    },
    data(){
        return {
            coverView:shareObject.coverView,

            nickname : '',
            email : '',
            password : '',
            passwordCheck : ''
        }
    },
    async created(){

    },
    methods:{
        signUp(){
            const data = {
                nickname : this.nickname,
                email : this.email,
                password: this.password,
                passwordCheck: this.passwordCheck
            }
            axios.post('/api/account', data)
                .then(res=>{
                    console.log('/api/account : ', res);
                    alert("회원가입이 완료되었습니다. \n 이메일 인증을 진행해주세요!");
                    this.coverView.hideSignUpView();
                })
                .catch(err=>{
                    alert(err.data);
                    console.log(err);
                })
        }

    }

});