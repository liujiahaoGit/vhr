<template>
    <div>
        <el-tabs v-model="activeName" type="card">
            <el-tab-pane label="部门管理" name="first">
                <DeptManager></DeptManager>
            </el-tab-pane>
            <el-tab-pane label="职位管理" name="second">
                <JobManager></JobManager>
            </el-tab-pane>
            <el-tab-pane label="职称管理" name="third">
                <PostManager></PostManager>
            </el-tab-pane>
            <el-tab-pane label="奖惩规则" name="fourth">
                <RewardRules></RewardRules>
            </el-tab-pane>
            <el-tab-pane label="权限组" name="fifth">
                <RightsGroups></RightsGroups>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>


<script>

    import DeptManager from '../../components/sys/basic/DeptManager'
    import JobManager from '../../components/sys/basic/JobManager'
    import PostManager from '../../components/sys/basic/PostManager'
    import RewardRules from '../../components/sys/basic/RewardRules'
    import RightsGroups from '../../components/sys/basic/RightsGroups'

    export default {
        name: "SysBasic",
        data() {
            return {
                activeName: 'first'
            };
        },
        //注册为组件
        components: {
            DeptManager, JobManager, PostManager, RewardRules, RightsGroups
        }
    }
</script>

<style scoped>

</style>