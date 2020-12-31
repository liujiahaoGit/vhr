import {getRequest} from "./api";
import router from "../router";

//定义初始化菜单树的方法
export const initMenu = (router, store) => {
    if (store.state.routes.length > 0) {
        return;
    }

    getRequest('/system/config/menuTree').then(data => {
        if (data) {
            let fmtRoutes = formatRoutes(data.data);
            router.addRoutes(fmtRoutes); //将格式化好的菜单数据加入到路由中
            store.commit('initRoutes', fmtRoutes) //放入store(全局状态管理)中
            store.dispatch('connect'); //初始化websocket连接
        }
    })
};


//将后台返回的菜单树格式化成前台所需要的格式
export const formatRoutes = (routes) => {
    let fmRoutes = [];

    routes.forEach(router => {
        let {
            path,
            component,
            name,
            meta,
            iconCls,
            children
        } = router;
        if (children && children instanceof Array) {
            children = formatRoutes(children)
        }
        let prefix = component.substring(0, 3).toLowerCase().toString();
        let fmRouter = {
            path: path,
            name: name,
            meta: meta,
            iconCls: iconCls,
            children: children,
            component:(resolve) =>{
                if (component.startsWith('Home')){
                   // console.log(component)
                    require(['../views/'+ component + '.vue'], resolve)
                }else {
                    require(['../views/' + prefix + '/' + component + '.vue'], resolve)
                   // console.log(component)
                }


            }
        };
        //console.log(fmRouter)
        fmRoutes.push(fmRouter);

    });
    return fmRoutes;
}