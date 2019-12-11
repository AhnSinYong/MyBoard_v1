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
                        <div v-for="(file,index) in fileList">
                            <span>{{file.originName}}</span>
                            <input type="button" value="delete" @click="deleteFile(index)">
                        </div>
                        <div>
                            <input type="file"><span>upload file</span>
                        </div>
                    </div>
                    <div>
                        <input type="button" value="complete">
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
            input:{

            },

            post:{
                title:'',
                content:'',
            },
            fileList:[],

        }
    },
    async created(){
    },
    watch:{
        board(board, oldVal){
            this.post = board.post;
            this.fileList = board.fileList;
        }
    },
    methods:{
        updatePost(boardId){
            // axios.get('/api/board/'+boardId)
            //     .then(this.successUpdatePost)
            //     .catch(this.fail)
        },
        successUpdatePost(res){
            const data = res.data;

            this.post = data.post;
            this.post.isLiked = data.isLikedPost;
            this.commentList = data.commentList;
            this.fileList = data.fileList;

            this.inputMethod.resetInput(this.input);
            shareObject.refreshManager.refresh();
        },
        fail(err){
            console.log(err);
            alert(err.data);
        },
        deleteFile(index){
            this.fileList.splice(index,1);
        },
        returnPost(){
            this.coverViewMethod.showPostView();
        }
    }

});
