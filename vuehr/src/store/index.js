import Vue from 'vue'
import Vuex from 'vuex'
import {getRequest} from "../util/api";
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import {Notification} from 'element-ui';


Vue.use(Vuex);

const now = new Date();

const store = new Vuex.Store({
    state: {
        routes: [],
        sessions: [],
        hrs: [],
        currentHr: JSON.parse(sessionStorage.getItem("user")),
        currentSession: null,
        filterKey: '',
        stomp: null,  //定义websocket连接变量
        isBadge:[]
    },
    mutations: {
        initRoutes(state, data) {
            state.routes = data
        },
        changeCurrentSession(state, currentSession) {
            state.currentSession = currentSession;
            Vue.set(state.isBadge,state.currentHr.username+'#'+currentSession.username,false)
        },
        addMessage(state, msg) {
            let mss = state.sessions[state.currentHr.username + "#" + msg.to];
            if (!mss) {
                // state.sessions[state.currentHr.username + "#" + msg.to] = [];
                Vue.set(state.sessions, state.currentHr.username + "#" + msg.to, [])
            }
            state.sessions[state.currentHr.username + "#" + msg.to].push({
                content: msg.content,
                date: new Date(),
                self: !msg.noSelf
            })


        },
        INIT_DATA(state) {
            let data = window.localStorage.getItem('vue-chat-session');
            //浏览器本地的历史聊天记录可以在这里完成
            if (data) {
                state.sessions = JSON.parse(data);
            }
        },
        INIT_HRS(state, data) {
            state.hrs = data
        },
         INIT_CURRENTHR(state, hr) {
             state.currentHr = hr
         }
    },
    actions: {
        initData(context) {
            context.commit('INIT_DATA')
            getRequest("/hr/chat").then(resp => {
                if (resp) {
                    context.commit('INIT_HRS', resp.data)
                }
            })
        },
        connect(content) {
            var socket = new SockJS('/ws/ep')
            content.state.stomp = Stomp.over(socket) //创建连接对象
            content.state.stomp.connect({}, function () {
                content.state.stomp.subscribe("/user/queue/chat", msg => {
                    //接收消息
                    let reciveMsg = JSON.parse(msg.body);
                    console.log("接受到的消息:" + reciveMsg)
                    console.log("reciveMsg" + reciveMsg)
                    //如果接收到不是当前正在聊天用户发来的消息时  弹框提示
                    if (!content.state.currentSession || reciveMsg.from != content.state.currentSession.username) {
                        Notification.info({
                            title: '[' + reciveMsg.nickName + ']发来一条消息',

                            message: '消息内容为' + reciveMsg.content.length > 10 ? reciveMsg.content.substr(0,10) : reciveMsg.content,
                            position: 'bottom-right'
                        });
                        Vue.set(content.state.isBadge,content.state.currentHr.username+'#'+reciveMsg.from,true)
                    }

                    reciveMsg.to = reciveMsg.from;
                    reciveMsg.noSelf = true;
                    content.commit('addMessage', reciveMsg);

                })
            }, error => {

            })
        }
    }
});

store.watch(function (state) {
    return state.sessions
}, function (val) {
    console.log("val" + val)
    window.localStorage.setItem('vue-chat-session', JSON.stringify(val));
}, {
    deep: true/*这个貌似是开启watch监测的判断,官方说明也比较模糊*/
})

export default store;