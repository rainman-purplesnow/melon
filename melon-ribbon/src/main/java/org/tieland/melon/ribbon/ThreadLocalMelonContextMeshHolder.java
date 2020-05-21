package org.tieland.melon.ribbon;

/**
 * @author zhouxiang
 * @date 2020/3/19 10:25
 */
public final class ThreadLocalMelonContextMeshHolder {

    private static final ThreadLocal<MelonContextMesh> MELON_CONTEXT_HOLDER = new ThreadLocal<>();

    private ThreadLocalMelonContextMeshHolder(){
        //
    }

    public static void set(MelonContextMesh mesh){
        MELON_CONTEXT_HOLDER.set(mesh);
    }

    public static MelonContextMesh get(){
        return MELON_CONTEXT_HOLDER.get();
    }

    public static void clear(){
        MELON_CONTEXT_HOLDER.remove();
    }

}
