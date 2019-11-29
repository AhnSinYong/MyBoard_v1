import shareObject from "./shareObject/shareObject.js";

export default Vue.component('sign-in',{
    template:
        `<div class="cover-view">
            <div class="component-sign-in margin-auto-center">
                <div> <input type="button" value="x" @click="inputMethod.closeView(input,coverViewMethod.hideSignInView)"> </div>
                <div>
                    <span> Sign In </span>
                </div>
                <div class="margin-auto-center">
                    <div class="inline-block">
                        <input v-model="input.email" type="text" placeholder="email" @keyup.enter="signIn(input.email, input.password)"></br>
                        <input v-model="input.password" type="password" placeholder="password" @keyup.enter="signIn(input.email, input.password)">    
                    </div>
                </div>
                <div>
                    <input type="button" value="sign in" @click="signIn(input.email, input.password)">
                    <input type="button" value="sign up" @click="coverViewMethod.showSignUpView()">
                </div>
            </div>
        </div>`,
    components: {

    },
    data(){
        return {
            coverViewMethod : shareObject.coverView.method,
            inputMethod : shareObject.input.method,
            loginMethod : shareObject.login.method,
            input:{
                email : '',
                password : '',
            }

        }
    },
    async created(){

    },
    methods:{
        signIn(email, password){
            const data ={
                email : email,
                password : password
            }
            axios.post('/api/account/signIn',data)
                .then(this.successSignIn)
                .catch(this.failSignIn)
        },
        successSignIn(res){
            console.log(res);
            console.log(this.loginMethod.getCookie('jwt-token'));
            this.loginMethod.setLoginState();
            this.coverViewMethod.resetState();
            this.inputMethod.resetInput(this.input);
        },
        failSignIn(err){
            alert(err.data.message);
            this.input.password='';
        }

    }

});