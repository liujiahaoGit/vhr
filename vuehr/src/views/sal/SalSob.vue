<template>
    <div>
        <div style="display: flex;justify-content: space-between;">
            <el-button type="primary" size="small" icon="el-icon-plus" @click="addSalary">添加员工账套</el-button>
            <el-button type="success" size="small" icon="el-icon-refresh" @click="initSalary"></el-button>
        </div>
        <div style="margin-top: 10px">
            <el-table
                    stripe
                    border
                    ref="multipleTable"
                    :data="salaries"
                    tooltip-effect="dark"
                    style="width: 100%"
                    @selection-change="handleSelectionChange"
            >
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        align="center"
                        label="账套名称"
                        width="120"
                        prop="name">
                </el-table-column>
                <el-table-column
                        align="center"
                        prop="basicSalary"
                        label="基本工资"
                        width="100">
                </el-table-column>
                <el-table-column
                        align="center"
                        prop="trafficSalary"
                        label="交通补助"
                        width="100"
                >
                </el-table-column>
                <el-table-column
                        align="center"
                        prop="lunchSalary"
                        label="午餐补助"
                        width="70">
                </el-table-column>
                <el-table-column
                        align="center"
                        prop="bonus"
                        label="奖金"
                        width="70"
                >
                </el-table-column>
                <el-table-column
                        align="center"
                        prop="createDate"
                        label="启用时间"
                        width="100">
                </el-table-column>
                <el-table-column
                        align="center"
                        label="养老金"
                        width="100">
                    <el-table-column
                            align="center"
                            prop="pensionPer"
                            label="比率"
                            width="50">
                    </el-table-column>
                    <el-table-column
                            align="center"
                            prop="pensionBase"
                            label="基数"
                            width="50">
                    </el-table-column>
                </el-table-column>
                <el-table-column
                        align="center"
                        label="医疗保险"
                        width="100">
                    <el-table-column
                            align="center"
                            prop="medicalPer"
                            label="比率"
                            width="50">
                    </el-table-column>
                    <el-table-column
                            align="center"
                            prop="medicalBase"
                            label="基数"
                            width="50">
                    </el-table-column>
                </el-table-column>
                <el-table-column
                        align="center"
                        label="公积金"
                        width="100">
                    <el-table-column
                            align="center"
                            prop="accumulationFundPer"
                            label="比率"
                            width="50">
                    </el-table-column>
                    <el-table-column
                            align="center"
                            prop="accumulationFundBase"
                            label="基数"
                            width="50">
                    </el-table-column>
                </el-table-column>
                <el-table-column
                        align="center"
                        label="操作"
                >
                    <el-table-column
                            align="center"
                            label="编辑"
                    >
                        <template slot-scope="scope">
                            <el-button
                                    size="mini"
                                    @click="handleEdit(scope.row)">编辑
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column
                            align="center"
                            label="删除"
                    >
                        <template slot-scope="scope">
                            <el-button
                                    size="mini"
                                    type="danger"
                                    @click="handleDelete(scope.row)">删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table-column>
            </el-table>
        </div>

        <el-dialog
                title="编辑员工账套"
                :visible.sync="dialogVisible"
                width="50%"
        >
            <div style="display: flex; justify-content: space-around;align-items: center">
                <div style="height: 300px;">
                    <el-steps direction="vertical" :active="activeStep">
                        <el-step :title="item" v-for="(item,index) in stepSalary" :key="index"></el-step>
                    </el-steps>
                </div>
                <div v-show="index==activeStep" v-for="(value,key,index) in stepInput" :key="index">
                    {{stepSalary[index]}}：
                    <el-input v-model="salary[key]" style="width: 200px;"
                              :placeholder="'请输入'+stepSalary[index]+'...'" @input="change"
                              @keydown.enter.native="nextStep"></el-input>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
    <el-button type="primary" @click="nextStep">{{activeStep==10?'完成':'下一步'}}</el-button>
  </span>
        </el-dialog>
        <el-button type="danger" style="margin-top: 10px" @click="batchDelByIds()" :disabled="multipleSelection.length==0">批量删除</el-button>
    </div>
</template>

<script>
    export default {
        name: "SalSob",
        data() {
            return {
                multipleSelection: [],
                salaries: [],
                salary: {},
                dialogVisible: false,
                activeStep: 0,
                stepSalary: [
                    '基本工资',
                    '交通补助',
                    '午餐补助',
                    '奖金',
                    '养老金比率',
                    '养老金基数',
                    '医疗保险比率',
                    '医疗保险基数',
                    '公积金比率',
                    '公积金基数',
                    '账套名称'
                ],
                stepInput: {
                    basicSalary: 0,
                    trafficSalary: 0,
                    lunchSalary: 0,
                    bonus: 0,
                    pensionPer: 0,
                    pensionBase: 0,
                    medicalPer: 0,
                    medicalBase: 0,
                    accumulationFundPer: 0,
                    accumulationFundBase: 0,
                    name: ''
                }
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

                    this.deleteRequest('/salary/sob/' + ids).then(resp => {
                        if (resp.code == 200) {
                            this.initSalary()
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
            },
            change() {
                this.$forceUpdate()
            },
            handleEdit(data) {

                this.salary.name = data.name;
                this.salary.accumulationFundBase = data.accumulationFundBase;
                this.salary.accumulationFundPer = data.accumulationFundPer;
                this.salary.basicSalary = data.basicSalary;
                this.salary.bonus = data.bonus;
                this.salary.lunchSalary = data.lunchSalary;
                this.salary.medicalBase = data.medicalBase;
                this.salary.pensionBase = data.pensionBase;
                this.salary.medicalPer = data.medicalPer;
                this.salary.trafficSalary = data.trafficSalary;
                this.salary.pensionPer = data.pensionPer;
                this.salary.id = data.id;
                this.dialogVisible = true

            },
            handleDelete(data) {
                this.$confirm('此操作将永久删除【' + data.name + '】, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/salary/sob/' + data.id).then(resp => {
                        if (resp.code == 200) {
                            this.initSalary()
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            cancel() {
                this.activeStep = 0
                this.dialogVisible = false
            },
            nextStep() {
                if (this.activeStep == 10) {
                    if (this.salary.id) {
                        this.putRequest('/salary/sob', this.salary).then(resp => {
                            console.log("resp..." + resp)
                            if (resp.code == 200) {
                                this.dialogVisible = false;
                                this.initSalary()
                            }
                        })
                    } else {
                        this.postRequest('/salary/sob', this.salary).then(resp => {
                            if (resp.code == 200) {
                                this.dialogVisible = false;
                                this.initSalary()
                            }
                        })
                    }


                    this.dialogVisible = false
                }
                this.activeStep++;
            },
            initSalary() {
                this.getRequest('/salary/sob').then(resp => {
                    if (resp.code == 200) {
                        console.log(resp.data)
                        this.salaries = resp.data;
                    }
                })
            },
            addSalary() {
                this.salary = {
                    basicSalary: 0,
                    trafficSalary: 0,
                    lunchSalary: 0,
                    bonus: 0,
                    pensionPer: 0,
                    pensionBase: 0,
                    medicalPer: 0,
                    medicalBase: 0,
                    accumulationFundPer: 0,
                    accumulationFundBase: 0,
                    name: ''
                }
                this.activeStep = 0
                this.dialogVisible = true
            }
        },
        mounted() {
            this.initSalary()
        }
    }
</script>

<style>

</style>