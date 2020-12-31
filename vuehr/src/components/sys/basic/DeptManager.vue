<template>
    <div style="width: 500px;margin: 5px">
        <el-input
                placeholder="请输入部门名称"
                prefix-icon="el-icon-search"
                v-model="filterText">
        </el-input>

        <el-tree
                class="filter-tree"
                :data="depts"
                :props="defaultProps"
                default-expand-all
                :filter-node-method="filterNode"
                :highlight-current="true"
                :default-expand-all="false"
                :expand-on-click-node="false"
                ref="tree">
                  <span class="custom-tree-node" slot-scope="{ node, data }" style="width: 100%">
                  <span>{{ data.name }}</span>
                      <span style="width: 200px;">
                    <el-button style="padding: 5px;float: right ;margin-left: 10px"
                               type="primary"
                               size="mini"
                               @click="addDept(node, data)">

                      添加部门
                    </el-button>
                    <el-button style="padding: 5px;float: right;"
                               type="danger"
                               size="mini"
                               @click="removeDept(node, data)">
                      删除部门
                    </el-button>
                      </span>
                    </span>

        </el-tree>

        <el-dialog
                title="添加部门信息"
                :visible.sync="dialogVisible"
                width="30%">
            <div>
                <table>
                    <tr>
                        <td>
                            <el-tag size="big" type="info">上级部门</el-tag>
                        </td>
                        <td>
                            <el-select v-model="pname" placeholder="请选择" @change="change">
                                <el-option
                                        v-for="item in deptNames"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.name">
                                </el-option>
                            </el-select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag size="big" type="info">部门名称</el-tag>
                        </td>
                        <td>
                            <el-input
                                    placeholder="请输入部门名称"
                                    v-model="dept.name"
                                    clearable>
                            </el-input>
                        </td>
                    </tr>
                </table>
            </div>
            <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAdd">确 定</el-button>
  </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "DeptManager",
        data() {
            return {
                filterText: "",
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                dept: {
                    id: '',
                    name: '',
                    parentId: ''
                },
                pname: '',
                depts: [],
                deptNames: [],

                dialogVisible: false
            }

        },
        methods: {
            change() {

                console.log("pname" + this.pname)
                for (let i = 0; i < this.deptNames.length; i++) {
                    let d = this.deptNames[i];
                    if (this.pname == d.name) {
                        console.log("id" + d.id)
                        this.dept.parentId = d.id
                        return;
                    }
                }
            },
            initDeptNames() {
                this.getRequest('/system/basic/dept/queryAllDeptName').then(resp => {
                    if (resp.code == 200) {
                        console.log(resp.data)
                        this.deptNames = resp.data
                    }
                })
            },
            addChildren(depts) {
                for (let i = 0; i < depts.length; i++) {

                    let d = depts[i];
                    if (d.id == this.dept.parentId) {
                        d.children = d.children.concat(this.dept)
                        if (d.children.length>0){
                            d.isParent=true
                        }
                        return;
                    } else {
                        this.addChildren(d.children)
                    }
                }
            },
            doAdd() {
                this.postRequest('/system/basic/dept', this.dept).then(resp => {
                    if (resp.code == 200) {
                        this.addChildren(this.depts)
                        this.dialogVisible = false
                        this.dept = {
                            id: '',
                            name: '',
                            parentId: ''
                        }
                    }
                })
            },
            addDept(node, data) {
                this.dialogVisible = true
                this.initDeptNames()
                this.pname = data.name
                this.dept.parentId = data.id
                this.doAdd
            },
            removeChildren2Depts(pdept,depts, id) {

                for (let i = 0; i < depts.length; i++) {
                    let d = depts[i];
                    console.log(d)
                    if (d.id == id) {
                        depts.splice(i, 1)
                        if (depts.length==0){
                            pdept.isParent=false
                        }
                        console.log("data..."+d.toString())
                        console.log("data..."+d.parent)
                    } else {
                        this.removeChildren2Depts(d,d.children, id)
                    }
                }
            },
            removeDept(node, data) {
                if (data.isParent == true) {
                    this.$message.error("该部门是父部门,不能直接删除")
                } else {
                    this.$confirm('此操作将永久删除【' + data.name + '】职位, 是否继续?', '提示', {

                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'

                    }).then(() => {

                            console.log("1111" + data.id)
                            this.deleteRequest('/system/basic/dept/' + data.id).then(resp => {
                                if (resp.code == 200) {

                                    this.removeChildren2Depts(null,this.depts, data.id);
                                }
                            })


                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消删除'
                        });
                    });
                }
                console.log(data)
            },
            initDepts() {
                this.getRequest('/system/basic/dept').then(resp => {
                    if (resp.code == 200) {
                        console.log(resp.data)
                        this.depts = resp.data
                    }
                })
            },
            filterNode(value, data) {
                if (!value) return true;
                return data.name.indexOf(value) !== -1;
            }
        },
        watch: {
            filterText(val) {
                this.$refs.tree.filter(val);
            }
        },
        mounted() {
            this.initDepts()
        }
    }
</script>

<style scoped>

</style>