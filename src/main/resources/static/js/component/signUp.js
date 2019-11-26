export default Vue.component('sign-up',{
    template:
        `<div class="component-sign-up margin-auto-center">
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
                <input type="button" value="sign up">
            </div>
        </div>`,
    components: {

    },
    data(){
        return {
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
            axios.post('/api/member/signUp', data)
                .then(res=>{
                    alert("응답 도착");
                    console.log(res);
                })
                .catch(err=>{
                    alert("오류 발생");
                    console.log(err);
                })
        }

    }

});