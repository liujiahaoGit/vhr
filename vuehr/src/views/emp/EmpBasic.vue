<template>
    <div>
        <div>
            <div style="display: flex;justify-content:space-between">
                <div>
                    <el-input style="width: 300px;margin-right: 5px" :disabled="isShowSearch"
                              placeholder="请输入搜索的姓名..."
                              prefix-icon="el-icon-search"
                              v-model="advancedSearch.name"
                              clearable
                              @clear="initEmp"
                              @keydown.enter.native="initEmp">
                    </el-input>
                    <el-button class="el-icon-search" type="primary" :disabled="isShowSearch" @click="initEmp">搜索
                    </el-button>
                    <el-button type="primary" @click="isShowSearch=!isShowSearch">
                        <i :class="isShowSearch?'fa fa-angle-double-up':'fa fa-angle-double-down'"
                           aria-hidden="true"></i>
                        高级搜索
                    </el-button>
                </div>

                <div>
                    <el-upload style="display: inline-flex ;margin-right: 5px"
                               class="upload-demo"
                               action="/employee/basic/import"
                               :multiple="false"
                               :show-file-list="false"
                               :on-success="onSuccess"
                               :on-progress="onProgress"
                               :on-error="onFail"
                               :disabled="disabled"
                    >
                        <el-button type="success">
                            <i :class="el_icon" aria-hidden="true"></i>
                            {{importSource}}
                        </el-button>
                    </el-upload>

                    <el-button type="success" @click="exportEmp">
                        <i class="el-icon-download" aria-hidden="true"></i>
                        导出数据
                    </el-button>
                    <el-button class="el-icon-plus" type="primary" @click="addEmp">添加员工</el-button>
                </div>

            </div>
            <transition name="slide-fade">
                <div class="searchDiv" v-show="isShowSearch">
                    <el-row :gutter="0" style="display: flex;justify-content: space-around;margin-top: 5px">
                        <el-col :span="5" style="margin-left: 5px">
                            政治面貌:
                            <el-select v-model="advancedSearch.politicId" placeholder="政治面貌" size="mini"
                                       style="width: 135px; ">
                                <el-option
                                        v-for="item in politicsstatus"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="4">
                            民族:
                            <el-select v-model="advancedSearch.nationId" placeholder="民族" size="mini"
                                       style="width: 120px">
                                <el-option
                                        v-for="item in nation"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="4">
                            职位:
                            <el-select v-model="advancedSearch.posId" placeholder="职位" size="mini" style="width: 120px">
                                <el-option
                                        v-for="item in position"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="4">
                            职称:
                            <el-select v-model="advancedSearch.jobLevelId" placeholder="职称" size="mini"
                                       style="width: 120px">
                                <el-option
                                        v-for="item in joblevel"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </el-col>
                        <el-col :span="7">
                            <div>
                                聘用形式:
                                <el-radio v-model="advancedSearch.engageForm" label="劳动合同" style="margin-left: 5px ">
                                    劳动合同
                                </el-radio>
                                <el-radio v-model="advancedSearch.engageForm" label="劳务合同" style="margin-left: 0px  ">
                                    劳务合同
                                </el-radio>
                            </div>

                        </el-col>
                    </el-row>
                    <el-row :gutter="0" style="margin-left: 5px">
                        <el-col :span="6">
                            所属部门:
                            <el-popover
                                    size="mini"
                                    placement="right"
                                    title="请选择部门名称"
                                    width="200"
                                    trigger="manual"
                                    v-model="visible">
                                <el-tree :data="depts" :default-expand-all="true" :props="defaultProps"
                                         @node-click="advancedHandleNodeClick"></el-tree>
                                <div class="el-icon-place" style="width: 135px" @click="showDepts" slot="reference">
                                    {{deptName}}
                                </div>
                            </el-popover>
                        </el-col>
                        <el-col :span="14">
                            入职日期:
                            <el-date-picker
                                    value-format="yyyy-MM-dd"
                                    size="mini"
                                    unlink-panels
                                    v-model="advancedSearch.beginDate"
                                    type="daterange"
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期">
                            </el-date-picker>
                        </el-col>
                        <el-col :span="4">
                            <el-button size="mini" @click="cancelSearch">取消</el-button>
                            <el-button icon="el-icon-search" size="mini" type="primary" @click="initEmp('advanced')">
                                搜索
                            </el-button>
                        </el-col>
                    </el-row>
                </div>
            </transition>
        </div>

        <div style="margin-top: 10px;">
            <el-table
                    border
                    :data="epms"
                    stripe
                    style="width: 100%">
                <el-table-column
                        type="selection"
                        fixed
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="姓名"
                        fixed
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="workId"
                        label="工号"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="gender"
                        width="50"
                        label="性别">
                </el-table-column>
                <el-table-column
                        width="150"
                        prop="birthday"
                        label="出生日期">
                </el-table-column>
                <el-table-column
                        prop="idCard"
                        width="180"
                        label="身份证号码">
                </el-table-column>
                <el-table-column
                        prop="wedlock"
                        label="婚姻状况">
                </el-table-column>
                <el-table-column
                        prop="nation.name"
                        label="民族">
                </el-table-column>
                <el-table-column
                        prop="nativePlace"
                        label="籍贯">
                </el-table-column>
                <el-table-column
                        prop="politicsstatus.name"
                        label="政治面貌">
                </el-table-column>
                <el-table-column
                        width="150"
                        prop="email"
                        label="电子邮件">
                </el-table-column>
                <el-table-column
                        width="130"
                        prop="phone"
                        label="电话号码">
                </el-table-column>
                <el-table-column
                        width="180"
                        prop="address"
                        label="联系地址">
                </el-table-column>
                <el-table-column
                        prop="department.name"
                        width="120"
                        label="所属部门">
                </el-table-column>
                <el-table-column
                        width="150"
                        prop="position.name"
                        label="职位">
                </el-table-column>
                <el-table-column
                        width="150"
                        prop="jobLevel.name"
                        label="职称">
                </el-table-column>
                <el-table-column
                        prop="engageForm"
                        label="聘用形式">
                </el-table-column>
                <el-table-column
                        width="150"
                        prop="beginDate"
                        label="入职日期">
                </el-table-column>
                <el-table-column
                        width="150"
                        prop="conversionTime"
                        label="转正日期">
                </el-table-column>
                <el-table-column
                        width="150"
                        prop="beginContract"
                        label="合同起始日期">
                </el-table-column>
                <el-table-column
                        width="150"
                        prop="endContract"
                        label="合同终止日期">
                </el-table-column>
                <el-table-column
                        prop="contractTerm"
                        label="合同期限">
                </el-table-column>
                <el-table-column
                        prop="tiptopDegree"
                        label="最高学历">
                </el-table-column>
                <el-table-column label="操作" fixed="right" width="260">

                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                @click="editEmp(scope.row)" style=" padding: 3px; width: 50px">编辑
                        </el-button>
                        <el-button
                                size="mini"
                                type="primary"
                                @click="handleEdit(scope.$index, scope.row)" style=" padding: 3px; width: 100px">查看高级资料
                        </el-button>
                        <el-button
                                size="mini"
                                type="danger"
                                @click="handleDelete(scope.row)" style=" padding: 3px; width: 50px">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>

        </div>
        <div style="float: right;margin-top: 10px">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :page-sizes="[5, 10, 20, 50]"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="pagination.totalCount">
            </el-pagination>
        </div>
        <el-dialog
                :title="title"
                :visible.sync="dialogVisible"
                width="90%"
        >
            <div>
                <el-form :model="emp" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                    <el-row :gutter="0">
                        <el-col :span="6">
                            <el-form-item label="姓名" prop="name">
                                <el-input prefix-icon="el-icon-edit" v-model="emp.name" style="width: 150px"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="性别" prop="gender">
                                <el-radio v-model="emp.gender" label="男">男</el-radio>
                                <el-radio v-model="emp.gender" label="女" style="margin-left: 0px  ">女</el-radio>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="出生日期" prop="birthday">
                                <el-date-picker
                                        style="width: 200px"
                                        v-model="emp.birthday"
                                        type="date"
                                        placeholder="出生日期">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="政治面貌" prop="politicId">
                                <el-select v-model="emp.politicId" placeholder="政治面貌">
                                    <el-option
                                            v-for="item in politicsstatus"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row :gutter="0">
                        <el-col :span="6">
                            <el-form-item label="民族" prop="nationId">
                                <el-select v-model="emp.nationId" placeholder="民族" style="width: 150px">
                                    <el-option
                                            v-for="item in nation"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="籍贯" prop="nativePlace">
                                <el-input prefix-icon="el-icon-edit" v-model="emp.nativePlace"
                                          style="width: 150px"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="电子邮箱" prop="email">
                                <el-input prefix-icon="el-icon-message" v-model="emp.email"
                                          style="width: 200px"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="联系地址" prop="address">
                                <el-input prefix-icon="el-icon-edit" v-model="emp.address"
                                          style="width: 200px"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row :gutter="0">
                        <el-col :span="6">
                            <el-form-item label="职位" prop="posId">
                                <el-select v-model="emp.posId" placeholder="职位" style="width: 150px">
                                    <el-option
                                            v-for="item in position"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="职称" prop="jobLevelId">
                                <el-select v-model="emp.jobLevelId" placeholder="职称" style="width: 150px">
                                    <el-option
                                            v-for="item in joblevel"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="所属部门" prop="departmentId">
                                <el-popover
                                        placement="right"
                                        title="请选择部门名称"
                                        width="200"
                                        trigger="manual"
                                        v-model="visible">
                                    <el-tree :data="depts" :default-expand-all="true" :props="defaultProps"
                                             @node-click="handleNodeClick"></el-tree>
                                    <div class="el-icon-place" @click="showDepts" slot="reference">{{deptName}}</div>
                                </el-popover>

                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="电话号码" prop="phone">
                                <el-input prefix-icon="el-icon-phone" v-model="emp.phone"
                                          style="width: 200px"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row :gutter="0">
                        <el-col :span="6">
                            <el-form-item label="工号" prop="workId">
                                <el-input prefix-icon="el-icon-edit" v-model="emp.workId" disabled
                                          style="width: 150px"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="学历" prop="tiptopDegree">
                                <el-select v-model="emp.tiptopDegree" placeholder="职称" style="width: 150px">
                                    <el-option
                                            v-for="item in tiptopDegree"
                                            :key="item"
                                            :label="item"
                                            :value="item">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="毕业院校" prop="school">
                                <el-input prefix-icon="el-icon-school" v-model="emp.school"
                                          style="width: 200px"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="专业名称" prop="specialty">
                                <el-input prefix-icon="el-icon-edit" v-model="emp.specialty"
                                          style="width: 200px"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row :gutter="0">
                        <el-col :span="6">
                            <el-form-item label="入职日期" prop="beginDate">
                                <el-date-picker
                                        style="width: 150px"
                                        v-model="emp.beginDate"
                                        type="date"
                                        placeholder="入职日期">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="转正日期" prop="conversionTime">
                                <el-date-picker
                                        style="width: 150px"
                                        v-model="emp.conversionTime"
                                        type="date"
                                        placeholder="转正日期">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="合同起始日期" prop="beginContract">
                                <el-date-picker
                                        style="width: 200px"
                                        v-model="emp.beginContract"
                                        type="date"
                                        placeholder="合同起始日期">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="合同终止日期" prop="endContract">
                                <el-date-picker
                                        style="width: 200px"
                                        v-model="emp.endContract"
                                        type="date"
                                        placeholder="合同终止日期">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row :gutter="0">
                        <el-col :span="6">
                            <el-form-item label="身份证号码" prop="idCard">
                                <el-input prefix-icon="el-icon-edit" v-model="emp.idCard"
                                          style="width: 200px"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="6">
                            <el-form-item label="聘用形式：" prop="engageForm">
                                <el-radio v-model="emp.engageForm" label="劳动合同">劳动合同</el-radio>
                                <el-radio v-model="emp.engageForm" label="劳务合同" style="margin-left: 0px  ">劳务合同
                                </el-radio>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="婚姻状况：" prop="wedlock">
                                <el-radio v-model="emp.wedlock" label="已婚">已婚</el-radio>
                                <el-radio v-model="emp.wedlock" label="未婚" style="margin-left: 0px  ">未婚</el-radio>
                                <el-radio v-model="emp.wedlock" label="离异" style="margin-left: 0px  ">离异</el-radio>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="在职状态：" prop="workState">
                                <el-radio v-model="emp.workState" label="在职">在职</el-radio>
                                <el-radio v-model="emp.workState" label="离职" style="margin-left: 0px  ">离职</el-radio>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateOrAdd">确 定</el-button>
             </span>
        </el-dialog>
    </div>

</template>

<script>
    export default {
        name: "EmpBasic",
        data() {
            return {
                advancedSearch: {
                    name: '', //姓名
                    politicId: '', //政治面貌
                    nationId: '', //民族
                    posId: '', //职位
                    jobLevelId: '', //职称
                    engageForm: '',  //聘用形式
                    departmentId: '', //所属部门
                    beginDate: ''  //入职日期
                },
                isShowSearch: false,
                title: '添加员工',
                searchWord: '',
                epms: [],
                politicsstatus: [], //政治面貌
                nation: [], //民族
                position: [], //职位
                joblevel: [], //职称
                depts: [], //所属部门
                deptName: '',
                tiptopDegree: [
                    "博士",
                    "硕士",
                    "本科",
                    "大专",
                    "高中",
                    "初中",
                    "小学",
                    "其他",
                ],//学历
                emp: {

                    name: "测试用例",
                    gender: "男",
                    birthday: "1989-12-31 16:00:00",
                    idCard: "610122199001011256",
                    wedlock: "已婚",
                    nationId: 1,
                    nativePlace: "陕西",
                    politicId: 13,
                    email: "laowang@qq.com",
                    phone: "18565558897",
                    address: "深圳市南山区",
                    departmentId: null,
                    jobLevelId: 9,
                    posId: 29,
                    engageForm: "劳务合同",
                    tiptopDegree: "本科",
                    specialty: "信息管理与信息系统",
                    school: "深圳大学",
                    beginDate: "2017-12-31 16:00:00",
                    workState: "在职",
                    workId: "00000001",
                    contractTerm: 2,
                    conversionTime: "2018-03-31 16:00:00",
                    notworkDate: null,
                    beginContract: "2017-12-31 16:00:00",
                    endContract: "2019-12-31 16:00:00",
                    workAge: null
                },
                rules: {
                    name: [{required: true, message: '姓名不能为空', trigger: 'blur'}],
                    gender: [{required: true, message: '性别不能为空', trigger: 'blur'}],
                    birthday: [{required: true, message: '生日不能为空', trigger: 'blur'}],
                    idCard: [{required: true, message: '身份证号不能为空', trigger: 'blur'},
                        {
                            pattern: /^[1-9][0-9]{5}([1][9][0-9]{2}|[2][0][0|1][0-9])([0][1-9]|[1][0|1|2])([0][1-9]|[1|2][0-9]|[3][0|1])[0-9]{3}([0-9]|[X])$/
                            , message: '身份证格式不正确', trigger: ['blur', 'change']
                        }],
                    wedlock: [{required: true, message: '婚姻状况不能为空', trigger: 'blur'}],
                    nationId: [{required: true, message: '民族不能为空', trigger: 'blur'}],
                    nativePlace: [{required: true, message: '籍贯不能为空', trigger: 'blur'}],
                    politicId: [{required: true, message: '政治面貌不能为空', trigger: 'blur'}],
                    email: [{required: true, message: '邮件不能为空', trigger: 'blur'},
                        {type: 'email', message: '邮箱地址格式不正确', trigger: ['blur', 'change']}],
                    phone: [{required: true, message: '电话号码不能为空', trigger: 'blur'}],
                    address: [{required: true, message: '地址不能为空', trigger: 'blur'}],
                    departmentId: [{required: true, message: '所属部门不能为空', trigger: 'blur'}],
                    jobLevelId: [{required: true, message: '职称不能为空', trigger: 'blur'}],
                    posId: [{required: true, message: '职位不能为空', trigger: 'blur'}],
                    engageForm: [{required: true, message: '聘用形式不能为空', trigger: 'blur'}],
                    tiptopDegree: [{required: true, message: '学历不能为空', trigger: 'blur'}],
                    specialty: [{required: true, message: '所属专业不能为空', trigger: 'blur'}],
                    school: [{required: true, message: '毕业院校不能为空', trigger: 'blur'}],
                    beginDate: [{required: true, message: '入职日期不能为空', trigger: 'blur'}],
                    workState: [{required: true, message: '在职状态不能为空', trigger: 'blur'}],
                    workId: [{required: true, message: '工号不能为空', trigger: 'blur'}],
                    contractTerm: [{required: true, message: '合同期限不能为空', trigger: 'blur'}],
                    conversionTime: [{required: true, message: '转正日期不能为空', trigger: 'blur'}],
                    notworkDate: [{required: true, message: '离职日期不能为空', trigger: 'blur'}],
                    beginContract: [{required: true, message: '合同起始日期不能为空', trigger: 'blur'}],
                    workAge: [{required: true, message: '工龄不能为空', trigger: 'blur'}],
                    endContract: [{required: true, message: '合同终止不能为空', trigger: 'blur'}],
                },
                dialogVisible: false,
                pagination: {
                    currPage: 1,
                    pageSize: 10,
                    totalCount: 0,
                    pageTotal: 10
                },
                visible: false,
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                importSource: "导入数据",
                el_icon: 'el-icon-upload2',
                disabled: false
            }
        },
        methods: {
            cancelSearch() {
                this.isShowSearch = false
                this.advancedSearch = {
                    name: '', //姓名
                    politicId: '', //政治面貌
                    nationId: '', //民族
                    posId: '', //职位
                    jobLevelId: '', //职称
                    engageForm: '',  //聘用形式
                    departmentId: '', //所属部门
                    beginDate: ''  //入职日期
                }
            },
            advancedHandleNodeClick(data) {
                console.log(data);
                this.advancedSearch.departmentId = data.id
                this.deptName = data.name
                this.visible = false
            },
            onSuccess() {

                this.importSource = "导入数据"
                this.el_icon = 'el-icon-upload2'
                this.disabled = false
                this.$message({
                    message: '导入成功',
                    type: 'success'
                });
                this.initEmp()
            },
            onProgress() {
                this.importSource = "导入中"
                this.el_icon = 'el-icon-loading'
                this.disabled = true
            },
            onFail() {
                this.importSource = "导入数据"
                this.el_icon = 'el-icon-upload2'
                this.disabled = false
                this.$message({
                    message: '导入失败',
                    type: 'error'
                });
            },
            exportEmp() {
                window.open("/employee/basic/export", '_parent')  //在当前页面打开文件(文件下载)
            },
            emptyEmp() {
                this.emp = {
                    name: "",
                    gender: "",
                    birthday: "",
                    idCard: "",
                    wedlock: "",
                    nationId: 1,
                    nativePlace: "",
                    politicId: 13,
                    email: "",
                    phone: "",
                    address: "",
                    departmentId: null,
                    jobLevelId: 9,
                    posId: 29,
                    engageForm: "",
                    tiptopDegree: "",
                    specialty: "",
                    school: "",
                    beginDate: "",
                    workState: "",
                    workId: "",
                    contractTerm: 2,
                    conversionTime: "",
                    notworkDate: null,
                    beginContract: "",
                    endContract: "",
                    workAge: null
                }
            },
            editEmp(data) {
                console.log(data)
                this.deptName = data.department.name
                this.title = '员工修改'
                this.emp = data
                this.initData();
            },
            handleDelete(data) {
                this.$confirm('此操作将永久删除【' + data.name + '】, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/employee/basic/' + data.id).then(resp => {
                        if (resp.code == 200) {
                            this.initEmp()
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },

            updateOrAdd() {


                if (this.emp.id) {
                    console.log(this.emp.id)
                    //修改
                    this.updateEmp();

                } else {  //增加

                    this.doAdd();

                }
            },
            updateEmp() {

                this.$refs.ruleForm.validate((valid) => {
                    if (valid) {
                        console.log("valid:" + valid)

                        this.putRequest('/employee/basic', this.emp).then(resp => {
                            console.log("resp..." + resp)
                            if (resp.code == 200) {
                                console.log("resp2:" + resp.code)
                                this.dialogVisible = false;
                                this.emptyEmp();
                                this.initEmp()
                            }
                        })
                    } else {
                        this.$message({
                            showClose: true,
                            message: '请输入正确的相关必填字段',
                            type: 'warning'
                        });
                        return false;
                    }
                })
            },

            doAdd() {

                this.$refs.ruleForm.validate((valid) => {
                    if (valid) {
                        this.postRequest('/employee/basic', this.emp).then(resp => {
                            if (resp.code == 200) {
                                this.dialogVisible = false;

                                this.initEmp()
                            }
                        })
                    } else {
                        this.$message({
                            showClose: true,
                            message: '请输入正确的相关必填字段',
                            type: 'warning'
                        });
                        return false;
                    }
                })

            }
            ,

            handleNodeClick(data) {
                console.log(data);
                this.emp.departmentId = data.id
                this.deptName = data.name
                this.visible = false
            }
            ,
            showDepts() {
                this.visible = !this.visible
            }
            ,

            getWorkId() {
                this.getRequest('/employee/basic/workId').then(resp => {
                    if (resp.code == 200) {
                        this.emp.workId = resp.data;
                    }
                })
            }
            ,
            getAllDepts() {
                if (!window.sessionStorage.getItem("depts")) {
                    this.getRequest('/employee/basic/depts').then(resp => {
                        if (resp.code == 200) {
                            this.depts = resp.data;
                            window.sessionStorage.setItem("depts", JSON.stringify(this.depts))
                        }
                    })
                } else {
                    this.depts = JSON.parse(window.sessionStorage.getItem("depts"))
                }

            }
            ,
            getAllPoliticsStatus() {
                if (!window.sessionStorage.getItem("politicsstatus")) {
                    this.getRequest('/employee/basic/politicsStatus').then(resp => {
                        if (resp.code == 200) {
                            this.politicsstatus = resp.data;
                            window.sessionStorage.setItem("politicsstatus", JSON.stringify(this.politicsstatus))
                        }
                    })
                } else {
                    this.politicsstatus = JSON.parse(window.sessionStorage.getItem("politicsstatus"))
                }

            }
            ,
            getAllNation() {
                if (!window.sessionStorage.getItem("nation")) {
                    this.getRequest('/employee/basic/nation').then(resp => {
                        if (resp.code == 200) {
                            this.nation = resp.data;
                            window.sessionStorage.setItem("nation", JSON.stringify(this.nation))
                        }
                    })
                } else {
                    this.nation = JSON.parse(window.sessionStorage.getItem("nation"))
                }

            }
            ,
            getAllPosition() {
                if (!window.sessionStorage.getItem("position")) {
                    this.getRequest('/employee/basic/position').then(resp => {
                        if (resp.code == 200) {
                            this.position = resp.data.list;
                            window.sessionStorage.setItem("position", JSON.stringify(this.position))
                        }
                    })
                } else {
                    this.position = JSON.parse(window.sessionStorage.getItem("position"))
                }

            }
            ,
            getAllJoblevel() {
                if (!window.sessionStorage.getItem("joblevel")) {
                    this.getRequest('/employee/basic/joblevel').then(resp => {
                        if (resp.code == 200) {
                            this.joblevel = resp.data.list;
                            window.sessionStorage.setItem("joblevel", JSON.stringify(this.joblevel))
                        }
                    })
                } else {
                    this.joblevel = JSON.parse(window.sessionStorage.getItem("joblevel"))
                }

            }
            ,
            addEmp() {

                this.initData()
                this.emptyEmp()
                this.dialogVisible = true;
            },
            initData() {
                this.getAllPoliticsStatus()
                this.getAllNation()
                this.getAllJoblevel()
                this.getAllPosition()
                this.getWorkId()
                this.getAllDepts()
                this.dialogVisible = true;

            },
            handleSizeChange(pageSize) {
                this.pagination.pageSize = pageSize;
                this.initEmp()
            }
            ,
            handleCurrentChange(currentPage) {
                this.pagination.currPage = currentPage;
                this.initEmp()
            }
            ,
            initEmp(type) {
                let url = '/employee/basic?page=' + this.pagination.currPage + '&size=' + this.pagination.pageSize;
                if (type && type == 'advanced') {
                    if (this.advancedSearch.politicId) {
                        url += '&politicId=' + this.advancedSearch.politicId
                    }
                    if (this.advancedSearch.nationId) {
                        url += '&nationId=' + this.advancedSearch.nationId
                    }
                    if (this.advancedSearch.departmentId) {
                        url += '&departmentId=' + this.advancedSearch.departmentId
                    }
                    if (this.advancedSearch.engageForm) {
                        url += '&engageForm=' + this.advancedSearch.engageForm
                    }
                    if (this.advancedSearch.posId) {
                        url += '&posId=' + this.advancedSearch.posId
                    }
                    if (this.advancedSearch.jobLevelId) {
                        url += '&jobLevelId=' + this.advancedSearch.jobLevelId
                    }
                    if (this.advancedSearch.beginDate) {
                        url += '&beginDate=' + this.advancedSearch.beginDate
                    }

                } else {
                    if (this.advancedSearch.name) {
                        url += '&name=' + this.advancedSearch.name
                    }

                }
                console.log(url)
                this.getRequest(url).then(resp => {
                    if (resp.code == 200) {
                        this.epms = resp.data.list;
                        this.pagination.totalCount = resp.data.totalCount
                        this.pagination.pageTotal = resp.data.pageTotal
                    }
                })
            }
        }
        ,
        mounted() {
            this.initEmp()
            this.getAllPoliticsStatus()
            this.getAllNation()
            this.getAllJoblevel()
            this.getAllPosition()
            this.getWorkId()
            this.getAllDepts()
        }
    }
</script>

<style>
    .el-row {
        margin-bottom: 10px;
    }

    .el-icon-place {
        width: 200px;
        height: 26px;
        border: 1px silver solid;
        border-radius: 3px;
        align-items: center;
        line-height: 26px;
        padding-left: 10px;
        cursor: pointer;
        box-sizing: border-box;
        font-size: 13px;
    }

    .searchDiv {
        margin-top: 5px;
        width: 100%;
        border: 1px solid #00e8ff;
        border-radius: 5px;
    }

    /* 可以设置不同的进入和离开动画 */
    /* 设置持续时间和动画函数 */
    .slide-fade-enter-active {
        transition: all .8s ease;
    }

    .slide-fade-leave-active {
        transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
    }

    .slide-fade-enter, .slide-fade-leave-to
        /* .slide-fade-leave-active for below version 2.1.8 */
    {
        transform: translateX(10px);
        opacity: 0;
    }
</style>