import shareObject from "./shareObject/shareObject.js";

export default Vue.component('update-post',{
    props:['board'],
    template:
        `<div class="cover-view">
            <div class="component-update-post margin-auto-center">
                <div>
                    <div> <input type="button" value="x" @click="inputMethod.closeView(input,coverViewMethod.hideUpdatePostView)"> </div>
                    <div><input v-model="post.title"type="text"></div>
                    <div><textarea v-model="post.content"></textarea></div>
                    <div>
                        <div v-for="(file,index) in post.fileList">
                            <span>{{file.originName}}</span>
                            <input type="button" value="x" @click="deleteFile(index)">
                        </div>                        
                        <div v-for="(file,index) in input.fileList">
                            <span> name : {{file.name}}, size : {{file.size}}</span>
                            <input type="button" value="x" @click="removeFile(index)">
                        </div>
                        <div>
                            <input type="file" ref="fileInput" @change="addFile()" ><span>upload file</span>
                        </div>
                    </div>
                    <div>
                        <input type="button" value="complete" @click="updatePost(post, input.fileList)">
                        <input type="button" value="cancel" @click="returnPost()">
                    </div>
                </div>                
            </div>
        </div>`,
    components: {

    },
    data(){
        return {
            coverViewMethod : shareObject.coverView.method,
            inputMethod : shareObject.input.method,
            loginInfo : shareObject.login.info,
            loginMethod : shareObject.login.method,

            post:{
                title:'',
                content:'',
            },

            input:{
                fileList:[]
            },
        }
    },
    async created(){
    },
    watch:{
        board(board, oldVal){
            this.post = board.post;
            this.post.fileList = board.fileList;
        }
    },
    methods:{
        updatePost(post,fileList){
            const formData = new FormData();
            formData.append('title',post.title);
            formData.append('content', post.content);

            for(let i=0; i<post.fileList.length; i++){
                const file = post.fileList[i];
                const email = file.account.email;
                const boardId = file.board.boardId;
                const fileId = file.fileId;
                formData.append('existFileInfoList['+i+'].email', email);
                formData.append('existFileInfoList['+i+'].boardId', boardId);
                formData.append('existFileInfoList['+i+'].fileId', fileId);
            }

            for(let i=0; i<fileList.length; i++){
                formData.append('inputFileList['+i+']', fileList[i]);
            }

            axios.put('/api/board/'+post.boardId,formData,{
                headers : {
                    'Content-Type': 'multipart/form-data'
                }
            })
                .then(this.successUpdatePost)
                .catch(this.fail)
        },
        successUpdatePost(res){
            const data = res.data;

            this.coverViewMethod.showPostView();
            this.inputMethod.resetInput(this.input);
            shareObject.refreshManager.refresh();
        },
        fail(err){
            console.log(err);
            alert(err.data);
        },
        deleteFile(index){
            this.post.fileList.splice(index,1);
            this.$forceUpdate();
        },
        removeFile(index){
            this.input.fileList.splice(index,1);
        },
        addFile(){
            this.input.fileList.push(this.$refs.fileInput.files[0]);
            this.$refs.fileInput.value='';
        },
        returnPost(){
            this.coverViewMethod.showPostView();
        }
    }

});