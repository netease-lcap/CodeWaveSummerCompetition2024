<template>
    <div vusion-slot-name="default">
        <template v-if="$env.VUE_APP_DESIGNER">
            <a-skeleton
                :loading="loading"
                :active="active"
                :avatar="avatarCom"
                :title="titleCom"
                :paragraph="paragraphCom">
            </a-skeleton>
            <a-skeleton :loading="false">
                <slot></slot>
                <s-empty v-if="!$slots.default"> </s-empty>
            </a-skeleton>
        </template>
        <a-skeleton
            v-else
            :loading="loading"
            :active="active"
            :avatar="avatarCom"
            :title="titleCom"
            :paragraph="paragraphCom">
            <slot></slot>
        </a-skeleton>
    </div>
</template>

<script>
    import ASkeleton from 'ant-design-vue/lib/skeleton';
    import 'ant-design-vue/lib/skeleton/style/css';

    export default {
        name: 'jxt-skeleton',
        components: { ASkeleton },
        props: {
            active: {
                type: Boolean,
                default: false,
            },
            loading: {
                type: Boolean,
                default: true,
            },
            avatar: {
                type: Boolean,
                default: false,
            },
            avatarSize: {
                type: String,
                default: 'default',
            },
            avatarShape: {
                type: String,
                default: 'circle',
            },
            title: {
                type: Boolean,
                default: true,
            },
            titleWidth: {
                type: String,
            },
            paragraph: {
                type: Boolean,
                default: true,
            },
            paragraphRows: {
                type: Number,
                default: 3,
            },
            paragraphWidth: {
                type: String,
            },
        },
        computed: {
            avatarCom() {
                if (this.avatar) {
                    const avatarProps = {};
                    if (this.avatarSize) {
                        avatarProps.size = this.avatarSize;
                    }
                    if (this.avatarShape) {
                        avatarProps.shape = this.avatarShape;
                    }
                    if (Object.keys(avatarProps).length === 0) {
                        return true;
                    }
                    return avatarProps;
                }
                return false;
            },
            titleCom() {
                if (this.title) {
                    const titleProps = {};
                    if (this.titleWidth) {
                        titleProps.width = this.titleWidth;
                    }
                    if (Object.keys(titleProps).length === 0) {
                        return true;
                    }
                    return titleProps;
                }
                return false;
            },
            paragraphCom() {
                if (this.paragraph) {
                    const paragraphProps = {};
                    if (this.paragraphRows || this.paragraphRows === 0) {
                        paragraphProps.rows = this.paragraphRows;
                    }
                    if (this.paragraphWidth) {
                        paragraphProps.width = this.paragraphWidth;
                    }
                    if (Object.keys(paragraphProps).length === 0) {
                        return true;
                    }
                    return paragraphProps;
                }
                return false;
            },
        },
    };
</script>

<style></style>
