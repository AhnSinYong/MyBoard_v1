import shareObject from "./shareObject/shareObject.js";

export default Vue.component('board',{
    template:
        `<div class="board">
            <div class="board-table">
                <table class="table table-hover table-striped">
                    <thead>
                        <tr>
                            <td name="number">{{i18n('index.board.number')}}</td>
                            <td name="title">{{i18n('index.board.title')}}</td>
                            <td name="like">{{i18n('index.board.like')}}</td>
                            <td name="view">{{i18n('index.board.view')}}</td>
                            <td name="regdate">{{i18n('index.board.regdate')}}</td>
                            <td name="update">{{i18n('index.board.update')}}</td>
                            <td name="nickname">{{i18n('index.board.nickname')}}</td>                        
                        </tr>
                    </thead>
                    <tbody >
                        <tr v-for="board in pagination.list" 
                            @click="coverViewMethod.showPostView();
                                    deliveryData('post','boardId',board.boardId);">
                            <td>{{board.boardId}}</td>
                            <td>{{board.title}}</td>
                            <td>{{board.like}}</td>
                            <td>{{board.view}}</td>
                            <td>{{new Date(board.regDate).format('yy-MM-dd a/p hh:mm:ss')}}</td>                            
                            <td>{{new Date(board.upDate).format('yy-MM-dd a/p hh:mm:ss')}}</td>
                            <td v-if="board.account">{{board.account.nickname}}</td>
                            <td v-else>{{i18n('index.unknown')}}</td>
                        </tr>                    
                    </tbody>
                </table>
            </div>
            <div>
                <div>
                    <input v-if="pagination.prevPage!=-1"type="button" :value="i18n('index.board.prev')"
                           class="btn btn-outline-dark" 
                           @click="getBoardList(pagination.prevPage)">
                    <input v-for="page in (pagination.endPage-pagination.startPage+1)" 
                           type="button" 
                           :class="{focusPage:pagination.page==(page+pagination.startPage-1)}"
                           class="btn btn-outline-dark page-item"                           
                           :value="page+pagination.startPage-1" 
                           @click="getBoardList(page+pagination.startPage-1)">
                    <input v-if="pagination.nextPage!=-1"type="button" :value="i18n('index.board.next')"
                           class="btn btn-outline-dark" 
                           @click="getBoardList(pagination.nextPage)">
                </div>                
            </div>
            <div>
                <div>
                    <input v-if="loginInfo.isLogin" type="button" value="write"
                           class="btn btn-outline-dark" 
                           @click="coverViewMethod.showWritePostView()">
                </div>
            </div>
        </div>`,
    components: {

    },
    data(){
        return {
            deliveryData : shareObject.deliveryData,
            coverViewMethod : shareObject.coverView.method,
            inputMethod : shareObject.input.method,
            loginInfo : shareObject.login.info,
            loginMethod : shareObject.login.method,
            failFunc : shareObject.failFunc,
            input:{
            },
            pagination:{
                list:[],
                page:'',
                startPage:'',
                endPage:'',
                prevPage:'',
                nextPage:''
            },
            i18n: i18n,
        }
    },
    created(){
        shareObject.refreshManager.register(this.refreshBoard);
        this.getBoardList(1);

    },
    methods:{
        refreshBoard(){
            this.getBoardList(this.pagination.page);
        },
        getBoardList(page){
            axios.get('/api/board/'+page)
                .then(this.success)
                .catch(this.fail)
        },
        success(res){
            console.log(res);
            this.pagination = res.data;
        },
        fail(err){
            this.failFunc.failFunc(err);
        },
    }
});