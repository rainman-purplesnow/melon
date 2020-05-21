package org.tieland.melon.ribbon;

import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.tieland.melon.core.AccessOrigin;
import org.tieland.melon.core.MelonContext;
import org.tieland.melon.core.MelonInstance;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author zhouxiang
 * @date 2019/10/12 11:09
 */
@Slf4j
public abstract class AbstractMelonZoneAvoidanceRule extends ZoneAvoidanceRule {

    public AbstractMelonZoneAvoidanceRule(){
        super();
    }

    /**
     * 获取MelonContextMesh
     * @return
     */
    protected abstract MelonContextMesh getMelonContextMesh();

    @Override
    public Server choose(Object key) {
        List<Server> serverList = this.getPredicate().getEligibleServers(this.getLoadBalancer().getAllServers());
        if(CollectionUtils.isEmpty(serverList)){
            log.warn(" no server exist. ");
            return null;
        }

        //分组server
        Multimap<String, Server> groupServers = groupServers(serverList);
        MelonContextMesh contextMesh = getMelonContextMesh();
        MelonContext context = contextMesh.getContext();
        MelonInstance instance = contextMesh.getInstance();
        log.debug(" melon context:{} ", context);

        //当请求来源不是gateway时，只可选择本组
        if(AccessOrigin.GATEWAY != context.getAccessOrigin()){
            log.debug(" request is not from gateway. ");
            log.debug(" only select instance group:{} servers. ", instance.getGroup());
            List<Server> instanceGroupServers = (List<Server>)groupServers.get(instance.getGroup());
            return selectServer(instanceGroupServers, key);
        }

        //来源gateway分发的请求
        //无white groups和 black groups时，可全局访问
        if(CollectionUtils.isEmpty(context.getWhiteGroups())
                && CollectionUtils.isEmpty(context.getBlackGroups())){
            log.debug(" no white groups and no black groups. select any. ");
            return selectServer(serverList, key);
        }

        //如果有white groups则优先选择
        if(CollectionUtils.isNotEmpty(context.getWhiteGroups())){
            log.debug(" gray is on. select with white groups . ");
            return selectWithWhiteGroups(groupServers, context.getWhiteGroups(), key);
        }

        //如果有black groups，则排除后选择
        if(CollectionUtils.isNotEmpty(context.getBlackGroups())){
            log.debug(" gray is on. select with black groups . ");
            return selectWithBlackGroups(groupServers, context.getBlackGroups(), key);
        }

        log.warn(" no server is selected. ");
        return null;
    }

    /**
     * 按 white groups 选择
     * @param groupServers
     * @param whiteGroups
     * @param key
     * @return
     */
    private Server selectWithWhiteGroups(Multimap<String, Server> groupServers, Set<String> whiteGroups, Object key){
        List<Server> optimalServers = new ArrayList<>();

        whiteGroups.forEach(group->{
            if(CollectionUtils.isNotEmpty(groupServers.get(group))){
                optimalServers.addAll(groupServers.get(group));
            }
        });

        return selectServer(optimalServers, key);
    }

    /**
     * 按排除black group 选择
     * @param groupServers
     * @param blackGroups
     * @param key
     * @return
     */
    private Server selectWithBlackGroups(Multimap<String, Server> groupServers, Set<String> blackGroups, Object key){
        List<Server> optimalServers = new ArrayList<>();

        groupServers.keySet().forEach(group->{
            if(!blackGroups.contains(group)){
                if(CollectionUtils.isNotEmpty(groupServers.get(group))){
                    optimalServers.addAll(groupServers.get(group));
                }
            }
        });

        return selectServer(optimalServers, key);
    }

    /**
     * 选择server
     * @param availableServers
     * @param key
     * @return
     */
    private Server selectServer(List<Server> availableServers, Object key){
        if(CollectionUtils.isEmpty(availableServers)){
            return null;
        }

        Optional<Server> server = this.getPredicate().chooseRoundRobinAfterFiltering(availableServers, key);
        if(server.isPresent()){
            log.debug(" server:{} is selected. ", server.get().getId());
        }

        return server.isPresent() ? server.get() : null;
    }


    /**
     * 根据group组队
     * @param serverList
     * @return
     */
    private Multimap<String, Server> groupServers(List<Server> serverList){
        Multimap<String, Server> serverMultimap = ArrayListMultimap.create();
        for(Server server:serverList){
            DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer)server;
            String groupName = discoveryEnabledServer.getInstanceInfo().getAppGroupName();
            serverMultimap.put(StringUtils.lowerCase(groupName), server);
        }

        return serverMultimap;
    }

}
