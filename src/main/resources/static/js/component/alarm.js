import shareObject from "./shareObject/shareObject.js";

export default Vue.component('alarm',{
    props:['is_login'],
    template:
        `<div>
            <div class="component-alarm margin-auto-center">
                <div v-if="alarmListVisibleState">
                    <div> <input type="button" value="x" @click="hideAlarmList()"> </div>
                    <div> 알람 목록들이 나옴</div>    
                </div>
                <div>
                    <img src="/static/img/alarm.png" @click="switchAlarmList()">
                </div>                
            </div>            
        </div>`,
    components: {

    },
    data(){
        return {
            coverViewMethod : shareObject.coverView.method,
            inputMethod : shareObject.input.method,
            loginMethod : shareObject.login.method,
            loginInfo : shareObject.login.info,
            socket : shareObject.socket,

            alarmList:[],

            alarmListVisibleState:false,

        }
    },
    async created(){

    },
    watch:{
        is_login(isLogin, old){
            if(isLogin){
                this.socket.create('/ws/alarm');

                this.socket.onmessage((event)=>{

                })
                this.socket.onclose((event)=>{

                })
                this.socket.onerror((event)=>{

                })

                this.getAlarmList()
                return;
            }
            this.socket.close();
        }
    },
    methods:{
        getAlarmList(){

        },
        switchAlarmList(){
            this.alarmListVisibleState = !this.alarmListVisibleState;
        },
        hideAlarmList(){
            this.alarmListVisibleState = false;
        }
    }

});