package com.wgx.lib.delayqueue.service;

import com.wgx.lib.delayqueue.modal.DelayedTask;
import com.wgx.lib.delayqueue.service.DelayQueueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class DelayQueueServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(DelayQueueServiceTest.class);

    private DelayQueueService delayQueueService;

    @BeforeEach
    public void setUp() {
        delayQueueService = new DelayQueueService();
        // 初始化延迟队列，避免空指针异常
        delayQueueService.delayQueue = new DelayQueue<>();
    }

    @Test
    public void testAddTaskSuccess() {
        DelayedTask task = new DelayedTask();
        task.setTaskId("testTask1");
        task.setDelay(1000L);
        task.setStartTime(System.currentTimeMillis() + 1000);
        task.setStatus("pending");
        task.setParameters("param1");
        task.setResult("");
        task.setTaskName("Task One");

        boolean result = delayQueueService.addTask(task);
        assertTrue(result);
    }

    @Test
    public void testAddTaskNullTask() {
        boolean result = delayQueueService.addTask(null);
        assertFalse(result);
    }

    @Test
    public void testOfferSuccess() {
        DelayedTask task = new DelayedTask();
        task.setTaskId("testTask2");
        task.setDelay(1000L);
        task.setStartTime(System.currentTimeMillis() + 1000);
        task.setStatus("pending");
        task.setParameters("param2");
        task.setResult("");
        task.setTaskName("Task Two");

        boolean result = delayQueueService.offer(task);
        assertTrue(result);
    }

    @Test
    public void testTake() throws InterruptedException {
        DelayedTask task = new DelayedTask();
        task.setTaskId("testTask3");
        task.setDelay(0L);
        task.setStartTime(System.currentTimeMillis());
        task.setStatus("pending");
        task.setParameters("param3");
        task.setResult("");
        task.setTaskName("Task Three");

        delayQueueService.addTask(task);
        DelayedTask takenTask = delayQueueService.take();
        assertNotNull(takenTask);
        assertEquals(task.getTaskId(), takenTask.getTaskId());
    }

    @Test
    public void testPollNow() {
        DelayedTask task = new DelayedTask();
        task.setTaskId("testTask4");
        task.setDelay(0L);
        task.setStartTime(System.currentTimeMillis());
        task.setStatus("pending");
        task.setParameters("param4");
        task.setResult("");
        task.setTaskName("Task Four");

        delayQueueService.addTask(task);
        DelayedTask polledTask = delayQueueService.pollNow();
        assertNotNull(polledTask);
        assertEquals(task.getTaskId(), polledTask.getTaskId());
    }

    @Test
    public void testPollWithTimeout() throws InterruptedException {
        DelayedTask task = new DelayedTask();
        task.setTaskId("testTask5");
        task.setDelay(0L);
        task.setStartTime(System.currentTimeMillis());
        task.setStatus("pending");
        task.setParameters("param5");
        task.setResult("");
        task.setTaskName("Task Five");

        delayQueueService.addTask(task);
        DelayedTask polledTask = delayQueueService.pollWithTimeout(1000L);
        assertNotNull(polledTask);
        assertEquals(task.getTaskId(), polledTask.getTaskId());
    }

    @Test
    public void testPeek() {
        DelayedTask task = new DelayedTask();
        task.setTaskId("testTask6");
        task.setDelay(0L);
        task.setStartTime(System.currentTimeMillis());
        task.setStatus("pending");
        task.setParameters("param6");
        task.setResult("");
        task.setTaskName("Task Six");

        delayQueueService.addTask(task);
        DelayedTask peekedTask = delayQueueService.peek();
        assertNotNull(peekedTask);
        assertEquals(task.getTaskId(), peekedTask.getTaskId());
    }

    @Test
    public void testFindAllTasks() {
        DelayedTask task1 = new DelayedTask();
        task1.setTaskId("testTask7");
        task1.setDelay(1000L);
        task1.setStartTime(System.currentTimeMillis() + 1000);
        task1.setStatus("pending");
        task1.setParameters("param7");
        task1.setResult("");
        task1.setTaskName("Task Seven");

        DelayedTask task2 = new DelayedTask();
        task2.setTaskId("testTask8");
        task2.setDelay(2000L);
        task2.setStartTime(System.currentTimeMillis() + 2000);
        task2.setStatus("pending");
        task2.setParameters("param8");
        task2.setResult("");
        task2.setTaskName("Task Eight");

        delayQueueService.addTask(task1);
        delayQueueService.addTask(task2);
        assertEquals(2, delayQueueService.findAllTasks().size());
    }

    @Test
    public void testSearchTasksByTaskName() {
        DelayedTask task1 = new DelayedTask();
        task1.setTaskId("testTask9");
        task1.setDelay(1000L);
        task1.setStartTime(System.currentTimeMillis() + 1000);
        task1.setStatus("pending");
        task1.setParameters("param9");
        task1.setResult("");
        task1.setTaskName("Task Nine");

        DelayedTask task2 = new DelayedTask();
        task2.setTaskId("testTask10");
        task2.setDelay(2000L);
        task2.setStartTime(System.currentTimeMillis() + 2000);
        task2.setStatus("pending");
        task2.setParameters("param10");
        task2.setResult("");
        task2.setTaskName("Task Ten");

        delayQueueService.addTask(task1);
        delayQueueService.addTask(task2);
        assertEquals(1, delayQueueService.searchTasksByTaskName("Task Nine").size());
    }

    @Test
    public void testGetTaskByTaskId() {
        DelayedTask task1 = new DelayedTask();
        task1.setTaskId("testTask11");
        task1.setDelay(1000L);
        task1.setStartTime(System.currentTimeMillis() + 1000);
        task1.setStatus("pending");
        task1.setParameters("param11");
        task1.setResult("");
        task1.setTaskName("Task Eleven");

        delayQueueService.addTask(task1);
        DelayedTask foundTask = delayQueueService.getTaskByTaskId("testTask11");
        assertNotNull(foundTask);
        assertEquals(task1.getTaskId(), foundTask.getTaskId());
    }

    @Test
    public void testClearAllTasks() {
        DelayedTask task1 = new DelayedTask();
        task1.setTaskId("testTask12");
        task1.setDelay(1000L);
        task1.setStartTime(System.currentTimeMillis() + 1000);
        task1.setStatus("pending");
        task1.setParameters("param12");
        task1.setResult("");
        task1.setTaskName("Task Twelve");

        DelayedTask task2 = new DelayedTask();
        task2.setTaskId("testTask13");
        task2.setDelay(2000L);
        task2.setStartTime(System.currentTimeMillis() + 2000);
        task2.setStatus("pending");
        task2.setParameters("param13");
        task2.setResult("");
        task2.setTaskName("Task Thirteen");

        delayQueueService.addTask(task1);
        delayQueueService.addTask(task2);
        delayQueueService.clearAllTasks();
        assertEquals(0, delayQueueService.size());
    }

    @Test
    public void testRemove() {
        DelayedTask task1 = new DelayedTask();
        task1.setTaskId("testTask14");
        task1.setDelay(1000L);
        task1.setStartTime(System.currentTimeMillis() + 1000);
        task1.setStatus("pending");
        task1.setParameters("param14");
        task1.setResult("");
        task1.setTaskName("Task Fourteen");

        delayQueueService.addTask(task1);
        boolean removed = delayQueueService.remove(task1);
        assertTrue(removed);
    }

    @Test
    public void testSize() {
        DelayedTask task1 = new DelayedTask();
        task1.setTaskId("testTask15");
        task1.setDelay(1000L);
        task1.setStartTime(System.currentTimeMillis() + 1000);
        task1.setStatus("pending");
        task1.setParameters("param15");
        task1.setResult("");
        task1.setTaskName("Task Fifteen");

        delayQueueService.addTask(task1);
        assertEquals(1, delayQueueService.size());
    }

    @Test
    public void testSizeUnexpired() throws InterruptedException {
        DelayedTask task1 = new DelayedTask();
        task1.setTaskId("testTask16");
        task1.setDelay(1000L);
        task1.setStartTime(System.currentTimeMillis() + 1000);
        task1.setStatus("pending");
        task1.setParameters("param16");
        task1.setResult("");
        task1.setTaskName("Task Sixteen");

        delayQueueService.addTask(task1);
        assertEquals(1, delayQueueService.sizeUnexpired());
        Thread.sleep(1000);
        assertEquals(0, delayQueueService.sizeUnexpired());
    }
}