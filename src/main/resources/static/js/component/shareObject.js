export default {
    coverView:{
        state:{
            signUp : false,
        },
        showSignUpView(){
            this.state.signUp = true;
        },
        hideSignUpView(){
            this.state.signUp = false;
        }

    }
}