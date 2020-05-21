package org.tieland.melon.web.filter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.tieland.melon.core.AccessOrigin;
import org.tieland.melon.core.MelonContext;
import org.tieland.melon.core.MelonException;
import java.util.HashSet;

/**
 * @author zhouxiang
 * @date 2019/8/27 11:23
 */
public final class MelonContextFactory {

    private MelonContextFactory(){
        //
    }

    /**
     * 创建本地MelonContext
     * @return
     */
    public static MelonContext buildWithLocal(){
        MelonContext.Builder builder = new MelonContext.Builder();
        MelonContext melonContext = builder.accessOrigin(AccessOrigin.NON_GATEWAY).build();
        return melonContext;
    }

    /**
     * 从header中还原MelonContext
     * @param melonContextHeader
     * @return
     */
    public static MelonContext buildWithHeader(String melonContextHeader){
        MelonContext.Builder builder = new MelonContext.Builder();
        JSONObject jsonObject = JSONObject.parseObject(melonContextHeader);
        String origin =jsonObject.getString("accessOrigin");
        Boolean grayActivated =jsonObject.getBoolean("grayActivated");
        JSONArray whiteGroupsJSONArray = jsonObject.getJSONArray("whiteGroups");
        JSONArray blackGroupsJSONArray = jsonObject.getJSONArray("blackGroups");

        if(StringUtils.isBlank(origin)
                ||AccessOrigin.valueOf(origin) == null){
            throw new MelonException(" access origin illegal");
        }

        if(whiteGroupsJSONArray != null){
            builder.whiteGroups(Sets.newHashSet(whiteGroupsJSONArray.toJavaList(String.class)));
        }

        if(blackGroupsJSONArray != null){
            builder.blackGroups(Sets.newHashSet(blackGroupsJSONArray.toJavaList(String.class)));
        }


        return builder.accessOrigin(AccessOrigin.valueOf(origin))
                .grayActivated(grayActivated).build();
    }

}
