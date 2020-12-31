import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import WeChat from '../views/chat/WeChat.vue'
import HrInfo from '../views/hr/HrInfo.vue'


Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Login',
        component: Login,
        hidden:true
    },
    {
        path: '/home',
        name: 'Home',
        component: Home,
        hidden:true,
        children:[
            {
                path: '/chat',
                name: '聊天页面',
                component: WeChat,
                hidden:true
            },
            {
                path: '/hrInfo',
                name: '个人中心',
                component: HrInfo,
                hidden:true
            }

        ]
    }
]

const router = new VueRouter({
    routes
})

export default router
