import shareObject from "./shareObject/shareObject.js";

export default Vue.component('post',{
    props:['board_id'],
    template:
        `<div class="cover-view">
            <div class="component-post margin-auto-center">
                <div name="post">
                    <div> <input type="button" value="x" @click="inputMethod.closeView(input,coverViewMethod.hidePostView)"> </div>
                    <div><span>제목</span></div>
                    <div><span>내용</span></div>
                    <div>
                        <div>첨부파일</div>
                    </div>
                </div>
                <div name="comment">
                    <div>
                        <textarea v-model="input.inputComment"></textarea>
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
            input:{
                inputComment:'',
            },

        }
    },
    async created(){
    },
    watch:{
        board_id:function(boardId, oldVal){
            this.getPost(boardId);
        }
    },
    methods:{
        getPost(boardId){
            axios.get('/api/board/post/'+boardId)
                .then(this.success)
                .catch(this.fail)
        },
        success(res){
            console.log(res);
            // this.coverViewMethod.resetState();
            this.inputMethod.resetInput(this.input);
            shareObject.refreshManager.refresh();
        },
        fail(err){
            console.log(err);
            alert(err.data);
        },

    }

});