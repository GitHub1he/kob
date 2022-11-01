package com.he.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread{ //消费bot
    private final ReentrantLock lock = new ReentrantLock();
    private final Queue<Bot> queue = new LinkedList<>();
    private final Condition condition = lock.newCondition();

    public void addBot(Integer userId, String botCode, String input) {
        lock.lock();
        try {
            queue.add(new Bot(userId, botCode, input));
            condition.signalAll(); //唤醒
        } finally {
            lock.unlock();
        }
    }

    private void consume(Bot bot){
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000,bot);
    }

    @Override
    public void run() {
        while (true) { //消息队列
            lock.lock();
            if (queue.isEmpty()){
                try {
                    condition.await(); //将线程睡眠
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            }else {
                Bot bot = queue.remove();
                lock.unlock();
                consume(bot); //可能会比较耗时
            }
        }
    }
}
