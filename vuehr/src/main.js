import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import 'font-awesome/css/font-awesome.min.css';

import {postKeyValueRequest} from './util/api'
import {postRequest} from './util/api'
import {putRequest} from './util/api'
import {getRequest} from './util/api'
import {deleteRequest} from './util/api'
import {initMenu} from './util/menu'

/*将封装的这些方法注册为插件 这样可以在各个组件中直接可以通过this调用*/
Vue.prototype.postKeyValueRequest = postKeyValueRequest
Vue.prototype.postRequest = postRequest
Vue.prototype.putRequest = putRequest
Vue.prototype.getRequest = getRequest
Vue.prototype.deleteRequest = deleteRequest

Vue.use(ElementUI,{size:"small"}); //设置全局的ElementUI组件大小为small
Vue.config.productionTip = false

/*前置路由守卫*/
router.beforeEach((to, from, next) => {

    /*to: Route: 即将要进入的目标 路由对象
    from: Route: 当前导航正要离开的路由
    next()进行管道中的下一个钩子。如果全部钩子执行完了，则导航的状态就是 confirmed (确认的)。 类似于过滤器中的放行doFilter()
   */
    if (to.path=='/'){
        next();
    }else {
        if (sessionStorage.getItem("user")){
            initMenu(router,store);
            next();
        } else {
            next('/?redirect='+to.path);
        }
    }


})
new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
