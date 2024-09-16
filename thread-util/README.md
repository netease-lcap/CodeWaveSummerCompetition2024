# Thread - Util 暂停当前线程依赖库

## 概述
`Thread - Util` 是一个基于 Java 来处理线程的暂停、等待和唤醒操作。

### 主要方法：

1. `threadSleep(Integer millisecond)`：
  - 作用：使当前线程暂停执行指定的毫秒数。
  - 参数：`millisecond`，表示要暂停的时间（以毫秒为单位）。
  - 返回值：如果线程成功暂停，返回`true`；如果暂停失败（例如，由于中断），则记录错误日志并返回`false`。
  - 异常处理：如果线程在睡眠期间被中断，会捕获`InterruptedException`异常，记录错误日志，并重新设置中断状态。

2. `waitThread(String lock)`：
  - 作用：使当前线程在指定的锁对象上等待，直到被唤醒或线程被中断。
  - 参数：`lock`，一个字符串对象，用作同步锁。
  - 返回值：如果线程成功被唤醒，返回`true`；如果等待过程中发生中断，记录错误日志并返回`false`。
  - 注意：使用`String.intern()`确保不同的字符串实例指向同一个锁对象。

3. `waitThreadWithTimeout(String lock, Long timeout)`：
  - 作用：与`waitThread`类似，但带有超时机制，避免了可能的死锁。
  - 参数：`lock`，用作同步锁的字符串对象；`timeout`，超时时间（以毫秒为单位）。
  - 返回值：如果线程在超时前被唤醒，返回`true`；如果超时或发生中断，记录相应的日志并返回`false`。

4. `notifyThread(String lock)`：
  - 作用：唤醒在指定锁对象上等待的第一个线程。
  - 参数：`lock`，用作同步锁的字符串对象。
  - 返回值：如果成功唤醒线程，返回`true`；如果监控状态不合法（即当前线程不持有锁），记录错误日志并返回`false`。

5. `notifyAllThreads(String lock)`：
  - 作用：唤醒在指定锁对象上等待的所有线程。
  - 参数：`lock`，用作同步锁的字符串对象。
  - 返回值：如果成功唤醒所有线程，返回`true`；如果监控状态不合法，记录错误日志并返回`false`。

