package org.tieland.melon.ribbon;

import org.tieland.melon.core.MelonContext;
import org.tieland.melon.core.MelonInstance;

/**
 * @author zhouxiang
 * @date 2020/3/17 12:58
 */
public final class MelonContextMesh {

    private MelonContext context;

    private MelonInstance instance;

    public MelonContextMesh(MelonContext context, MelonInstance instance){
        this.context = context;
        this.instance = instance;
    }

    public MelonContext getContext() {
        return context;
    }

    public MelonInstance getInstance() {
        return instance;
    }

}
