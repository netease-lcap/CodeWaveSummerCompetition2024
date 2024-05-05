<template>
    <div class="container">
        <!--画面div-->
        <div
            class="main-window"
            ref="large"></div>
        <!--小画面div-->
        <div
            class="sub-window"
            :style="this.subStyle"
            ref="small"></div>
        <!--底层栏-->
        <div class="tab-bar">
            <div
                class="tab-bar-item"
                :class="{ silence: isAudioOpen, notSilence: !isAudioOpen }"
                @click="setOrRelieveSilence"></div>
            <div
                class="tab-bar-item over"
                @click="overVideo"></div>
            <div
                class="tab-bar-item"
                :class="{ stop: isVideoOpen, start: !isVideoOpen }"
                @click="stopOrOpenVideo"></div>
        </div>
    </div>
</template>

<script>
    import { baseMixin } from '../../mixins';

    export default {
        name: 'jxt-nertc',
        mixins: [baseMixin],
        props: {
            subWidth: {
                type: String,
                default: '165px',
            },
            subHeight: {
                type: String,
                default: '95px',
            },
            subZIndex: {
                type: Number,
                default: 9,
            },
            subTop: {
                type: String,
                default: '16px',
            },
            subRight: {
                type: String,
                default: '16px',
            },
            borderColor: {
                type: String,
                default: '#ffffff',
            },
        },
        data() {
            return {
                remoteStream: null,
                isAudioOpen: this.audio,
                isVideoOpen: this.video,
            };
        },
        computed: {
            subStyle() {
                return {
                    width: this.subWidth,
                    height: this.subHeight,
                    zIndex: this.subZIndex,
                    top: this.subTop,
                    right: this.subRight,
                    borderColor: this.borderColor,
                };
            },
        },
        methods: {
            /**
             * 加载本地视频
             */
            loadLocalVideo() {
                const mainVideo = this.$refs.large;
                this.localStream.play(mainVideo);
                // 设置视频窗口大小
                this.localStream.setLocalRenderMode({
                    width: mainVideo.clientWidth,
                    height: mainVideo.clientHeight,
                    cut: false, // 是否裁剪
                });
            },
            /**
             * 加载远程视频
             */
            async loadRemoteVideo(remoteStream) {
                const subVideo = this.$refs.small;
                const playOptions = {
                    audio: true,
                    video: true,
                };
                await remoteStream.play(subVideo, playOptions);
                // 设置视频窗口大小
                remoteStream.setRemoteRenderMode({
                    width: subVideo.clientWidth,
                    height: subVideo.clientHeight,
                    cut: false, // 是否裁剪
                });
            },
            async subscribe() {
                this.remoteStream.setSubscribeConfig({
                    audio: true,
                    video: true,
                });
                await this.client.subscribe(this.remoteStream);
            },
            /**
             * 打开或关闭音频
             */
            async setOrRelieveSilence() {
                if (!this.localStream) {
                    return;
                }
                const open = !this.isAudioOpen;
                try {
                    await this.toggleAudio(open);
                    this.isAudioOpen = open;
                } catch (error) {
                    console.error('打开/关闭 mic 失败', error);
                }
            },
            /**
             * 打开或关闭视频
             */
            async stopOrOpenVideo() {
                if (!this.localStream) {
                    return;
                }
                const open = !this.isVideoOpen;
                try {
                    await this.toggleVideo(open);
                    this.isVideoOpen = open;
                    if (open) {
                        this.loadLocalVideo();
                    }
                } catch (error) {
                    console.error('打开/关闭摄像头失败', error);
                }
            },
            onPeerLeave(event) {
                if (!this.remoteStream) return;
                if (this.remoteStream.getId() === event.uid) {
                    this.remoteStream = null;
                    console.info('对方离开房间');
                }
            },
            async onStreamAdded(event) {
                const remoteStreamMap = this.client.adapterRef.remoteStreamMap;
                // 已经有两人在房间中，剔除（不能排除观众）
                if (Object.keys(remoteStreamMap).length === 2) {
                    await this.handleOver();
                    return;
                }
                const remoteStream = event.stream;
                // 房间里第三个人加入，忽略
                if (this.remoteStream && this.remoteStream.getId() !== remoteStream.getId()) {
                    return;
                } else {
                    this.remoteStream = remoteStream;
                }
                this.subscribe(remoteStream).catch((error) => {
                    console.error('订阅加入用户失败=====>', error);
                });
            },
            onStreamRemoved(event) {
                const remoteStream = event.stream;
                remoteStream.stop(event.mediaType);
            },
            async onStreamSubscribed(event) {
                const remoteStream = event.stream;
                this.loadRemoteVideo(remoteStream);
            },
        },
    };
</script>

<style scoped>
    .container {
        position: relative;
        width: 100%;
        height: 500px;
    }
    .main-window {
        width: 100%;
        height: calc(100% - 88px);
        background-color: #25252d;
    }
    .sub-window {
        background-color: #25252d;
        position: absolute;
        border-width: 1px;
        border-style: solid;
    }

    .tab-bar {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 100%;
        height: 88px;
        max-height: 118px;
        padding: 5px 0;
        color: #fff;
        background-image: linear-gradient(180deg, #292933 7%, #212129 100%);
        box-shadow: 0 0 0 0 rgba(255, 255, 255, 0.3);
    }
    .tab-bar-item {
        height: 100%;
        width: 125px;
        margin: 10px;
        background-repeat: no-repeat;
        background-position: center;
        background-size: contain;
        cursor: pointer;
    }

    .over {
        background-image: url('../../assets/images/over.png');
    }
    .over:hover {
        background-image: url('../../assets/images/over-hover.png');
    }
    .over:active {
        background-image: url('../../assets/images/over-click.png');
    }

    .silence {
        background-image: url('../../assets/images/silence.png');
    }
    .silence:hover {
        background-image: url('../../assets/images/silence-hover.png');
    }
    .silence:active {
        background-image: url('../../assets/images/silence-click.png');
    }

    .notSilence {
        background-image: url('../../assets/images/relieve-silence.png');
    }
    .notSilence:hover {
        background-image: url('../../assets/images/relieve-silence-hover.png');
    }
    .notSilence:active {
        background-image: url('../../assets/images/relieve-silence-click.png');
    }

    .stop {
        background-image: url('../../assets/images/stop.png');
    }
    .stop:hover {
        background-image: url('../../assets/images/stop-hover.png');
    }
    .stop:active {
        background-image: url('../../assets/images/stop-click.png');
    }

    .start {
        background-image: url('../../assets/images/open.png');
    }
    .start:hover {
        background-image: url('../../assets/images/open-hover.png');
    }
    .start:active {
        background-image: url('../../assets/images/open-click.png');
    }
</style>
