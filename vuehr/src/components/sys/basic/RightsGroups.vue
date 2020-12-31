<template>
    <div>
        <div class="rightsInput">
            <el-input placeholder="请输入角色英文名称" v-model="role.name">
                <template slot="prepend">ROLE_</template>
            </el-input>
            <el-input placeholder="请输入角色中文名称" v-model="role.nameZh"></el-input>
            <el-button type="primary" @click="addRole">添加角色</el-button>
        </div>
        <div class="collapse">
            <el-collapse v-model="activeName" accordion v-for="(r,index) in roles" :key="index"
                         @change="initMenus">
                <el-collapse-item :title="r.nameZh" :name="r.id">
                    <el-card class="box-card">
                        <div slot="header" class="clearfix">
                            <span>可访问的资源</span>
                            <el-button style="float: right; padding: 3px 0;color: red" type="text"
                                       class="el-icon-delete" @click="deleteRoleById(r)"></el-button>
                        </div>
                        <el-tree
                                :data="menus"
                                show-checkbox
                                ref="tree"
                                :key="index"
                                node-key="id"
                                :default-checked-keys="checkedKeys"
                                :props="defaultProps">
                        </el-tree>
                        <div style="display: flex;float: right;margin: 8px 0 8px 0">
                            <el-button plain round type="primary" @click="doUpdate(r.id,index)">确认修改</el-button>
                            <el-button plain round type="warning" @click="noUpdate">取消修改</el-button>
                        </div>
                    </el-card>


                </el-collapse-item>
            </el-collapse>

        </div>
    </div>
</template>

<script>
    export default {
        name: "RightsGroups",
        data() {
            return {
                role: {},
                roles: [],
                activeName: -1,
                menus: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                checkedKeys: []
            }
        },
        methods: {
            deleteRoleById(r){
                this.$confirm('此操作将永久删除【' + r.nameZh + '】角色, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/basic/roles/?ids=' + r.id).then(resp => {
                        if (resp.code == 200) {
                            this.initRoles()
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            addRole(){
                if (this.role.name&&this.role.nameZh) {
                    this.postRequest('/system/basic/roles', this.role).then(resp => {
                        if (resp.code == 200) {
                            this.initRoles()
                            this.role={}
                        }
                    })
                } else {
                    this.$message({
                        showClose: true,
                        message: '添加的相关字段不能为空',
                        type: 'warning'
                    });
                }
            },

            noUpdate(){
                this.activeName=-1;
            },
            doUpdate(rid, index) {
                console.log(rid)
                let tree = this.$refs.tree[index] //给树组件取一个名字(标识符,相当于document.getElementById())
                let selectKeys = tree.getCheckedKeys(true); //拿到所有当前被选中的节点Key
                let url = "";
                if (selectKeys){
                    selectKeys.forEach(key => {
                        url += "&mids=" + key;
                    })
                }
                this.putRequest('/system/basic/menu?rid=' + rid+url).then(resp => {
                    if (resp.code == 200) {
                        this.activeName=-1;
                    }
                })
                console.log(selectKeys)
            },
            initMenus(rid) {
                if (rid) {
                    console.log("id----" + rid)
                    this.getRequest('/system/basic/menu').then(resp => {
                        if (resp.code == 200) {
                            this.menus = resp.data
                            this.checkedKeys=[]
                            this.initMenusByRid(rid)
                        }
                    })


                }

            },

            initMenusByRid(id) {
                this.getRequest('/system/basic/roles/' + id).then(resp => {
                    if (resp.code == 200) {
                        this.checkedKeys = resp.data
                        console.log(resp.data)
                    }
                })


            },
            initRoles() {
                this.getRequest('/system/basic/roles').then(resp => {
                    if (resp.code == 200) {
                        console.log(resp)
                        this.roles = resp.data
                    }
                })
            },
        },
        mounted() {
            this.initRoles()
        }
    }
</script>

<style>

    .rightsInput .el-input {
        width: 280px;
        margin-right: 8px;
        margin-bottom: 15px;
    }

    .collapse {
        width: 500px;
    }


</style>