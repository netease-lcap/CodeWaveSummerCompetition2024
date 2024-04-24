<template>
    <div
        v-if="$env.VUE_APP_DESIGNER"
        vusion-slot-name="default"
        class="transition-group-container">
        <div
            v-for="item in 4"
            :key="`key_${index}`"
            :designer="$env.VUE_APP_DESIGNER"
            :style="comStyle"
            class="transition-group-item">
            <slot></slot>
            <s-empty v-if="$scopedSlots && !($scopedSlots.default && $scopedSlots.default({}))"></s-empty>
        </div>
    </div>
    <transition-group
        v-else
        tag="div"
        class="transition-group-container"
        :css="false"
        :appear="appear"
        @before-enter="onBeforeEnter"
        @enter="onEnter"
        @after-enter="onAfterEnter"
        @enter-cancelled="onEnterCancelled"
        @before-leave="onBeforeLeave"
        @leave="onLeave"
        @after-leave="onAfterLeave"
        @leave-cancelled="onLeaveCancelled">
        <div
            v-for="(item, index) in options"
            :key="`key_${index}`"
            :data-index="index"
            :style="comStyle">
            <slot
                :item="item"
                :index="index"></slot>
        </div>
    </transition-group>
</template>

<script>
    import gsap from 'gsap';
    import 'animate.css';
    import supportDatasource from '../../utils/support.datasource';

    export default {
        name: 'jxt-transition-group',
        mixins: [supportDatasource],
        props: {
            repeat: {
                type: Number,
                default: 4,
            },
            enterAnimation: {
                type: String,
                default: '',
            },
            leaveAnimation: {
                type: String,
                default: '',
            },
            enterAnimationDuration: {
                type: Number,
                default: 1,
            },
            leaveAnimationDuration: {
                type: Number,
                default: 1,
            },
            appear: {
                type: Boolean,
                default: false,
            },
        },
        emits: ['before-enter', 'enter', 'after-enter', 'enter-cancelled', 'before-leave', 'leave', 'after-leave', 'leave-cancelled'],
        computed: {
            options() {
                if (this.currentDataSource && this.currentDataSource.data) {
                    return this.currentDataSource.data;
                }
                return [];
            },
            comStyle() {
                if (this.repeat) {
                    const num = this.repeat;
                    const width = 100 / num;
                    return {
                        width: width + '%',
                    };
                }
                return {};
            },
        },
        methods: {
            onBeforeEnter(el) {
                el.style.setProperty('--animate-duration', `${this.enterAnimationDuration}s`);
                if (!this.customer) {
                    el.style.opacity = 0;
                }
                const index = el.dataset.index;
                this.$emit('before-enter', { gsap, el, index });
            },
            onEnter(el, done) {
                const index = el.dataset.index;
                if (!this.customer) {
                    el.style.opacity = 1;
                    this.changeClassList(el, this.enterAnimation);
                    this.delayDone(done, this.enterAnimationDuration);
                }
                this.$emit('enter', { gsap, el, done, index });
            },
            onAfterEnter(el) {
                const index = el.dataset.index;
                if (!this.customer) {
                    this.changeClassList(el, this.enterAnimation, false);
                }
                this.$emit('after-enter', { gsap, el, index });
            },
            onEnterCancelled(el) {
                const index = el.dataset.index;
                this.$emit('enter-cancelled', { gsap, el, index });
            },
            onBeforeLeave(el) {
                el.style.setProperty('--animate-duration', `${this.leaveAnimationDuration}s`);
                const index = el.dataset.index;
                this.$emit('before-leave', { gsap, el, index });
            },
            onLeave(el, done) {
                const index = el.dataset.index;
                if (!this.customer) {
                    this.changeClassList(el, this.leaveAnimation);
                    this.delayDone(done, this.leaveAnimationDuration);
                }
                this.$emit('leave', { gsap, el, done, index });
            },
            onAfterLeave(el) {
                const index = el.dataset.index;
                if (!this.customer) {
                    this.changeClassList(el, this.leaveAnimation, false);
                }
                this.$emit('after-leave', { gsap, el, index });
            },
            onLeaveCancelled(el) {
                const index = el.dataset.index;
                this.$emit('leave-cancelled', { gsap, el, index });
            },
            changeClassList(el, animation, isAdd = true) {
                if (isAdd) {
                    el.classList.add('animate__animated');
                    el.classList.add(`animate__${animation}`);
                } else {
                    el.classList.remove('animate__animated');
                    el.classList.remove(`animate__${animation}`);
                }
            },
            delayDone(done, delay) {
                setTimeout(() => {
                    done();
                }, delay * 1000);
            },
        },
    };
</script>

<style scoped>
    .transition-group-container {
        display: flex;
        flex-basis: auto;
        flex-wrap: wrap;
    }
    .transition-group-item[designer] {
        position: relative;
    }
    .transition-group-item[designer] + .transition-group-item[designer]:after {
        content: '';
        position: absolute;
        display: block;
        background: rgba(255, 255, 255, 0.8);
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
    }
</style>
