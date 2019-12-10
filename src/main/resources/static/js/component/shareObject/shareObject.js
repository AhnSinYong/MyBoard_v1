import login from "./share.login.js"
import coverView from "./share.coverView.js"
import input from "./share.input.js"
import refreshManager from "./share.refreshManager.js"
export default {
    login: login,
    coverView: coverView,
    input: input,
    refreshManager : refreshManager,
    deliveryData(eventName,name,data){
        this.$emit(eventName,{name:name,content:data});
    }
}