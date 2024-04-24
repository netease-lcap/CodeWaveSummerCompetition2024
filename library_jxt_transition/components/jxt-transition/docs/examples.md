### 基本用法

设置进入动画和离开动画。

> 注意：执行动画的元素需要为块级元素。

```vue
<template>
    <div>
        <u-button @click="toggle">{{ show ? '隐藏' : '显示' }}</u-button>
        <jxt-transition
            :enterAnimation="enterAnimation"
            :leaveAnimation="leaveAnimation">
            <span
                style="display: inline-block;"
                v-if="show"
                >{{ show ? enterAnimation : leaveAnimation }}</span
            >
        </jxt-transition>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                show: false,
                enterAnimation: 'backInDown',
                leaveAnimation: 'backOutDown',
            };
        },
        methods: {
            toggle() {
                this.show = !this.show;
            },
        },
    };
</script>
```

### 设置动画执行时间

设置进入动画和离开动画的持续时间。

```vue
<template>
    <div>
        <u-button @click="toggle">{{ show ? '隐藏' : '显示' }}</u-button>
        <jxt-transition
            :enterAnimation="enterAnimation"
            :leaveAnimation="leaveAnimation"
            :enterAnimationDuration="enterAnimationDuration"
            :leaveAnimationDuration="leaveAnimationDuration">
            <span
                style="display: inline-block;"
                v-if="show"
                >{{ show ? enterAnimation : leaveAnimation }}</span
            >
        </jxt-transition>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                show: false,
                enterAnimation: 'backInDown',
                leaveAnimation: 'backOutDown',
                enterAnimationDuration: 2,
                leaveAnimationDuration: 2,
            };
        },
        methods: {
            toggle() {
                this.show = !this.show;
            },
        },
    };
</script>
```

### 设置元素初次展示内容时执行动画效果

用于设置需要过渡的元素在为添加条件判断的情况下，初次展示时是否显示进入动画效果。

```vue
<template>
    <div>
        <jxt-transition
            :appear="appear"
            :enterAnimation="enterAnimation"
            :leaveAnimation="leaveAnimation">
            <span style="display: inline-block;">{{ enterAnimation }}</span>
        </jxt-transition>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                appear: true,
                enterAnimation: 'backInDown',
                leaveAnimation: 'backOutDown',
            };
        },
    };
</script>
```

### 自定义动画

```vue
<template>
    <div>
        <u-button @click="toggle">{{ show ? '隐藏' : '显示' }}</u-button>
        <jxt-transition
            @before-enter="onBeforeEnter"
            @enter="onEnter"
            @leave="onLeave">
            <span
                v-if="show"
                style="display: inline-block;"
                >自定义动画</span
            >
        </jxt-transition>
    </div>
</template>
<script>
    export default {
        data() {
            return {
                show: false,
            };
        },
        methods: {
            toggle() {
                this.show = !this.show;
            },
            onBeforeEnter(event) {
                const el = event.el;
                const gsap = event.gsap;
                gsap.set(el, {
                    scaleX: 0.25,
                    scaleY: 0.25,
                    opacity: 1,
                });
            },
            onEnter(event) {
                const el = event.el;
                const gsap = event.gsap;
                const done = event.done;
                gsap.to(el, {
                    duration: 1,
                    scaleX: 1,
                    scaleY: 1,
                    opacity: 1,
                    ease: 'elastic.inOut(2.5, 1)',
                    onComplete: done,
                });
            },
            onLeave(event) {
                const el = event.el;
                const gsap = event.gsap;
                const done = event.done;
                gsap.to(el, {
                    duration: 0.7,
                    scaleX: 1,
                    scaleY: 1,
                    x: 300,
                    ease: 'elastic.inOut(2.5, 1)',
                });
                gsap.to(el, {
                    duration: 0.2,
                    delay: 0.5,
                    opacity: 0,
                    onComplete: done,
                });
            },
        },
    };
</script>
```
