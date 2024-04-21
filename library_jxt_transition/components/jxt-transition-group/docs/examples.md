### 基本用法

设置进入动画和离开动画。

```vue
<template>
    <u-block>
        <u-linear-layout style="padding: 10px;">
            <u-button @click="toggle">{{ text }}</u-button>
        </u-linear-layout>

        <jxt-transition-group
            :data-source="dataSource"
            :enterAnimation="enterAnimation"
            :leaveAnimation="leaveAnimation">
            <template #default="scope">
                <u-linear-layout style="padding: 10px;">
                    <u-label color="primary">{{ scope.index }}</u-label>
                </u-linear-layout>
            </template>
        </jxt-transition-group>
    </u-block>
</template>
<script>
    export default {
        data() {
            return {
                dataSource: [],
                enterAnimation: 'backInDown',
                leaveAnimation: 'backOutDown',
            };
        },
        computed: {
            text() {
                if (this.dataSource.length > 0) {
                    return '隐藏';
                }
                return '显示';
            },
        },
        methods: {
            toggle() {
                if (this.dataSource.length > 0) {
                    this.dataSource = [];
                } else {
                    this.dataSource = [1, 2, 3, 4, 5];
                }
            },
        },
    };
</script>
```

### 设置动画执行时间

设置进入动画和离开动画的持续时间。

```vue
<template>
    <u-block>
        <u-linear-layout style="padding: 10px;">
            <u-button @click="toggle">{{ text }}</u-button>
        </u-linear-layout>

        <jxt-transition-group
            :data-source="dataSource"
            :enterAnimation="enterAnimation"
            :leaveAnimation="leaveAnimation"
            :enterAnimationDuration="enterAnimationDuration"
            :leaveAnimationDuration="leaveAnimationDuration">
            <template #default="scope">
                <u-linear-layout style="padding: 10px;">
                    <u-label color="primary">{{ scope.index }}</u-label>
                </u-linear-layout>
            </template>
        </jxt-transition-group>
    </u-block>
</template>
<script>
    export default {
        data() {
            return {
                dataSource: [],
                enterAnimation: 'backInDown',
                leaveAnimation: 'backOutDown',
                enterAnimationDuration: 2,
                leaveAnimationDuration: 2,
            };
        },
        computed: {
            text() {
                if (this.dataSource.length > 0) {
                    return '隐藏';
                }
                return '显示';
            },
        },
        methods: {
            toggle() {
                if (this.dataSource.length > 0) {
                    this.dataSource = [];
                } else {
                    this.dataSource = [1, 2, 3, 4, 5];
                }
            },
        },
    };
</script>
```

### 设置元素初次展示内容时执行动画效果

```vue
<template>
    <u-block>
        <u-linear-layout style="padding: 10px;">
            <u-button @click="toggle">{{ text }}</u-button>
        </u-linear-layout>

        <jxt-transition-group
            :appear="appear"
            :data-source="dataSource"
            :enterAnimation="enterAnimation"
            :leaveAnimation="leaveAnimation">
            <template #default="scope">
                <u-linear-layout style="padding: 10px;">
                    <u-label color="primary">{{ scope.index }}</u-label>
                </u-linear-layout>
            </template>
        </jxt-transition-group>
    </u-block>
</template>
<script>
    export default {
        data() {
            return {
                dataSource: [1, 2, 3, 4, 5],
                appear: true,
                enterAnimation: 'backInDown',
                leaveAnimation: 'backOutDown',
            };
        },
        computed: {
            text() {
                if (this.dataSource.length > 0) {
                    return '隐藏';
                }
                return '显示';
            },
        },
        methods: {
            toggle() {
                if (this.dataSource.length > 0) {
                    this.dataSource = [];
                } else {
                    this.dataSource = [1, 2, 3, 4, 5];
                }
            },
        },
    };
</script>
```

### 设置展示列数

```vue
<template>
    <u-block>
        <u-linear-layout style="padding: 10px;">
            <u-button @click="toggle">{{ text }}</u-button>
        </u-linear-layout>

        <jxt-transition-group
            :repeat="repeat"
            :data-source="dataSource"
            :enterAnimation="enterAnimation"
            :leaveAnimation="leaveAnimation">
            <template #default="scope">
                <u-linear-layout style="padding: 10px;">
                    <u-label color="primary">{{ scope.index }}</u-label>
                </u-linear-layout>
            </template>
        </jxt-transition-group>
    </u-block>
</template>
<script>
    export default {
        data() {
            return {
                dataSource: [],
                repeat: 2,
                enterAnimation: 'backInDown',
                leaveAnimation: 'backOutDown',
            };
        },
        computed: {
            text() {
                if (this.dataSource.length > 0) {
                    return '隐藏';
                }
                return '显示';
            },
        },
        methods: {
            toggle() {
                if (this.dataSource.length > 0) {
                    this.dataSource = [];
                } else {
                    this.dataSource = [1, 2, 3, 4, 5];
                }
            },
        },
    };
</script>
```

### 列表项增删

```vue
<template>
    <u-block>
        <u-linear-layout style="padding: 10px;">
            <u-button @click="clear">清除数据</u-button>
            <u-button @click="add">新增一条数据</u-button>
            <u-button @click="remove">清除一条数据</u-button>
        </u-linear-layout>

        <jxt-transition-group
            :data-source="dataSource"
            :enterAnimation="enterAnimation"
            :leaveAnimation="leaveAnimation">
            <template #default="scope">
                <u-linear-layout style="padding: 10px;">
                    <u-label color="primary">{{ scope.index }}</u-label>
                </u-linear-layout>
            </template>
        </jxt-transition-group>
    </u-block>
</template>
<script>
    export default {
        data() {
            return {
                dataSource: [],
                enterAnimation: 'backInDown',
                leaveAnimation: 'backOutDown',
            };
        },
        methods: {
            clear() {
                this.dataSource = [];
            },
            add() {
                this.dataSource.push('Hello');
            },
            remove() {
                this.dataSource.shift();
            },
        },
    };
</script>
```

### 自定义动画

通过自定义动画实现渐进延迟列表动画。

```vue
<template>
    <u-block>
        <u-linear-layout style="padding: 10px;">
            <u-input v-model="query"></u-input>
        </u-linear-layout>

        <jxt-transition-group
            :data-source="computedList"
            :repeat="1"
            @before-enter="onBeforeEnter"
            @enter="onEnter"
            @leave="onLeave">
            <template #default="scope">
                <u-block style="padding: 10px">
                    <u-label color="primary">{{ scope.item.msg }}</u-label>
                </u-block>
            </template>
        </jxt-transition-group>
    </u-block>
</template>
<script>
    const list = [{ msg: 'Bruce Lee' }, { msg: 'Jackie Chan' }, { msg: 'Chuck Norris' }, { msg: 'Jet Li' }, { msg: 'Kung Fury' }];
    export default {
        data() {
            return {
                enterAnimation: 'backInDown',
                leaveAnimation: 'backOutDown',
                query: '',
            };
        },
        computed: {
            text() {
                if (this.dataSource.length > 0) {
                    return '隐藏';
                }
                return '显示';
            },
            computedList() {
                return list.filter((item) => item.msg.toLowerCase().includes(this.query));
            },
        },
        methods: {
            onBeforeEnter(event) {
                const el = event.el;
                el.style.opacity = 0;
                el.style.height = 0;
            },
            onEnter(event) {
                const el = event.el;
                const gsap = event.gsap;
                const done = event.done;
                const index = event.index;
                gsap.to(el, {
                    opacity: 1,
                    height: 'auto',
                    delay: index * 0.15,
                    onComplete: done,
                });
            },
            onLeave(event) {
                const el = event.el;
                const gsap = event.gsap;
                const done = event.done;
                const index = event.index;
                gsap.to(el, {
                    opacity: 0,
                    height: 0,
                    delay: index * 0.15,
                    onComplete: done,
                });
            },
        },
    };
</script>
```
