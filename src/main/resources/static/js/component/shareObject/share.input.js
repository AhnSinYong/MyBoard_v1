
export default {
    method:{
        resetInput(obj){
            for(let prop in obj){
                obj[prop] = '';
            }
        },
        closeView(obj, callback){
            this.resetInput(obj);
            callback();
        }
    }
}