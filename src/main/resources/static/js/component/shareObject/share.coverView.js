const coverViewState = {
    signUp : false,
    signIn : false,
    writePost : false,
}

function resetState(){
    for(let prop in coverViewState){
        coverViewState[prop] = false;
    }
}

export default {
    state : coverViewState,
    method : {
        showSignUpView(){
            resetState();
            coverViewState.signUp = true;
        },
        hideSignUpView(){
            resetState();
            coverViewState.signUp = false;
        },
        showSignInView(){
            resetState();
            coverViewState.signIn = true;
        },
        hideSignInView(){
            resetState();
            coverViewState.signIn = false;
        },
        showWritePostView(){
            resetState();
            coverViewState.writePost = true;
        },
        hideWritePostView(){
            resetState();
            coverViewState.writePost = false;
        },
        resetState : resetState
    }
}