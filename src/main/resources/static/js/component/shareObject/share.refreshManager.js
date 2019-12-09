export default {
    methods:[],
    refresh(){
        for(let i=0; i<this.methods.length; i++){
            const method = this.methods[i].method;
            const params = this.methods[i].params;
            method.apply(method,params);

        }
    },
    register(func){
        this.methods.push({
            method : func,
            params : Array.prototype.slice.call(arguments, 1)
        });
    }

}