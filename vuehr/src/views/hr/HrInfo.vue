<template>
    <div class="hrInfo">
        <el-card class="box-card" style="width: 300px">
            <div slot="header" class="clearfix" style="display: flex;justify-content:space-between">
                <div>{{hrInfo.name}}</div>
                <el-upload
                        :on-success="onSuccess"
                        :show-file-list="false"
                        class="upload-demo"
                        action="/userFace"
                >
                    <img :src="hrInfo.userface" title="点击修改图像" alt=""
                         style="width: 80px;height: 80px ;border-radius: 10px">
                </el-upload>

            </div>

            <div class="hrInfo">
                电话: {{hrInfo.telephone}}
            </div>
            <div class="hrInfo">
                手机: {{hrInfo.phone}}
            </div>
            <div class="hrInfo">
                地址: {{hrInfo.address}}
            </div>
            <div class="hrInfo">
                角色:
                <el-tag type="success" v-for="(r,index) in hrInfo.roles">{{r.nameZh}}</el-tag>
            </div>
            <div style="margin-top: 10px;display: flex;justify-content: space-between">
                <el-button size="mini" @click="dialogVisible = true" type="primary">修改信息</el-button>
                <el-button size="mini" type="danger" @click="passdialogVisible = true">修改密码</el-button>
            </div>
        </el-card>

        <el-dialog
                title="修改用户信息"
                :visible.sync="dialogVisible"
                width="30%">
            <div>
                <table>
                    <tr>
                        <td>
                            <el-tag type="primary" size="normal">姓名</el-tag>
                        </td>
                        <td>
                            <el-input v-model="hr2.name"></el-input>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag type="primary" size="normal">手机</el-tag>
                        </td>
                        <td>
                            <el-input v-model="hr2.phone"></el-input>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag type="primary" size="normal">电话</el-tag>
                        </td>
                        <td>
                            <el-input v-model="hr2.telephone"></el-input>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag type="primary" size="normal">地址</el-tag>
                        </td>
                        <td>
                            <el-input v-model="hr2.address"></el-input>
                        </td>
                    </tr>
                </table>
            </div>

            <span slot="footer" class="dialog-footer">
    <el-button @click="cancelUpdate">取 消</el-button>
    <el-button type="primary" @click="updateInfo">确 定</el-button>
  </span>
        </el-dialog>

        <el-dialog
                title="修改密码"
                :visible.sync="passdialogVisible"
                width="30%">
            <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px"
                     class="demo-ruleForm">
                <el-form-item label="旧密码" prop="oldPass">
                    <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="pass">
                    <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="checkPass">
                    <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
                    <el-button @click="resetForm('ruleForm')">重置</el-button>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">

  </span>
        </el-dialog>


    </div>
</template>

<script>
    export default {
        name: "HrInfo",
        data() {
            var checkAge = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('年龄不能为空'));
                }
                setTimeout(() => {
                    if (!Number.isInteger(value)) {
                        callback(new Error('请输入数字值'));
                    } else {
                        if (value < 18) {
                            callback(new Error('必须年满18岁'));
                        } else {
                            callback();
                        }
                    }
                }, 1000);
            };
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.ruleForm.checkPass !== '') {
                        this.$refs.ruleForm.validateField('checkPass');
                    }
                    callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (value !== this.ruleForm.pass) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return {
                ruleForm: {
                    pass: '',
                    checkPass: '',
                    oldPass: ''

                },
                rules: {
                    pass: [
                        {validator: validatePass, trigger: 'blur'}
                    ],
                    checkPass: [
                        {validator: validatePass2, trigger: 'blur'}
                    ],
                    age: [
                        {validator: checkAge, trigger: 'blur'}
                    ]
                },

                hrInfo: {},
                dialogVisible: false,
                passdialogVisible: false,
                hr2: {}
            }
        },//
        methods: {
            onSuccess() {
                this.initHrInfo()


            },
            initHrInfo() {
                this.getRequest('/hrInfo').then(resp => {
                    if (resp.code == 200) {
                        this.hrInfo = resp.data
                        this.hr2 = Object.assign({}, this.hrInfo)
                        this.$store.commit('INIT_CURRENTHR', this.hr2)
                        window.sessionStorage.setItem("user", JSON.stringify(this.hr2))

                    }
                })
            },
            cancelUpdate() {
                this.dialogVisible = false
                this.hr2 = Object.assign({}, this.hrInfo)
            },
            updateInfo() {
                this.putRequest('/hrInfo', this.hr2).then(resp => {
                    if (resp.code == 200) {
                        this.dialogVisible = false
                        this.initHrInfo()
                    }
                })
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.ruleForm.hrId = this.hrInfo.id
                        this.putRequest('/hrPass', this.ruleForm).then(resp => {
                            if (resp.code == 200) {
                                this.dialogVisible = false
                                this.getRequest('/logout')
                                // window.sessionStorage.removeItem("user");
                                window.sessionStorage.clear();
                                this.$store.commit('initRoutes', []);//注销时清空store中的菜单数据
                                this.$router.replace("/")
                            }
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        },
        mounted() {
            this.initHrInfo();
        }
    }
</script>

<style>
    .hrInfo {
        font-size: 15px;
        font-family: 楷体;
        color: #00b3ff;
    }
</style>