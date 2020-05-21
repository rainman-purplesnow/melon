package org.tieland.melon.ribbon;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;

/**
 * @author zhouxiang
 * @date 2020/3/19 10:25
 */
public final class HystrixMelonContextMeshHolder {

    private static HystrixRequestVariableDefault<MelonContextMesh> MELON_CONTEXT_MESH_HOLDER = new HystrixRequestVariableDefault<>();

    private HystrixMelonContextMeshHolder(){
        //
    }

    public static void set(MelonContextMesh mesh){
        MELON_CONTEXT_MESH_HOLDER.set(mesh);
    }

    public static MelonContextMesh get(){
        return MELON_CONTEXT_MESH_HOLDER.get();
    }
}
