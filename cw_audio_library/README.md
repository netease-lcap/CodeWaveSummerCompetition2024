# cw_audio_library

**依赖库设计**

这个依赖库旨在提供一套用于低代码开发的工具集，以简化开发人员的工作流程。它包含了多个组件和逻辑，每个组件和逻辑都提供了特定的功能，使开发过程更加高效。

**主要特性**

- **cw-audio-view：** 用于网页上播放音频文件，支持播放、暂停、调整播放速率和音量等功能。
- **cw-audio-record：** 用于网页上录制和管理音频文件，提供录制、暂停、停止、上传和下载等功能。

## cw-audio-view

**支持的平台：** H5 / PC

### attrs

- **src：** 音频流地址。指定要播放的音频文件的地址。
- **showControls：** 是否显示播放器面板。设置为true将显示播放器控制面板，设置为false将隐藏。

### methods

- **play()：** 继续播放音频。
- **pause()：** 暂停播放音频。
- **handleSetPlaybackRate(rate):** 设置播放速率。速率范围在0.5到4之间的十进制数。
- **handleSetPlaybackVolume(volume):** 设置播放音量。音量大小范围在0到1之间的十进制数。

### events

- **start($event):** 当音频开始播放时触发。
- **pause($event):** 当音频暂停播放时触发。
- **play($event):** 当音频继续播放时触发。
- **ended($event):** 当音频播放结束时触发。
- **videoProgress($event):** 当音频播放进度更新时触发。
- **onLoadedMetadata($event):** 当音频资源加载完毕后触发。

## cw-audio-record

**支持的平台：** H5 / PC

### attrs

- **waveBgColor：** 设定声波背景颜色。可传入任何合法的CSS颜色值。
- **waveColor：** 设定波形绘制颜色。可传入任何合法的CSS颜色值。
- **maxFileSize：** 上传文件的最大大小，单位为MB。设置为0表示不限制文件大小。
- **uploadUrl：** 指定上传音频文件的地址。应提供一个有效的URL路径。
- **isShowWave：** 控制是否显示声波。设置为true将显示声波，设置为false将隐藏。

### methods

- **startRecord()：** 开始录制音频。
- **stopRecord()：** 停止录制音频。
- **pauseRecord()：** 暂停录制音频。
- **resumeRecord()：** 恢复录制音频。
- **uploadRecord(type):** 上传录制的音频。支持wav，mp3，pcm格式。
- **downloadRecord(type):** 下载录制的音频。支持wav，mp3，pcm格式。

### events

- **onUploadSuccess(value):** 上传音频成功时触发。
- **onUploadError(value):** 上传失败时触发。

