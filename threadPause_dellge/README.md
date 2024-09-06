# 暂停当前线程


## 逻辑详情

### threadSleep

暂停线程。

| 参数名         | 类型      | 描述        | 是否必填 |
|-------------|---------|-----------|------|
| millisecond | Integer | 暂停的时间（毫秒） | 是    |

### wait

让线程进入等待状态，直到另一个线程调用该对象的 notify()或 notifyAll()方法为止。

| 参数名 | 类型      | 描述          | 是否必填 |
|-----|---------|-------------|------|
| obj | Boolean | 调用wait方法的对象 | 是    |

### notify

唤醒该对象等待队列中的单个线程。

| 参数名 | 类型      | 描述            | 是否必填 |
|-----|---------|---------------|------|
| obj | Boolean | 调用notify方法的对象 | 是    |

### notifyAll

唤醒该对象等待队列中的所有线程。

| 参数名 | 类型      | 描述               | 是否必填 |
|-----|---------|------------------|------|
| obj | Boolean | 调用notifyAll方法的对象 | 是    |

## 使用步骤说明

1. 下载依赖库后，应用引用依赖库
   ![](https://dev-a310-xdddcb.app.codewave.163.com:443/upload/app/36a05c99-34af-4d43-a680-6ef45a963d30/add_20240905203800718_ori.png)
2. 点击按钮调用ThreadSleep方法，暂停10秒（10000毫秒）
   ![](https://dev-a310-xdddcb.app.codewave.163.com:443/upload/app/36a05c99-34af-4d43-a680-6ef45a963d30/threadSleep_20240905203844878_ori.png)
3. 调用结果：
![](https://dev-a310-xdddcb.app.codewave.163.com:443/upload/app/36a05c99-34af-4d43-a680-6ef45a963d30/threadSleep_20240905204714182_ori.png)
4. 点击按钮调用wait方法,让线程进入等待状态，直到另一个线程调用该对象的 notify() 或 notifyAll() 方法为止。

![](https://dev-a310-xdddcb.app.codewave.163.com:443/upload/app/36a05c99-34af-4d43-a680-6ef45a963d30/wait1_20240906134206212_ori.png)
![](https://dev-a310-xdddcb.app.codewave.163.com:443/upload/app/36a05c99-34af-4d43-a680-6ef45a963d30/wait_20240906133823466_ori.png)

5.调用notify方法,对象的等待队列中等待的单个线程
![](https://dev-a310-xdddcb.app.codewave.163.com:443/upload/app/36a05c99-34af-4d43-a680-6ef45a963d30/notify_20240906140009760_ori.png)
![](https://dev-a310-xdddcb.app.codewave.163.com:443/upload/app/36a05c99-34af-4d43-a680-6ef45a963d30/notify1_20240906140049834_ori.png)

6.调用notifyAll方法,唤醒在对象的等待队列中等待的所有线程
![](https://dev-a310-xdddcb.app.codewave.163.com:443/upload/app/36a05c99-34af-4d43-a680-6ef45a963d30/notifyAll_20240906141026138_ori.png)
![](https://dev-a310-xdddcb.app.codewave.163.com:443/upload/app/36a05c99-34af-4d43-a680-6ef45a963d30/notifyAll1_20240906141208476_ori.png)
## 应用演示链接
https://dev-a310-xdddcb.app.codewave.163.com/threadTest