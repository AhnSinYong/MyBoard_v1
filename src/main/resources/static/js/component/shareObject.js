const state = {
    signUp : false,
}

export default {
    coverView:{
        state : state,
        method : {
            showSignUpView: function() {
                state.signUp = true;
            },
            hideSignUpView(){
                state.signUp = false;
            }
        }
    }
}