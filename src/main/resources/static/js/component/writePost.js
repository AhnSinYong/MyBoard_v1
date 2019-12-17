import shareObject from "./shareObject/shareObject.js";

export default Vue.component('write-Post',{
    template:
        `<div class="cover-view">
            <div class="component-write-post margin-auto-center">
                <div> <input type="button" value="x" 
                             class="btn btn-outline-dark right"
                             @click="inputMethod.closeView(input,coverViewMethod.hideWritePostView)"> </div>
                <div ><input class="title" v-model="input.title" type="text" placeholder="title"></div>
                <div><textarea class="content" v-model="input.content" placeholder="content"></textarea></div>
                <div>
                    <div v-for="(file,index) in input.fileList">
                        <span class="file-name"> name : {{file.name}}, size : {{file.size}}</span>
                        <input class="btn btn-outline-dark" type="button" value="x" @click="removeFile(index)">
                    </div>
                    <div><input type="file" ref="fileInput"@change="addFile()"></div>
                </div>
                <div>
                    <input type="button" value="post" @click="writePost(input.title, input.content, input.fileList)"
                           class="btn btn-outline-dark right">
                </div>
            </div>
        </div>`,
    components: {

    },
    data(){
        return {
            coverViewMethod : shareObject.coverView.method,
            inputMethod : shareObject.input.method,
            input:{
                title : '',
                content : '',
                fileList : []
            }

        }
    },
    async created(){

    },
    methods:{
        writePost(title, content, fileList){
            const formData = new FormData();
            formData.append('title',title);
            formData.append('content', content);
            for(let i=0; i<fileList.length; i++){
                formData.append('fileList['+i+']', fileList[i]);
            }

            axios.post('/api/board',formData,{
                headers : {
                    'Content-Type': 'multipart/form-data'
                }
            })
                .then(this.successSignIn)
                .catch(this.failSignIn)
        },
        successSignIn(res){
            console.log(res);
            this.coverViewMethod.resetState();
            this.inputMethod.resetInput(this.input);
            shareObject.refreshManager.refresh();
        },
        failSignIn(err){
            console.log(err);
            alert(err.data);
        },
        addFile(){
            this.input.fileList.push(this.$refs.fileInput.files[0]);
            this.$refs.fileInput.value='';
        },
        removeFile(index){
            this.input.fileList.splice(index,1);
        }

    }

});