<template>
    <div>
        <el-container>
            <el-header class="homeHeader">
                <div class="title">
                    微人事
                </div>
                <div>
                    <el-button @click="weChat" icon="el-icon-chat-round"
                               style="margin-right: 10px;background-color: #00b3ff;border: #00b3ff;"
                               size="normal"></el-button>
                    <el-dropdown @command="userInfoClick">
                    <span class="el-dropdown-link" style="text-align: center">
                      {{user.name}}<i><img :src="user.userface" alt="logo"></i>
                    </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="userInfo">个人中心</el-dropdown-item>
                            <el-dropdown-item command="setting">设置</el-dropdown-item>
                            <el-dropdown-item command="logout" divided>注销</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>

            </el-header>
            <el-container>
                <el-aside width="200px">
                    <el-menu router>
                        <el-submenu :index="index.toString()" :key="index" v-for="(item,index) in routes"
                                    v-if="!item.hidden">
                            <template slot="title">
                                <i :class="item.iconCls" style="color: #00b3ff;margin-right: 8px"></i>
                                <span>{{item.name}}</span>
                            </template>
                            <el-menu-item-group v-for="(child,index) in item.children" :key="index">
                                <el-menu-item :index="child.path">{{child.name}}</el-menu-item>
                            </el-menu-item-group>
                        </el-submenu>
                    </el-menu>
                </el-aside>
                <el-main>
                    <el-breadcrumb separator="/" v-if="this.$router.currentRoute.path!='/home'">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>
                            <a :href="'#'+this.$router.currentRoute.path">{{this.$router.currentRoute.name}}</a>
                        </el-breadcrumb-item>
                    </el-breadcrumb>
                    <div class="homeWelcome" v-if="this.$router.currentRoute.path=='/home'">欢迎来到微人事</div>
                    <router-view class="homeRouterView"/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "Home",
        data() {
            return {
                //user: JSON.parse(window.sessionStorage.getItem("user"))
            }
        },
        computed: {
            routes() {
                //console.log(this.$store.state.routes)
                return this.$store.state.routes;  //返回store里面存储的路由菜单对象
            },

            user() {
                //在个人中心里修改完用户名后 要更新home组件里的用户名,则将user放入计算属性里,可以直接拿到更新后store中state的值
                return this.$store.state.currentHr
            }
        },
        methods: {
            weChat() {
                this.$router.replace('/chat')
            },
            userInfoClick(cmd) {
                if (cmd == "logout") {
                    this.$confirm('此操作将注销系统, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',

                        type: 'warning'
                    }).then(() => {
                        this.getRequest('/logout')
                        // window.sessionStorage.removeItem("user");
                        window.sessionStorage.clear();
                        this.$store.commit('initRoutes', []);//注销时清空store中的菜单数据
                        this.$router.replace("/")
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消操作'
                        });
                    });
                } else {
                    this.$router.replace("/hrInfo")
                }
            }
        }
    }
</script>

<style>

    .homeWelcome {
        text-align: center;
        font-size: 50px;
        color: #00b3ff;
        font-family: 华文行楷;
        padding-top: 50px;
    }

    .homeHeader {
        background-color: #00b3ff;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 15px;
        box-sizing: border-box;
    }

    .homeHeader .title {
        font-size: 30px;
        font-family: 华文行楷;
        color: #fff;
    }

    .el-dropdown-link {
        cursor: pointer;
        color: #fff;
        align-items: center;
        display: flex;
    }

    .el-icon-arrow-down {
        font-size: 15px;
    }

    .el-dropdown-link img {
        width: 50px;
        height: 50px;
        border-radius: 24px;
        margin-left: 10px;
    }

    .homeRouterView {
        margin-top: 20px;
    }
</style>