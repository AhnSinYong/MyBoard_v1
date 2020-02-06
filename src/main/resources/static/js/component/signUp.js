import shareObject from "./shareObject/shareObject.js";

export default Vue.component('sign-up',{
    template:
        `<div class="cover-view">
            <div class="component-sign-up margin-auto-center">
                <div> <input type="button" value="x" @click="inputMethod.closeView(input, coverViewMethod.hideSignUpView)"
                             class="btn btn-outline-dark right"> </div>
                <div>
                    <span> {{i18n('index.signup.guide')}} </span>
                </div>
                <div class="margin-auto-center">
                    <input v-model="input.nickname" class="display-block" type="text" :placeholder="i18n('index.signup.nickname.placeholder')">
                    <input v-model="input.email" class="display-block" type="text" :placeholder="i18n('index.signup.email.placeholder')">
                    <input v-model="input.password" class="display-block" type="password" :placeholder="i18n('index.signup.password.placeholder')">
                    <input v-model="input.passwordCheck" class="display-block" type="password" :placeholder="i18n('index.signup.passwordcheck.placeholder')">
                </div>
                <div>
                    <input class="btn btn-outline-dark" type="button" :value="i18n('index.signup')" @click="signUp()">
                </div>
            </div>
        </div>`,
    components: {

    },
    data(){
        return {
            coverViewState:shareObject.coverView.state,
            coverViewMethod : shareObject.coverView.method,
            inputMethod : shareObject.input.method,
            failFunc : shareObject.failFunc,
            input :{
                nickname : '',
                email : '',
                password : '',
                passwordCheck : ''
            },
            i18n:i18n,
        }
    },
    async created(){

    },
    methods:{
        signUp(){
            const data = {
                nickname : this.input.nickname,
                email : this.input.email,
                password: this.input.password,
                passwordCheck: this.input.passwordCheck
            }
            axios.post('/api/account', data)
                .then(res=>{
                    console.log('/api/account : ', res);
                    alert(i18n('index.signup.complete'));
                    this.coverViewMethod.hideSignUpView();
                    this.inputMethod.resetInput(this.input);
                })
                .catch(err=>{
                    this.failFunc.failFunc(err);
                })
        },

    }

});