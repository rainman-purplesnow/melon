package org.tieland.melon.ribbon;

/**
 * @author zhouxiang
 * @date 2020/3/19 10:31
 */
public class DefaultMelonZoneAvoidanceRule extends AbstractMelonZoneAvoidanceRule {

    public DefaultMelonZoneAvoidanceRule(){
        super();
    }

    @Override
    protected MelonContextMesh getMelonContextMesh() {
        return HystrixMelonContextMeshHolder.get();
    }
}
