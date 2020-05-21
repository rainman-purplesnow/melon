package org.tieland.melon.admin.web;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tieland.melon.admin.common.ClientResult;
import org.tieland.melon.admin.common.ClientResultBuilder;
import org.tieland.melon.admin.common.GlobalCodeEnum;
import org.tieland.melon.admin.service.MelonService;
import org.tieland.melon.admin.vo.GrayVO;
import org.tieland.melon.admin.vo.MelonVO;

import java.util.List;
import java.util.Optional;

/**
 * @author zhouxiang
 * @date 2019/8/28 14:59
 */
@Slf4j
@RestController
@RequestMapping("/gray")
public class GrayController {

    @Autowired
    private MelonService melonService;

    @GetMapping("/{id}")
    public ClientResult<GrayVO> get(@PathVariable("id") Long id){
        if(id == null){
            return new ClientResultBuilder<GrayVO>().error(GlobalCodeEnum.PARAMTER_ERROR).build();
        }

        return new ClientResultBuilder<GrayVO>().success(melonService.getGray(id)).build();
    }

    @PostMapping("/")
    public ClientResult<Boolean> add(@RequestBody GrayVO grayVO) throws Exception{
        log.info(" grayVO:{} ", grayVO);
        if(grayVO == null
                || StringUtils.isBlank(grayVO.getRule())
                || StringUtils.isBlank(grayVO.getConditionClass())
                || grayVO.getJson() == null
                || ArrayUtils.isEmpty(grayVO.getGroups())
                || StringUtils.isBlank(grayVO.getGrayNo())
                || grayVO.getMelonId() == null){
            return new ClientResultBuilder<Boolean>().error(GlobalCodeEnum.PARAMTER_ERROR).build();
        }

        ClientResult<Boolean> checkResult = check(grayVO);
        if(checkResult != null){
            return checkResult;
        }

        melonService.saveGray(grayVO);
        return new ClientResultBuilder<Boolean>().success(Boolean.TRUE).build();
    }

    @PutMapping("/{id}")
    public ClientResult<Boolean> update(@PathVariable("id") Long id, @RequestBody GrayVO grayVO) throws Exception{
        log.info(" id:{} grayVO:{} ", id, grayVO);
        if(grayVO == null
                || StringUtils.isBlank(grayVO.getRule())
                || StringUtils.isBlank(grayVO.getConditionClass())
                || grayVO.getJson() == null
                || ArrayUtils.isEmpty(grayVO.getGroups())
                || StringUtils.isBlank(grayVO.getGrayNo())
                || grayVO.getMelonId() == null
                || id == null){
            return new ClientResultBuilder<Boolean>().error(GlobalCodeEnum.PARAMTER_ERROR).build();
        }

        ClientResult<Boolean> checkResult = check(grayVO);
        if(checkResult != null){
            return checkResult;
        }

        grayVO.setId(id);
        melonService.saveGray(grayVO);
        return new ClientResultBuilder<Boolean>().success(Boolean.TRUE).build();
    }

    private ClientResult<Boolean> check(GrayVO grayVO) throws ClassNotFoundException {
        Optional<MelonVO> melonVO = melonService.findById(grayVO.getMelonId());
        if(!melonVO.isPresent()){
            return new ClientResultBuilder<Boolean>().error(0000002, "melon 不存在").build();
        }

        if(ArrayUtils.isNotEmpty(grayVO.getGroups())){
            List<String> grayGroups = Lists.newArrayList(grayVO.getGroups());
            String[] forbiddenGroups = melonVO.get().getForbiddenGroups();
            String[] primaryGroups = melonVO.get().getPrimaryGroups();
            List<String> existGroups = Lists.newArrayList();
            if(ArrayUtils.isNotEmpty(forbiddenGroups)){
                existGroups.addAll(Lists.newArrayList(forbiddenGroups));
            }

            if(ArrayUtils.isNotEmpty(primaryGroups)){
                existGroups.addAll(Lists.newArrayList(primaryGroups));
            }

            if(CollectionUtils.isNotEmpty(existGroups)){
                grayGroups.retainAll(existGroups);
                if(CollectionUtils.isNotEmpty(grayGroups)){
                    return new ClientResultBuilder<Boolean>().error(0000003, "灰度组、常规组、禁止组存在重叠交叉").build();
                }
            }
        }

        return null;
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ClientResult<Boolean> delete(@PathVariable("id") Long id){
        if(id == null){
            return new ClientResultBuilder<Boolean>().error(GlobalCodeEnum.PARAMTER_ERROR).build();
        }

        melonService.deleteGray(id);
        return new ClientResultBuilder<Boolean>().success(Boolean.TRUE).build();
    }

}
