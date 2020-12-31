<template>
    <div id="uesrtext">
        <textarea placeholder="按 Ctrl + Enter 发送" v-model="content" @keyup="addMessage"></textarea>
    </div>
</template>

<script>
    import {mapState} from 'vuex'

    export default {
        name: 'uesrtext',
        data() {
            return {
                content: ''
            }
        },
        computed: mapState([
            'sessions',
            'currentSession'
        ]),
        methods: {
            addMessage(e) {
                if (e.ctrlKey && e.keyCode === 13 && this.content.length) {
                    let msgObj = new Object();
                    msgObj.to = this.currentSession.username
                    //msgObj.to = 'libai'
                    msgObj.content = this.content
                    this.$store.state.stomp.send('/ws/chat', {}, JSON.stringify(msgObj)); //发送消息
                    this.$store.commit('addMessage', msgObj);
                    this.content = '';
                }
            }
        }
    }
</script>

<style lang="scss" scoped>
    #uesrtext {
        position: absolute;
        bottom: 0;
        right: 0;
        width: 100%;
        height: 30%;
        border-top: solid 1px #DDD;

    > textarea {
        padding: 10px;
        width: 100%;
        height: 100%;
        border: none;
        outline: none;
    }

    }
</style>
