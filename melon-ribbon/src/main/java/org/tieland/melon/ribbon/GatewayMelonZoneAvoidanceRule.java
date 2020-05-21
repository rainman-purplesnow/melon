package org.tieland.melon.ribbon;

/**
 * @author zhouxiang
 * @date 2020/3/19 10:33
 */
public class GatewayMelonZoneAvoidanceRule extends AbstractMelonZoneAvoidanceRule {

    public GatewayMelonZoneAvoidanceRule(){
        super();
    }

    @Override
    protected MelonContextMesh getMelonContextMesh() {
        return ThreadLocalMelonContextMeshHolder.get();
    }
}
