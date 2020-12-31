<template>

    <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="100px" class="loginForm">
        <h2 class="loginTitle">系统登录</h2>
        <el-form-item label="用户名" prop="username">
            <el-input v-model="loginForm.username" placeholder="请输入用户名" @keydown.enter.native="submit"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
            <el-input v-model="loginForm.password" placeholder="请输入密码" type="password"
                      @keydown.enter.native="submit"></el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code" class="verCode">
            <el-input v-model="loginForm.code" placeholder="请输入验证码" type="text" style="width: 120px"
                      @keydown.enter.native="submit"></el-input>
            <img :src="codeSrc" alt="验证码" @click="updateCode">
        </el-form-item>
        <el-checkbox v-model="rememberMe" class="rememberMe">记住密码</el-checkbox>
        <el-form-item>
            <el-button type="primary" style="width:150px" @click="submit">登录</el-button>
        </el-form-item>
    </el-form>

</template>


<script>
    import {Loading} from 'element-ui';

    export default {
        name: "Login",
        data() {
            return {
                loginForm: {
                    username: 'admin',
                    password: '123',
                    code: ''
                },
                codeSrc: '/code',
                rememberMe: true,
                rules: {
                    username: [
                        {required: true, message: '请输入用户名', trigger: 'blur'},
                        {min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '请输入密码', trigger: 'blur'},
                        {min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur'}
                    ],
                    code: [
                        {required: true, message: '请输入验证码', trigger: 'blur'}
                    ]
                }
            }
        },

        methods: {
            updateCode() {
                this.codeSrc = '/code?time=' + new Date();
            },
            submit() {
                this.$refs.loginForm.validate((valid) => {
                    Loading.service(true);
                    if (valid) {
                        this.postKeyValueRequest('/doLogin', this.loginForm).then(resp => {
                            Loading.service(true).close();
                            if (resp) {
                                // alert(JSON.stringify(resp.data))

                                window.sessionStorage.setItem("user", JSON.stringify(resp.data))
                                let path = this.$route.query.redirect;
                                console.log(path);
                                this.$router.replace(path == '/' || path == undefined ? "/home" : path)
                            }else {
                                this.updateCode();
                            }
                        })
                    } else {
                        this.$message({
                            showClose: true,
                            message: '请输入用户名或密码',
                            type: 'warning'
                        });
                        return false;
                    }
                });
            }
        }
    }
</script>

<style>
    .loginForm {
        border-radius: 15px;
        background-clip: padding-box;
        margin: 130px auto;
        width: 350px;
        padding: 15px 35px 15px 35px;
        background: #fff;
        border: 1px solid #eaeaea;
        box-shadow: 0 0 15px #cac6c6;
    }

    .loginTitle {
        margin: 15px auto 25px auto; /*上右下左*/
        text-align: center;
        color: #505458;
    }

    .rememberMe {
        text-align: left;
        margin: 0 0 15px 50px;
    }

    .el-form-item__content {
        display: flex;

        align-items: center;
    }

</style>