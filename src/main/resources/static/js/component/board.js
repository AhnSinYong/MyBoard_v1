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
                        <tr v-for="board in boardList">
                            <td>{{board.boardId}}</td>
                            <td>{{board.title}}</td>
                            <td>{{board.like}}</td>
                            <td>{{board.view}}</td>
                            <td>{{board.regDate}}</td>
                            <td>{{board.upDate}}</td>
                            <td>{{board.account.nickname}}</td>
                        </tr>                    
                    </tbody>
                </table>
            </div>
            <div>
                
            </div>
            <div>
                <div>
                    <input type="text">
                    <input type="button" value="search">
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
            },
            boardList:[]
        }
    },
    async created(){
        this.getBoardList();
    },
    methods:{
        getBoardList(){
            axios.get('/api/board')
                .then(this.successSignIn)
                .catch(this.failSignIn)
        },
        successSignIn(res){
            console.log(res);
            this.boardList = res.data;
            // this.loginMethod.setLoginState();
            // this.coverViewMethod.resetState();
            // this.inputMethod.resetInput(this.input);
        },
        failSignIn(err){
            alert(err.data.message);
            // this.input.password='';
        }

    }

});