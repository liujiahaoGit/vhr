<template>
    <div>
        <div>
            <el-input size="small" style="width: 300px;margin-right: 10px"
                      placeholder="添加职位"
                      prefix-icon="el-icon-plus"
                      v-model="pos.name">
            </el-input>
            <el-button type="primary" size="small" icon="el-icon-plus" @click="addPosition()">添加</el-button>
        </div>
        <div class="block">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pagination.currPage"
                    :page-sizes="[5, 10, 15, 20]"
                    :page-size="pagination.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="pagination.totalCount">
            </el-pagination>
        </div>
        <div class="positionTable">
            <el-table size="small"
                      :data="position"
                      border
                      stripe
                      style="width: 100%"
                      @selection-change="handleSelectionChange">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="id"
                        label="编号"
                        width="50">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="职位名称"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        label="创建时间"
                        width="200">
                </el-table-column>
                <el-table-column
                        label="是否启用"
                        width="100">
                    <template slot-scope="scope">
                        <el-tag type="success" v-if="scope.row.enabled">已启用</el-tag>
                        <el-tag type="danger" v-else>已禁用</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                @click="handleEdit(scope.$index, scope.row)">编辑
                        </el-button>
                        <el-button
                                size="mini"
                                type="danger"
                                @click="handleDelete(scope.$index, scope.row)">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-dialog
                    title="修改职位名称"
                    :visible.sync="dialogVisible"
                    width="30%"
            >
                <div style="margin-bottom: 10px">
                    <el-tag>职位名称</el-tag>
                    <el-input placeholder="请输入内容" size="small" v-model="updatePos.name"
                              class="updatePosition"></el-input>
                </div>
                <div>
                    <el-tag style="float: left">是否启用</el-tag>
                    <div  class="switchBut">
                        <el-switch
                                style="display: block"
                                v-model="updatePos.enabled"
                                active-color="#13ce66"
                                inactive-color="#ff4949"
                                active-text="启用"
                                inactive-text="禁用">
                        </el-switch>
                    </div>
                </div>
                <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="updatePosition()">确 定</el-button>
  </span>
            </el-dialog>
        </div>
        <el-button type="danger" size="small" style="margin-top: 10px" @click="batchDelByIds()"
                   :disabled="multipleSelection.length==0">批量删除
        </el-button>
    </div>
</template>

<script>
    export default {
        name: "JobManager",
        data() {
            return {
                pos: {
                    name: "",
                    enabled:""
                },
                updatePos: {
                    name: "",
                    enabled:""
                },
                position: [],
                dialogVisible: false,
                pagination: {
                    currPage: 1,
                    pageSize: 5,
                    totalCount: 0,
                    pageTotal: 10
                },
                multipleSelection: []
            }
        },
        methods: {
            batchDelByIds() {
                this.$confirm('此操作将永久删除【' + this.multipleSelection.length + '】条记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let ids = '?';
                    this.multipleSelection.forEach(item => {
                        ids += 'ids=' + item.id + '&'
                    });

                    this.deleteRequest('/system/basic/pos/' + ids).then(resp => {
                        if (resp.code == 200) {
                            this.initPosition(this.pagination.currPage, this.pagination.pageSize);
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
                console.log(val)
            },
            //每页显示的条数
            handleSizeChange(val) {
                this.pagination.pageSize = val
                this.initPosition(1, val)
                this.pagination.currPage = 1
            },
            //显示第几页
            handleCurrentChange(val) {
                // 改变默认的页数
                this.pagination.currentPage = val
                // 切换页码时，要获取每页显示的条数
                this.initPosition(this.pagination.currentPage, this.pagination.pageSize)
            },
            initPosition(currPage, pageSize) {
                this.getRequest('/system/basic/pos?startPage=' + currPage + '&pageSize=' + pageSize).then(resp => {
                    if (resp.code == 200) {
                        console.log(resp)
                        this.position = resp.data.list
                        this.pagination.totalCount = resp.data.totalCount
                        this.pagination.pageSize = resp.data.pageSize
                        this.pagination.currPage = resp.data.currPage
                        this.pagination.pageTotal = resp.data.pageTotal

                    }
                })
            },
            addPosition() {
                if (this.pos.name.trim()) {
                    this.postRequest('/system/basic/pos', this.pos).then(resp => {
                        if (resp.code == 200) {
                            this.initPosition(this.pagination.currPage, this.pagination.pageSize);
                            this.pos.name = ''
                        }
                    })
                } else {
                    this.$message({
                        showClose: true,
                        message: '请输入正确的职位名称',
                        type: 'warning'
                    });
                }

            },
            handleEdit(index, row) {
                this.dialogVisible = true
                Object.assign(this.updatePos, row)
            },
            updatePosition() {
                this.putRequest('/system/basic/pos', this.updatePos).then(resp => {
                    if (resp.code == 200) {
                        this.initPosition(this.pagination.currPage, this.pagination.pageSize);
                        this.updatePos.name = ''
                    }
                })
                this.dialogVisible = false
            },
            handleDelete(index, row) {
                this.$confirm('此操作将永久删除【' + row.name + '】职位, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/basic/pos/?ids=' + row.id).then(resp => {
                        if (resp.code == 200) {
                            this.initPosition(this.pagination.currPage, this.pagination.pageSize);
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });

            }
        },
        mounted() {
            this.initPosition(this.pagination.currPage, this.pagination.pageSize);
        }
    }
</script>

<style>

    .positionTable {
        margin-top: 15px;
        width: 700px;
    }

    .updatePosition {
        width: 200px;
        margin-left: 10px;
    }

    .block {
        margin-top: 10px;
    }
    .switchBut{
        display: flex;
        padding: 8px 0 0 10px;
    }
</style>