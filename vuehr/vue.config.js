let proxyObj={};
proxyObj['/ws']={
    ws:true,
    target:'ws://localhost:8082'
}
proxyObj['/']={
    ws:false,  //是否启用websockets
    target:'http://localhost:8082',
    changeOrigin: true, //开启跨域
    pathRewrite:{
        '^/':''
    }
}

module.exports={
    devServer:{
        host:'localhost',
        port:8080,
        open:true,//配置自动启动浏览器
        proxy:proxyObj
    }
}