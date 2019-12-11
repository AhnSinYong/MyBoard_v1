import shareObject from "./shareObject/shareObject.js";

export default Vue.component('post',{
    props:['board_id'],
    template:
        `<div class="cover-view">
            <div class="component-post margin-auto-center">
                <div name="post">
                    <div> <input type="button" value="x" @click="inputMethod.closeView(input,coverViewMethod.hidePostView)"> </div>
                    <div><span>{{post.title}}</span></div>
                    <div v-html="post.content.replace(/(?:\\r\\n|\\r|\\n)/g, '<br/>')"></div>
                    <div>
                        <span>작성자 : 
                            <span v-if="post.account!=null">{{post.account.nickname}}</span>
                            <span v-else> 알수없음 </span>
                        </span>
                        <span>조회수 : <span> {{post.view}} </span></span>
                        <span><input :class="{focusLike:post.isLiked}"type="button" value="like" @click="likePost(board_id)">{{post.like}}</span>
                    </div>
                    <div>
                        <span>작성일 : <span>{{new Date(post.regDate).format('yy-MM-dd a/p hh:mm:ss')}}</span> </span>
                    </div>
                    <div>
                        <span>수정일 : <span>{{new Date(post.upDate).format('yy-MM-dd a/p hh:mm:ss')}}</span> </span>
                    </div>
                    <div>
                        <div v-for="file in fileList">
                            <span>{{file.originName}}</span>
                            <input type="button" value="download" @click="download(file.originName)">
                        </div>
                    </div>
                    <div>
                        <input v-if="loginInfo.isLogin" type="button" value="modify">
                        <input v-if="loginInfo.isLogin" type="button" value="delete">
                    </div>
                </div>
                <div name="comment">
                    <div>
                        <textarea v-model="input.inputComment"></textarea>
                    </div>
                    <div v-for="comment in commentList">
                        <div>
                            <span><input  type="button" value="like"> {{comment.like}}</span>
                            <span>
                                작성자 : 
                                <span v-if="comment.account!=null">{{comment.account.nickname}}</span>
                                <span v-else>알수없음</span>
                            </span>
                        </div>
                        <div> {{comment.content}}</div>
                        <div>
                            <div>
                                <span>작성일 : <span>{{new Date(comment.regDate).format('yy-MM-dd a/p hh:mm:ss')}}</span> </span> 
                            </div>
                            <div>
                                <span>수정일 : <span>{{new Date(comment.upDate).format('yy-MM-dd a/p hh:mm:ss')}}</span> </span> 
                            </div>  
                        </div>
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
                inputComment:'',
            },

            post:{
                content:'',
                like:'',
                isLiked:false,
            },
            fileList:[],
            commentList:[],

        }
    },
    async created(){
    },
    watch:{
        board_id(boardId, oldVal){
            this.getPost(boardId);
        }
    },
    methods:{
        getPost(boardId){
            axios.get('/api/board/post/'+boardId)
                .then(this.successGetPost)
                .catch(this.fail)
        },
        successGetPost(res){
            const data = res.data;

            this.post = data.post;
            this.post.isLiked = data.isLikedPost;
            this.commentList = data.commentList;
            this.fileList = data.fileList;

            this.inputMethod.resetInput(this.input);
            shareObject.refreshManager.refresh();
        },
        likePost(boardId){
            if(!this.loginMethod.isLogin()){
                alert("please, sign in");
                return;
            }

            axios.post('/api/board/like',{boardId:boardId})
                .then(this.successLikePost)
                .catch(this.fail)

        },
        successLikePost(res){
            this.post.like = res.data.like;
            this.post.isLiked = !this.post.isLiked;
            shareObject.refreshManager.refresh();
        },
        fail(err){
            console.log(err);
            alert(err.data);
        },

        download(fileName){

        }
    }

});
