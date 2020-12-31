<template>
    <div>
        <el-table
                size="mini"
                :data="emps"
                stripe
                border
                style="width: 100%">
            <el-table-column
                    type="selection"
                    width="55">
            </el-table-column>
            <el-table-column
                    prop="name"
                    label="姓名"
                    width="130">
            </el-table-column>
            <el-table-column
                    prop="workId"
                    label="工号"
                    width="130">
            </el-table-column>
            <el-table-column
                    prop="email"
                    label="电子邮件"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="phone"
                    label="电话号码"
                    width="130">
            </el-table-column>
            <el-table-column
                    prop="department.name"
                    label="所属部门"
                    width="180">
            </el-table-column>
            <el-table-column
                    align="center"
                    label="工资账套"
                    width="180">
                <template slot-scope="scope">
                    <el-tooltip placement="right-start">
                        <div slot="content">
                            <table>
                                <tr>
                                    <td>
                                        <el-tag size="mini" style="margin-right: 10px">基本工资</el-tag>
                                    </td>
                                    <td>￥{{scope.row.salary.basicSalary}}</td>
                                </tr>
                            </table>
                        </div>
                        <div slot="content">
                            <table>
                                <tr>
                                    <td>
                                        <el-tag size="mini" style="margin-right: 10px">奖金</el-tag>
                                    </td>
                                    <td>￥{{scope.row.salary.bonus}}</td>
                                </tr>
                            </table>
                        </div>
                        <div slot="content">
                            <table>
                                <tr>
                                    <td>
                                        <el-tag size="mini" style="margin-right: 10px">午餐补助</el-tag>
                                    </td>
                                    <td>￥{{scope.row.salary.lunchSalary}}</td>
                                </tr>
                            </table>
                        </div>
                        <div slot="content">
                            <table>
                                <tr>
                                    <td>
                                        <el-tag size="mini" style="margin-right: 10px">交通补助</el-tag>
                                    </td>
                                    <td>￥{{scope.row.salary.trafficSalary}}</td>
                                </tr>
                            </table>
                        </div>
                        <div slot="content">
                            <table>
                                <tr>
                                    <td>
                                        <el-tag size="mini" style="margin-right: 10px">养老金基数</el-tag>
                                    </td>
                                    <td>￥{{scope.row.salary.pensionBase}}</td>
                                </tr>
                            </table>
                        </div>
                        <div slot="content">
                            <table>
                                <tr>
                                    <td>
                                        <el-tag size="mini" style="margin-right: 10px">养老金比率</el-tag>
                                    </td>
                                    <td>{{scope.row.salary.pensionPer}}</td>
                                </tr>
                            </table>
                        </div>
                        <div slot="content">
                            <table>
                                <tr>
                                    <td>
                                        <el-tag size="mini" style="margin-right: 10px">公积金基数</el-tag>
                                    </td>
                                    <td>￥{{scope.row.salary.accumulationFundBase}}</td>
                                </tr>
                            </table>
                        </div>
                        <div slot="content">
                            <table>
                                <tr>
                                    <td>
                                        <el-tag size="mini" style="margin-right: 10px">公积金比率</el-tag>
                                    </td>
                                    <td>{{scope.row.salary.accumulationFundPer}}</td>
                                </tr>
                            </table>
                        </div>
                        <div slot="content">
                            <table>
                                <tr>
                                    <td>
                                        <el-tag size="mini" style="margin-right: 10px">医疗保险基数</el-tag>
                                    </td>
                                    <td>￥{{scope.row.salary.medicalBase}}</td>
                                </tr>
                            </table>
                        </div>
                        <div slot="content">
                            <table>
                                <tr>
                                    <td>
                                        <el-tag size="mini" style="margin-right: 10px">医疗保险比率</el-tag>
                                    </td>
                                    <td>{{scope.row.salary.medicalPer}}</td>
                                </tr>
                            </table>
                        </div>
                        <el-tag>{{scope.row.salary.name}}</el-tag>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column
                    align="center"
                    label="操作">
                <template slot-scope="scope">

                    <el-popover
                            placement="right"
                            width="200"
                            @show="showSalName(scope.row)"
                            @hide="updateSalary(scope.row)"
                            trigger="click">
                        <el-select v-model="currentSalary" placeholder="请选择">
                            <el-option
                                    v-for="item in salaries"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                        <el-button
                                slot="reference"
                                type="danger"
                                size="mini"
                        >修改账套
                        </el-button>
                    </el-popover>


                </template>
            </el-table-column>
        </el-table>

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
    </div>
</template>

<script>
    export default {
        name: "SalSobCfg",
        data() {
            return {
                emps: [],
                salaries: [],
                pagination: {
                    currPage: 1,
                    pageSize: 10,
                    totalCount: 0,
                    pageTotal: 10
                },
                currentSalary: -1,
                salary: {
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
                    name: '暂未设置'
                },
                loading: false
            }
        },
        methods: {
            handleSizeChange(pageSize) {
                this.pagination.pageSize = pageSize;
                this.initEmps()
            },
            handleCurrentChange(currPage) {
                this.pagination.currPage = currPage;
                this.initEmps()
            },
            updateSalary(data) {

                if (this.currentSalary && this.currentSalary != data.salary.id) {
                    this.putRequest('/salary/sobcfg?eid=' + data.id + '&sid=' + this.currentSalary).then(resp => {
                        if (resp.code == 200) {
                            this.initEmps()
                        }
                    })
                }



            },
            showSalName(data) {
                console.log("data.id"+data.salary.id)
                this.currentSalary = data.salary.id
            },
            initEmps() {
                this.getRequest('/salary/sobcfg?page=' + this.pagination.currPage + '&size=' + this.pagination.pageSize).then(resp => {
                    if (resp.code == 200) {

                        this.emps = resp.data.list
                        console.log(this.emps)
                        console.log(resp.data.list)
                        this.emps.forEach(e => {
                            if (e.salary == null || e.salary == undefined) {
                                e.salary = this.salary;
                            }
                        })
                        this.pagination.pageTotal = resp.data.pageTotal
                        this.pagination.totalCount = resp.data.totalCount

                    }
                })
            },
            initSalary() {
                this.getRequest('/salary/sobcfg/salaries').then(resp => {
                    if (resp.code == 200) {
                        this.salaries = resp.data
                        console.log(this.salaries)
                        console.log(resp.data)
                    }
                })
            }
        },
        mounted() {
            this.initEmps()
            this.initSalary()
        }
    }
</script>

<style>
    .block {
        margin-top: 10px;
        display: flex;
        justify-content: flex-end;
    }
</style>