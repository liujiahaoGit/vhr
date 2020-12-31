<template>
    <div id="list">
        <ul style="padding-left: 10px">
            <li v-for="item in hrs" :class="{ active: currentSession? item.username === currentSession.username :false}"
                @click="changeCurrentSession(item)">
                <!--   :class="[item.id === currentSession ? 'active':'']" -->
                <img class="avatar" :src="item.userface" :alt="item.name">
                <el-badge :is-dot="isBadge[user.username+'#'+item.username]" class="item"> <p class="name">{{item.name}}</p></el-badge>

            </li>
        </ul>
    </div>
</template>

<script>
    import {mapState} from 'vuex'

    export default {
        name: 'list',
        data() {
            return {
                user:JSON.parse(window.sessionStorage.getItem("user"))
            }
        },
        computed: mapState([
            'hrs',
            'currentSession',
            'isBadge'
        ]),
        methods: {
            changeCurrentSession(currentSession) {
                this.$store.commit('changeCurrentSession', currentSession)
            }
        }
    }
</script>

<style lang="scss" scoped>
    #list {

    li {
        padding: 10px 0px;
        border-bottom: 1px solid #292C33;
        cursor: pointer;
        list-style: none;

    &
    :hover {
        background-color: rgba(255, 255, 255, 0.03);
    }

    }
    li.active { /*注意这个是.不是冒号:*/
        background-color: rgba(255, 255, 255, 0.1);
    }

    .avatar {
        border-radius: 2px;
        width: 30px;
        height: 30px;
        vertical-align: middle;
    }

    .name {
        display: inline-block;
        margin:0 0 0 15px;


    }

    .item {
        margin-top: 10px;
        margin-right: 40px;
    }

    }
</style>
