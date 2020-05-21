package org.tieland.melon.core;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhouxiang
 * @date 2020/3/17 11:05
 */
public final class MelonInstanceFactory {

    private static MelonInstance instance;

    private static Lock lock = new ReentrantLock();

    private MelonInstanceFactory(){
        //
    }

    public static MelonInstance get(MelonConfig config, Boolean isGateway){
        if(instance != null){
            return instance;
        }

        try{
            lock.lock();
            instance = new MelonInstance(config.getGroup(), isGateway);
            return instance;
        }finally {
            lock.unlock();
        }
    }

}
