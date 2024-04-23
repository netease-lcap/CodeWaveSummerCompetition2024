### 基本用法

``` html
<lxy-turning-step-bar>
    <template #default="current">
            <p>{{ current.item.status }}</p>
            <p>{{ current.item.title }}</p>
            <p>{{ current.item.description }}</p>
    </template>
</lxy-turning-step-bar>
```
