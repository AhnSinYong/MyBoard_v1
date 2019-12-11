import shareObject from "./shareObject/shareObject.js";

export default Vue.component('board',{
    template:
        `<div class="board">
            <div>
                <table>
                    <thead>
                        <tr>
                            <td>NUMBER</td>
                            <td>TITLE</td>
                            <td>LIKE</td>
                            <td>VIEW</td>
                            <td>REGDATE</td>
                            <td>UPDATE</td>
                            <td>NICKNAME</td>                        
                        </tr>
                    </thead>
                    <tbody>
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
                            <td v-else>알수없음</td>
                        </tr>                    
                    </tbody>
                </table>
            </div>
            <div>
                <div>
                    <input v-if="pagination.prevPage!=-1"type="button" value="prev" @click="getBoardList(pagination.prevPage)">
                    <input v-for="page in (pagination.endPage-pagination.startPage+1)" 
                           type="button" 
                           :class="{focusPage:pagination.page==(page+pagination.startPage-1)}"                           
                           :value="page+pagination.startPage-1" 
                           @click="getBoardList(page+pagination.startPage-1)">
                    <input v-if="pagination.nextPage!=-1"type="button" value="next" @click="getBoardList(pagination.nextPage)">
                </div>                
            </div>
            <div>
                <div>
                    <input type="text">
                    <input type="button" value="search">
                </div>
            </div>
            <div>
                <div>
                    <input v-if="loginInfo.isLogin" type="button" value="write" @click="coverViewMethod.showWritePostView()">
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
            input:{
            },
            pagination:{
                list:[],
                page:'',
                startPage:'',
                endPage:'',
                prevPage:'',
                nextPage:''
            }
        }
    },
    created(){
        shareObject.refreshManager.register(this.getBoardList,1);
        this.getBoardList(1);
    },
    methods:{
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
            alert(err.data.message);
        },
    }
});