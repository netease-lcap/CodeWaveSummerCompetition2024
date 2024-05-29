### 基本用法

```vue
<template>
    <u-grid-layout>
        <u-grid-layout-row
            :repeat="1"
            type="flex"
            justify="center">
            <u-grid-layout-column :span="1">
                <jxt-nertc
                    :appKey="appKey"
                    channelName="1234"
                    :autoUid="autoUid"
                    :uid="uid"
                    :authenticationType="authenticationType"
                    :token="token"
                    :permKey="permKey"
                    :video="video"
                    :audio="audio"></jxt-nertc>
            </u-grid-layout-column>
        </u-grid-layout-row>
        <u-grid-layout-row
            :repeat="2"
            type="flex"
            alignment="center">
            <u-grid-layout-column :span="1">
                <u-text>AppKey: </u-text>
                <u-input
                    placeholder="请输入AppKey"
                    v-model="appKey"></u-input>
            </u-grid-layout-column>
            <u-grid-layout-column :span="1">
                <u-text>ChannelName: </u-text>
                <u-input
                    placeholder="请输入ChannelName"
                    v-model="channelName"></u-input>
            </u-grid-layout-column>
        </u-grid-layout-row>
        <u-grid-layout-row
            :repeat="2"
            type="flex"
            alignment="center">
            <u-grid-layout-column :span="1">
                <u-text>是否自动生成uid </u-text>
                <u-switch v-model="autoUid"></u-switch>
            </u-grid-layout-column>
            <u-grid-layout-column
                v-if="!autoUid"
                :span="1">
                <u-text>uid: </u-text>
                <u-input
                    placeholder="请输入uid"
                    v-model="uid"></u-input>
            </u-grid-layout-column>
        </u-grid-layout-row>
        <u-grid-layout-row
            :repeat="2"
            type="flex"
            alignment="center">
            <u-grid-layout-column :span="1">
                <u-text>鉴权类型: </u-text>
                <u-select v-model="authenticationType">
                    <u-select-item value="debug">调试模式（无需 token 鉴权）</u-select-item>
                    <u-select-item value="safe">安全模式（基础 token 鉴权）</u-select-item>
                    <u-select-item value="safe-high">安全模式（高级 token 鉴权）</u-select-item>
                </u-select>
            </u-grid-layout-column>
        </u-grid-layout-row>
        <u-grid-layout-row
            :repeat="2"
            type="flex"
            alignment="center">
            <u-grid-layout-column
                v-if="authenticationType === 'safe' || authenticationType === 'safe-high'"
                :span="1">
                <u-text>token: </u-text>
                <u-input
                    placeholder="请输入token"
                    v-model="token"></u-input>
            </u-grid-layout-column>
            <u-grid-layout-column
                v-if="authenticationType === 'safe-high'"
                :span="1">
                <u-text>permKey: </u-text>
                <u-input
                    placeholder="请输入permKey"
                    v-model="permKey"></u-input>
            </u-grid-layout-column>
        </u-grid-layout-row>
    </u-grid-layout>
</template>
<script>
    export default {
        data() {
            return {
                appKey: '62730ba6fba064ff26794e8da02b2f78',
                authenticationType: 'debug',
                token: '',
                permKey: '',
                channelName: '1234',
                autoUid: true,
                uid: '',
                audio: false,
                video: false,
            };
        },
    };
</script>
```
