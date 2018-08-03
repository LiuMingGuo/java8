package com.qlk.jdk8;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class CountNum extends RecursiveTask<Integer> {

    private static final int THREAD_HOLD = 2;

    private int start;
    private int end;

    public CountNum(int start, int end){
        this.start = start;
        this.end = end;

    }

    @Override
    protected Integer compute() {
        int sum = 0;
        //如果任务足够小就计算
        boolean canCompute = (end - start) <= THREAD_HOLD;
        if(canCompute){
            sum= sum(start,end);
        }else{
            int middle = (start + end) / 2;
            CountNum left = new CountNum(start,middle);
            CountNum right = new CountNum(middle+1,end);
            //执行子任务
            left.fork();
            right.fork();
            //获取子任务结果
            int lResult = left.join();
            int rResult = right.join();
            sum = lResult + rResult;
        }
        return sum;
    }

    public static void main(String[] args){
        ForkJoinPool pool = new ForkJoinPool();
        int n=100;
        CountNum task = new CountNum(1,n);
        Long start=System.currentTimeMillis();
        Integer result = pool.invoke(task);
        System.out.println("forkJoin结果："+result+"时间："+(System.currentTimeMillis()-start));

        Long Forstart=System.currentTimeMillis();
        int forSum=sum(1,n);
        System.out.println("for结果："+forSum+"时间："+(System.currentTimeMillis()-start));


    }
    public static int sum(int start,int end){
        int total=0;
        for (int i=start;i<=end;i++){
            total+=i;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return total;
    }
}