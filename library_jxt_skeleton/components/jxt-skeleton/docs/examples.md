### 基本用法

最简单的占位效果。

```html
<jxt-skeleton></jxt-skeleton>
```

### 复杂的组合

更复杂的组合。

```html
<jxt-skeleton
    :avatar="true"
    :paragraph="true"
    :paragraphRows="4"></jxt-skeleton>
```

### 动画效果

显示动画效果。

```html
<jxt-skeleton :active="true"></jxt-skeleton>
```

### 综合效果展示

```vue
<template>
    <div>
        <u-grid-layout>
            <u-grid-layout-row :repeat="6">
                <u-grid-layout-column :span="6">
                    <jxt-skeleton
                        :active="active"
                        :avatar="avatar"
                        :avatarSize="avatarSize"
                        :avatarShape="avatarShape"
                        :title="title"
                        :titleWidth="titleWidth"
                        :paragraph="paragraph"
                        :paragraphRows="paragraphRows"
                        :paragraphWidth="paragraphWidth"></jxt-skeleton>
                </u-grid-layout-column>
            </u-grid-layout-row>
        </u-grid-layout>
        <u-grid-layout>
            <u-grid-layout-row :repeat="12">
                <u-grid-layout-column :span="3">
                    <u-checkbox
                        v-model="active"
                        text="是否展示动画效果" />
                </u-grid-layout-column>
                <u-grid-layout-column :span="3">
                    <u-checkbox
                        v-model="avatar"
                        text="是否显示头像占位图" />
                </u-grid-layout-column>
                <u-grid-layout-column :span="3">
                    <u-checkbox
                        v-model="title"
                        text="是否显示标题占位图" />
                </u-grid-layout-column>
                <u-grid-layout-column :span="3">
                    <u-checkbox
                        v-model="paragraph"
                        text="是否显示段落占位图" />
                </u-grid-layout-column>
            </u-grid-layout-row>
            <u-grid-layout-row
                v-if="avatar"
                :repeat="24">
                <u-grid-layout-column :span="24">
                    <u-text size="large">头像占位图配置</u-text>
                </u-grid-layout-column>
            </u-grid-layout-row>
            <u-grid-layout-row
                v-if="avatar"
                :repeat="24"
                type="flex"
                alignment="center">
                <u-grid-layout-column :span="4">
                    <u-text>头像的大小</u-text>
                </u-grid-layout-column>
                <u-grid-layout-column :span="8">
                    <u-select
                        placeholder="设置头像的大小"
                        v-model="avatarSize">
                        <u-select-item value="small">小</u-select-item>
                        <u-select-item value="default">正常</u-select-item>
                        <u-select-item value="large">大</u-select-item>
                    </u-select>
                </u-grid-layout-column>
                <u-grid-layout-column :span="4">
                    <u-text>头像的形状</u-text>
                </u-grid-layout-column>
                <u-grid-layout-column :span="8">
                    <u-select
                        placeholder="设置头像的形状"
                        v-model="avatarShape">
                        <u-select-item value="circle">圆形</u-select-item>
                        <u-select-item value="square">方形</u-select-item>
                    </u-select>
                </u-grid-layout-column>
            </u-grid-layout-row>
            <u-grid-layout-row
                v-if="title"
                :repeat="24">
                <u-grid-layout-column :span="24">
                    <u-text size="large">标题占位图配置</u-text>
                </u-grid-layout-column>
            </u-grid-layout-row>
            <u-grid-layout-row
                v-if="title"
                :repeat="24"
                type="flex"
                alignment="center">
                <u-grid-layout-column :span="4">
                    <u-text>标题占位图的宽度</u-text>
                </u-grid-layout-column>
                <u-grid-layout-column :span="8">
                    <u-input
                        v-model="titleWidth"
                        placeholder="设置标题占位图的宽度"></u-input>
                </u-grid-layout-column>
            </u-grid-layout-row>
            <u-grid-layout-row
                v-if="paragraph"
                :repeat="24">
                <u-grid-layout-column :span="24">
                    <u-text size="large">段落占位图配置</u-text>
                </u-grid-layout-column>
            </u-grid-layout-row>
            <u-grid-layout-row
                v-if="title"
                :repeat="24"
                type="flex"
                alignment="center">
                <u-grid-layout-column :span="4">
                    <u-text>段落占位图的行数</u-text>
                </u-grid-layout-column>
                <u-grid-layout-column :span="8">
                    <u-number-input
                        v-model="paragraphRows"
                        placeholder="设置段落占位图的行数"></u-number-input>
                </u-grid-layout-column>
                <u-grid-layout-column :span="4">
                    <u-text>最后一行的宽度</u-text>
                </u-grid-layout-column>
                <u-grid-layout-column :span="8">
                    <u-input
                        v-model="paragraphWidth"
                        placeholder="设置最后一行的宽度"></u-input>
                </u-grid-layout-column>
            </u-grid-layout-row>
        </u-grid-layout>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                active: false,
                avatar: false,
                avatarSize: 'default',
                avatarShape: 'circle',
                title: true,
                titleWidth: '',
                paragraph: true,
                paragraphRows: 2,
                paragraphWidth: '',
            };
        },
    };
</script>
```

```vue
<template>
    <div>
        <u-block>
            <u-switch v-model="loading"></u-switch>
            <u-text>显示内容</u-text>
        </u-block>

        <jxt-skeleton
            :loading="!loading"
            :active="true"
            :paragraphRows="3">
            <u-grid-layout>
                <u-grid-layout-row :repeat="1">
                    <u-grid-layout-column :span="1">
                        <u-text size="large">标题</u-text>
                    </u-grid-layout-column>
                </u-grid-layout-row>
                <u-grid-layout-row :repeat="1">
                    <u-grid-layout-column :span="1">
                        <u-text size="large">Lorem ipsum dolor sit amet consectetur, adipisicing elit. Nihil culpa inventore quaerat ut sed veritatis quidem harum obcaecati esse molestias laudantium quas adipisci eos doloribus earum, voluptas laborum, repudiandae at.</u-text>
                    </u-grid-layout-column>
                </u-grid-layout-row>
            </u-grid-layout>
        </jxt-skeleton>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                loading: false,
            };
        },
    };
</script>
```
