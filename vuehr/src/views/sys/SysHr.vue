<template>

    <div>
        <div class="search">
            <el-input class="searchInput"
                      placeholder="默认显示部分用户,可以根据用户名搜索更多用户"
                      prefix-icon="el-icon-search"
                      v-model="searchWord"
                        @keydown.enter.native="searchHr">
            </el-input>
            <el-button icon="el-icon-search" type="primary" style="width: 80px" @click="searchHr">搜索</el-button>
        </div>
        <div class="hr-card">
            <el-card class="box-card" v-for="(hr,index) in hrs" :key="index">
                <div slot="header" class="clearfix">
                    <span>{{hr.name}}</span>
                    <el-button style="float: right; padding: 3px 0;color: red" type="text"
                               class="el-icon-delete" @click="deleteHrById(hr)"></el-button>
                </div>
                <div class="el-dropdown-link">
                    <img :src="hr.userface" :alt="hr.name" :title="hr.name">
                </div>
                <div class="hr-info">
                    <span>用户名:{{hr.name}}</span>
                    <span>手机号码:{{hr.phone}}</span>
                    <span>电话号码:{{hr.telephone}}</span>
                    <span>地址:{{hr.address}}</span>
                    <span>用户状态:

                            <el-switch
                                    @change="changeStuate(hr)"
                                    v-model="hr.enabled"
                                    active-color="#13ce66"
                                    inactive-color="#ff4949"
                                    active-text="开启"
                                    inactive-text="禁用">

                            </el-switch>


                    </span>

                    <span>用户角色:
                        <el-tag class="tag" type="success" v-for="(role,index) in hr.roles"
                                :key="index">{{role.nameZh}}</el-tag>

                        <el-popover

                                placement="right"
                                title="角色名称"
                                width="200"
                                trigger="click"
                                @hide="updateRoles(hr)"
                                @show="showRoles(hr)"
                        >
                               <el-select v-model="selectKey" multiple placeholder="请选择">

                                  <el-option

                                          v-for="(r,index) in roles"
                                          :key="index"
                                          :label="r.nameZh"
                                          :value="r.id"> <!--label:前台展示的值
                                                              value:传给后台的值
                                                          -->
                                  </el-option>
                               </el-select>
                            <el-button size="big" type="text"  slot="reference" icon="el-icon-more"></el-button>
                        </el-popover>
                    </span>

                </div>
            </el-card>
        </div>
    </div>
</template>

<script>

    export default {
        name: "SysHr",
        data() {
            return {
                searchWord: "",
                hrs: [],
                roles: [],
                selectKey: [],

            }
        },
        methods: {
            deleteHrById(hr){
                this.$confirm('此操作将永久删除【' + hr.name + '】角色, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/hr/'+hr.id).then(resp => {
                        if (resp.code == 200) {
                            this.initHr()
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            updateRoles(hr) {

                let flag = false;
                let roles = hr.roles;
                console.log("roles长度" + roles.length)
                console.log("selectKey长度" + this.selectKey.length)

                if (roles.length != this.selectKey.length) {
                    flag = true;
                } else {
                    let newRoles = [];
                    Object.assign(newRoles, roles);
                    for (let i = 0; i < newRoles.length; i++) {
                        for (let j = 0; j < this.selectKey.length; j++) {
                            if (newRoles[i].id == this.selectKey[j]) {
                                console.log("rid" + newRoles[i].id)
                                console.log("sid" + j)
                                newRoles.splice(i, 1)
                                i--;
                                break;
                            }
                        }
                    }
                    if (newRoles.length != 0) {
                        flag = true;
                    }
                }

                if (flag) {

                    let url = '&';
                    if (this.selectKey) {
                        this.selectKey.forEach(r => {
                            url += 'rids=' + r + '&';
                        })
                    }else {
                        url='&'
                    }

                    console.log(url)
                    this.putRequest('/system/hr/hrRole?hid=' + hr.id + url).then(resp => {

                        if (resp.code == 200) {
                            this.initHr()
                        }
                    })

                }

            },
            showSelectKey(id) {
                /**/
                this.selectKey = []
                this.getRequest('/system/hr/roles/' + id).then(resp => {
                    if (resp.code == 200) {

                        let r = resp.data
                        r.forEach(x => {
                            this.selectKey.push(x.id)
                            console.log(this.selectKey)
                        })
                    }
                })

            },
            showRoles(hr) {
                this.showSelectKey(hr.id)
                this.getRequest('/system/hr/roles').then(resp => {
                    if (resp.code == 200) {
                        this.roles = resp.data
                    }
                })
            },
            changeStuate(hr) {
                this.putRequest('/system/hr', hr).then(resp => {
                    if (resp.code == 200) {

                        this.initHr()
                    }
                })
            },
            searchHr(){
                this.initHr();
            },
            initHr() {
                this.getRequest('/system/hr?searchWord='+this.searchWord).then(resp => {
                    console.log(this.searchWord)
                    if (resp.code == 200) {

                        this.hrs = resp.data
                    }
                })
            }
        },
        mounted() {
            this.initHr()
        }
    }
</script>

<style>

    .searchInput {
        width: 400px;
        margin-right: 10px;

    }

    .search {
        display: flex;
        justify-content: center;
    }

    .box-card {
        width: 300px;
        margin-top: 20px;

    }

    .hr-card {
        display: flex;
        justify-content: space-around;
        flex-wrap: wrap;

    }

    .el-dropdown-link {
        cursor: pointer;
        color: #fff;
        align-items: center;
        display: flex;
        justify-content: center;
    }

    .hr-info span {
        font-size: 15px;
        font-family: 楷体;
        color: #00b3ff;
        display: block;
        margin-top: 5px;
    }

    .tag {
        width: 150px;
    }
</style>