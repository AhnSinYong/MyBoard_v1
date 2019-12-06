
export default {
    method:{
        resetInput(obj){
            for(let prop in obj){
                if(Array.isArray(obj[prop])){
                    obj[prop]=[];
                }
                else{
                    obj[prop] = '';
                }
            }
        },
        closeView(obj, callback){
            this.resetInput(obj);
            callback();
        }
    }
}