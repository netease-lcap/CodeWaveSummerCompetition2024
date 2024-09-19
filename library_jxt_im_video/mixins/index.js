import NERTC from 'nertc-web-sdk/NERTC';
import { transFrameRate, transVideoQuality } from '../utils';

export const baseMixin = {
    props: {
        appKey: {
            type: String,
            default: '',
        },
        debug: {
            type: Boolean,
            default: true,
        },
        channelName: {
            type: String,
            default: '',
        },
        autoUid: {
            type: Boolean,
            default: true,
        },
        uid: {
            type: String,
            default: '',
        },
        authenticationType: {
            type: String,
            default: 'debug',
        },
        token: {
            type: String,
            default: '',
        },
        permKey: {
            type: String,
            default: '',
        },
        audio: {
            type: Boolean,
            default: true,
        },
        audioProfile: {
            type: String,
            default: 'speech_low_quality',
        },
        video: {
            type: Boolean,
            default: true,
        },
        frameRate: {
            type: String,
            default: 'default',
        },
        videoQuality: {
            type: String,
            default: '720P',
        },
    },
    emits: ['afterJoinRoom', 'afterLoadLocalSteam', 'roomOver', 'peerOnline', 'peerLeave', 'permkeyWillExpire', 'permkeyTimeout', 'toggleAudio', 'toggleVideo', 'overVideo'],
    data() {
        return {
            client: null,
            localStream: null,
            mounted: false,
        };
    },
    computed: {
        loadStart() {
            const mounted = this.mounted;
            const appKey = this.appKey;
            const channelName = this.channelName;
            const autoUid = this.autoUid;
            const uid = this.uid;
            const authenticationType = this.authenticationType;
            const token = this.token;
            const permKey = this.permKey;
            const result = { mounted, appKey, channelName, autoUid, uid, authenticationType, token, permKey };
            return result;
        },
    },
    watch: {
        loadStart(newValue) {
            // 设计模式不加载
            if (this.$env && this.$env.VUE_APP_DESIGNER) {
                return;
            }
            // client已存在，不再加载
            if (this.client) {
                return;
            }
            if (!newValue.mounted) {
                return;
            }
            // appKey必填
            if (!newValue.appKey) {
                console.warn('appKey未填写');
                return;
            }
            // 房间名必填
            if (!newValue.channelName) {
                console.warn('房间名未填写');
                return;
            }
            // 非自动生成 uid，需传入 uid 的值
            if (!newValue.autoUid && !newValue.uid) {
                console.warn('当前采用非自动生成 uid方式，请检查传递的 uid值');
                return;
            }
            // 安全模式需存在对应值
            const authenticationTypes = ['safe', 'safe-high'];
            if (authenticationTypes.includes(newValue.authenticationType) && !newValue.token) {
                console.warn('安全模式开启，但未填写 token 值');
                return;
            }
            // 安全模式（高级 token）
            if (newValue.authenticationType === 'safe-high' && !newValue.permKey) {
                console.warn('安全模式（高级 token）开启，但未填写 permKey 值');
                return;
            }
            this.load().catch((error) => {
                console.error(error);
                if (this.debug) {
                    const message = error.message || error.code || error;
                    this.$toast.show(message);
                }
            });
        },
    },
    mounted() {
        this.mounted = true;
    },
    methods: {
        async load() {
            await this.loadClient();
        },
        async loadClient() {
            const client = this.createClient(this.appKey, this.debug);
            this.client = client;
            this.addPeerListeners();
            this.addStreamListeners();
            this.addClientErrorListeners();
            this.addPermkeyListeners();
            // 加入房间
            const uid = this.autoUid ? 0 : this.uid;
            const token = ['safe', 'safe-high'].includes(this.authenticationType) ? this.token : '';
            const permKey = ['safe-high'].includes(this.authenticationType) ? this.permKey : '';
            await this.joinChannel(this.channelName, uid, token, permKey);
            const localUid = client.getUid();
            this.$emit('afterJoinRoom', { client: client, uid: localUid });
            this.localStream = this.createLocalStream(localUid, this.audio, this.video);
            await this.initLocalStream();
            this.$emit('afterLoadLocalSteam', { client: client, stream: this.stream, uid: localUid });
            await this.publish();
        },
        /**
         * 创建客户端
         * @param {string} appkey 应用的 AppKey
         * @param {boolean} debug 是否开启 debug 模式
         */
        createClient(appkey, debug) {
            return NERTC.createClient({
                appkey: appkey,
                debug,
            });
        },
        /**
         * 加入房间
         * @param {string} channelName 房间名称
         * @param {string|number} localUid 当前用户的唯一标识 id
         * @param {string} token 安全认证签名
         * @param {string} permKey 安全认证签名
         */
        async joinChannel(channelName, localUid, token, permKey) {
            if (!this.client) return;
            await this.client.join({
                channelName,
                uid: localUid,
                token,
                permKey,
            });
        },
        /**
         * 创建本地音视频流对象
         * @param {string|number} localUid 当前用户的唯一标识 id
         * @param {boolean} audio 是否启动音频
         * @param {video} video 是否启动视频
         */
        createLocalStream(localUid, audio, video) {
            if (!this.client) return;
            return NERTC.createStream({
                uid: localUid,
                audio, // 是否启动mic
                video, // 是否启动camera
            });
        },
        /**
         * 初始化本地视频流
         */
        async initLocalStream() {
            if (!this.client) return;
            // 设置本地视频质量
            this.setLocalVideoProfile(this.videoQuality, this.frameRate);
            // 设置本地音频质量
            this.setLocalAudioProfile(this.audioProfile);
            await this.localStream.init();
            this.loadLocalVideo();
        },
        /**
         * 设置本地视频质量
         * @param {number} resolution 视频分辨率枚举值
         * @param {number} frameRate 视频帧率枚举值
         */
        setLocalVideoProfile(resolution, frameRate) {
            this.localStream.setVideoProfile({
                resolution: transFrameRate(resolution), // 设置视频分辨率
                frameRate: transVideoQuality(frameRate), // 设置视频帧率
            });
        },
        /**
         * 设置本地音频质量
         * @param {string} profile 音频质量枚举值
         */
        setLocalAudioProfile(profile) {
            this.localStream.setAudioProfile(profile);
        },
        /**
         * 发布本地视频流给房间对端
         */
        async publish() {
            if (!this.client) return;
            await this.client.publish(this.localStream);
        },
        /**
         * 通话结束
         */
        async destroyClient() {
            if (this.client) {
                await this.client.leave();
            }
            this.localStream && this.localStream.destroy();
            this.client && this.client.destroy();
            this.client = null;
        },
        async overVideo() {
            try {
                await this.destroyClient();
            } catch (error) {
                console.error('离开房间失败=====>', error);
            }
            this.$emit('overVideo');
        },
        /**
         * 处理超出用户
         */
        async handleOver() {
            try {
                await this.destroyClient();
            } catch (error) {
                console.error('离开房间失败=====>', error);
            }
            this.$emit('roomOver');
        },
        /**
         * 切换音频打开/关闭
         * @param {boolean} open 是否打开摄像头
         */
        async toggleAudio(open) {
            this.$emit('toggleAudio', open);
            if (open) {
                await this.localStream.open({ type: 'audio' });
            } else {
                await this.localStream.close({ type: 'audio' });
            }
        },
        /**
         * 切换摄像头打开/关闭
         * @param {boolean} open 是否打开摄像头
         */
        async toggleVideo(open) {
            this.$emit('toggleVideo', open);
            if (open) {
                await this.localStream.open({ type: 'video' });
            } else {
                await this.localStream.close({ type: 'video' });
            }
        },
        addPeerListeners() {
            // 加入房间
            this.client.on('peer-online', (event) => {
                console.info(`${event.uid} 加入房间`);
                this.onPeerOnline(event);
                this.$emit('peerOnline', event);
            });

            // 离开房间
            this.client.on('peer-leave', (event) => {
                console.info(`${event.uid} 离开房间`);
                this.onPeerLeave(event);
                this.$emit('peerLeave', event);
            });
        },
        addStreamListeners() {
            this.client.on('stream-added', async (event) => {
                // 收到房间中其他成员发布自己的媒体的通知，对端同一个人同时开启了麦克风、摄像头、屏幕贡献，这里会通知多次
                this.onStreamAdded(event);
            });

            this.client.on('stream-removed', (event) => {
                this.onStreamRemoved(event);
            });

            this.client.on('stream-subscribed', async (event) => {
                const remoteStream = event.stream;
                this.addRemoteNotAllowedError(remoteStream);
                this.onStreamSubscribed(event);
            });
        },
        addPermkeyListeners() {
            this.client.on('permkey-will-expire', (event) => {
                console.warn(`permKey 即将过期}`);
                this.onPermKeyWillExpire(event);
                this.$emit.emits('permkeyWillExpire', { client: this.client });
            });
            this.client.on('permkey-timeout', (event) => {
                console.warn(`permkey已经超时}`);
                this.onPermKeyTimeout(event);
                this.$emit.emits('permkeyTimeout', { client: this.client });
            });
        },
        addClientErrorListeners() {
            // 本地 Client 异常监听
            this.client.on('error', (event) => {
                console.error('本地 client 出现异常=====>', event);
                this.onError(event);
            });

            // 设备权限异常监听
            this.client.on('accessDenied', (event) => {
                this.onAccessDenied(event);
            });
        },
        /**
         * 音频自动播放会被浏览器限制的问题
         * @param {*} remoteStream 远程视频流对象
         */
        addRemoteNotAllowedError(remoteStream) {
            remoteStream.on('notAllowedError', (error) => {
                this.onNotAllowedError(error);
            });
        },
        loadLocalVideo() {},
        onPeerOnline(event) {},
        onPeerLeave(event) {},
        onStreamAdded(event) {},
        onStreamRemoved(event) {},
        onStreamSubscribed(event) {},
        onPermKeyWillExpire(event) {},
        onPermKeyTimeout(event) {},
        onError(event) {
            if (event === 'SOCKET_ERROR') {
                this.$toast.show('网络异常，已经退出房间');
            } else {
                if (this.debug) {
                    this.$toast.show(event);
                }
            }
        },
        onAccessDenied(event) {
            this.$toast.show(`${event}设备开启的权限被禁止`);
        },
        onNotAllowedError(error) {
            const errorCode = error.getCode();
            // 处理音频自动播放会被浏览器限制的问题（https://doc.yunxin.163.com/nertc/docs/jM3NDE0NTI?platform=web）
            if (errorCode === 41030) {
                // 页面弹框加一个按钮，通过交互完成浏览器自动播放策略限制的解除
                let userGestureUI = document.getElementById('nertc-lowcode');
                if (userGestureUI) {
                    return;
                }
                userGestureUI = document.createElement('div');
                userGestureUI.id = 'nertc-lowcode';
                if (userGestureUI && userGestureUI.style) {
                    userGestureUI.style.position = 'fixed';
                    userGestureUI.style.zIndex = '9999';
                    userGestureUI.style.top = '0';
                    userGestureUI.style.width = '100%';
                    userGestureUI.style.margin = 'auto';
                    userGestureUI.style.fontSize = '20px';
                    userGestureUI.style.backgroundColor = 'yellow';
                    userGestureUI.style.cursor = 'pointer';
                    userGestureUI.onclick = () => {
                        if (userGestureUI && userGestureUI.parentNode) {
                            userGestureUI.parentNode.removeChild(userGestureUI);
                        }
                        remoteStream.resume();
                    };
                    userGestureUI.style.display = 'block';
                    userGestureUI.innerHTML = '自动播放受到浏览器限制，需手势触发。<br/>点击此处手动播放';
                    document.body.appendChild(userGestureUI);
                }
            }
        },
        /**
         * 更新 permKey
         * @param {string} newPermKey 新的 permKey
         */
        async updatePermKey(newPermKey) {
            if (!this.client) return;
            if (this.authenticationType !== 'safe-high') return;
            try {
                await this.client.updatePermKey(newPermKey);
            } catch (error) {
                console.error('更新permKey失败: ', error);
            }
        },
        /**
         * 重新进入房间
         */
        async reload() {
            if (this.client) {
                await this.destroyClient();
            }
            await this.load();
        },
    },
};
