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
                        <span>nickname : 
                            <span v-if="post.account!=null">{{post.account.nickname}}</span>
                            <span v-else> unknown </span>
                        </span>
                        <span>view : <span> {{post.view}} </span></span>
                        <span><input :class="{focusLike:post.isLiked}"type="button" value="like" @click="likePost(board_id)">{{post.like}}</span>
                    </div>
                    <div>
                        <span>regDate : <span>{{new Date(post.regDate).format('yy-MM-dd a/p hh:mm:ss')}}</span> </span>
                    </div>
                    <div>
                        <span>upDate : <span>{{new Date(post.upDate).format('yy-MM-dd a/p hh:mm:ss')}}</span> </span>
                    </div>
                    <div>
                        <div v-for="file in fileList">
                            <span>{{file.originName}}</span>
                            <input type="button" value="download" @click="download(file.fileId)">
                            <span>down :<span> {{file.down}} </span> </span>
                        </div>
                    </div>
                    <div v-if="post.account!=null">
                        <div v-if="post.account.email==loginInfo.email">
                            <input type="button" value="modify" @click="showUpdatePostView()">
                            <input type="button" value="delete" @click="deletePost(post.boardId)">
                        </div>                        
                    </div>
                </div>
                <div name="comment">
                    <div>
                        <div>
                            <textarea v-model="input.inputComment"></textarea>
                        </div>
                        <div>
                            <input type="button" value="write" @click="writeComment(post.boardId,input.inputComment)">
                        </div>                    
                    </div>
                    
                    <div v-for="(comment,index) in commentList">
                        <div v-for="index in comment.delParentCnt">
                            <div :class="{childComment: comment.hasDelTypeParent?(index!=1):true}">
                                <div>
                                    <span>삭제된 댓글 입니다.</span>
                                </div>
                                <div>
                                    <input v-if="loginInfo.isLogin&&comment.hasDelTypeParent?(index!=1):true" type="button" value="reply">                            
                                </div>                             
                            </div>                                                                                                               
                        </div>
                        <div :class="{childComment:comment.type=='CHILD'}">
                            <div v-if="index!=invisibleCommentIndex">
                                <div>                            
                                    <span>
                                        nickname : 
                                        <span v-if="comment.account!=null">{{comment.account.nickname}}</span>
                                        <span v-else>unknown</span>
                                    </span>
                                    <span><input  type="button" value="like"
                                                  :class="{focusLike:comment.isLiked}" 
                                                  @click="likeComment(index,comment.commentId)"> {{comment.like}}</span>
                                </div>
                                <div> {{comment.content}}</div>
                                <div>
                                    <div>
                                        <span>regDate : <span>{{new Date(comment.regDate).format('yy-MM-dd a/p hh:mm:ss')}}</span> </span> 
                                    </div>
                                    <div>
                                        <span>upDate : <span>{{new Date(comment.upDate).format('yy-MM-dd a/p hh:mm:ss')}}</span> </span> 
                                    </div>  
                                </div>
                                <div v-if="comment.account!=null">
                                    <div v-if="comment.account.email==loginInfo.email">
                                        <input type="button" value="modify" @click="showModifyCommentView(index)">
                                        <input type="button" value="delete" @click="deleteComment(comment.board.boardId, comment.commentId)">
                                    </div>                        
                                </div>                            
                                <div>
                                    <input v-if="loginInfo.isLogin" type="button" value="reply" @click="writeReplyComment(comment.group)">
                                </div>
                            </div>
                            <div v-else>
                                <div>                            
                                    <span>
                                        nickname : 
                                        <span v-if="comment.account!=null">{{input.modifyComment.account.nickname}}</span>
                                        <span v-else>unknown</span>
                                    </span>
                                    <span><input  type="button" value="like" @click="likeComment(index, comment.commentId)"> {{input.modifyComment.like}}</span>
                                </div>
                                <div>
                                    <textarea v-model="input.modifyComment.content"></textarea>
                                </div>
                                <div>
                                    <div>
                                        <span>regDate : <span>{{new Date(input.modifyComment.regDate).format('yy-MM-dd a/p hh:mm:ss')}}</span> </span> 
                                    </div>
                                    <div>
                                        <span>upDate : <span>{{new Date(input.modifyComment.upDate).format('yy-MM-dd a/p hh:mm:ss')}}</span> </span> 
                                    </div>  
                                </div>                                
                                <div>
                                    <input type="button" value="complete" @click="modifyComment(input.modifyComment)">
                                    <input type="button" value="cancle" @click="cancleModifyComment()">
                                </div>                                
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
            deliveryData : shareObject.deliveryData,
            input:{
                inputComment:'',
                modifyComment: {
                    content:''
                }
            },
            visibleModifyCommentFormIndex:-1,
            invisibleCommentIndex:-1,
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
            this.getCommentList(boardId);
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

        download(fileId){
            window.location.href = '/api/board/file/'+fileId;
        },

        deletePost(boardId){
            if(confirm("do you really want delete this post?")){
                axios.delete('/api/board/'+boardId)
                    .then(this.successDeltePost)
                    .catch(this.fail)
            }
        },
        successDeltePost(res){
            this.coverViewMethod.hidePostView();
            shareObject.refreshManager.refresh();
        },
        showUpdatePostView(){
            this.deliveryData('update_post','board',{'post':this.post, 'fileList':this.fileList})
            this.coverViewMethod.showUpdatePostView();
        },
        changeBoardId(boardId){
            this.boardId= boardId;

        },

        getCommentList(boardId){
            axios.get('/api/comment/'+boardId)
                .then(this.successGetCommentList)
                .catch(this.fail)

        },
        successGetCommentList(res){
            const data = res.data;
            this.commentList = data.commentList;
            const isLikedList = data.isLikedList;

            for(let i=0; i<isLikedList.length; i++){
                this.commentList[i].isLiked = isLikedList[i];
            }

        },
        showModifyCommentView(index){
            this.invisibleCommentIndex = index;
            this.input.modifyComment = this.commentList[index];
        },
        cancleModifyComment(){
            this.invisibleCommentIndex = -1;
        },
        deleteComment(boardId, commentId){
            axios.delete('/api/comment/'+commentId)
                .then(this.successDeleteComment)
                .catch(this.fail)

        },
        successDeleteComment(res){
            const boardId = res.data.boardId;
            this.getCommentList(boardId);
        },
        modifyComment(comment){
            const data={
                content : this.input.modifyComment.content
            }
            axios.put('/api/comment/'+comment.commentId,data)
                .then(this.successModifyComment)
                .catch(this.fail)
        },
        successModifyComment(res){
            const boardId = res.data.boardId;
            this.getCommentList(boardId);
            this.cancleModifyComment();
        },
        writeComment(boardId, content){
            const data = {
                content : content
            }

            axios.post('/api/comment/'+ boardId,data)
                .then(this.successwriteComment)
                .catch(this.fail)
        },
        successwriteComment(res){
            const boardId = res.data.boardId;
            this.getCommentList(boardId);
            this.input.inputComment='';
        },
        writeReplyComment(group){

        },
        likeComment(index,commentId){
            axios.put('/api/comment/like/'+commentId)
                .then((res)=>{
                    const data = res.data;
                    this.commentList[index].like = data.like;
                    this.commentList[index].isLiked = !this.commentList[index].isLiked;
                })
                .catch(this.fail)
        },
    }

});
