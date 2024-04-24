<template>
    <div vusion-slot-name="default">
        <template v-if="$env.VUE_APP_DESIGNER">
            <slot></slot>
            <s-empty v-if="!$slots.default"> </s-empty>
        </template>
        <transition
            v-else
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
            <slot></slot>
        </transition>
    </div>
</template>

<script>
    import gsap from 'gsap';
    import 'animate.css';

    export default {
        name: 'jxt-transition',
        props: {
            customer: {
                type: Boolean,
                default: false,
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
        methods: {
            onBeforeEnter(el) {
                el.style.setProperty('--animate-duration', `${this.enterAnimationDuration}s`);
                this.$emit('before-enter', { gsap, el });
            },
            onEnter(el, done) {
                if (!this.customer) {
                    this.changeClassList(el, this.enterAnimation);
                    this.delayDone(done, this.enterAnimationDuration);
                }
                this.$emit('enter', { gsap, el, done });
            },
            onAfterEnter(el) {
                if (!this.customer) {
                    this.changeClassList(el, this.enterAnimation, false);
                }
                this.$emit('after-enter', { gsap, el });
            },
            onEnterCancelled(el) {
                this.$emit('enter-cancelled', { gsap, el });
            },
            onBeforeLeave(el) {
                el.style.setProperty('--animate-duration', `${this.leaveAnimationDuration}s`);
                this.$emit('before-leave', { gsap, el });
            },
            onLeave(el, done) {
                if (!this.customer) {
                    this.changeClassList(el, this.leaveAnimation);
                    this.delayDone(done, this.leaveAnimationDuration);
                }
                this.$emit('leave', { gsap, el, done });
            },
            onAfterLeave(el) {
                if (!this.customer) {
                    this.changeClassList(el, this.leaveAnimation, false);
                }
                this.$emit('after-leave', { gsap, el });
            },
            onLeaveCancelled(el) {
                this.$emit('leave-cancelled', { gsap, el });
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

<style></style>
