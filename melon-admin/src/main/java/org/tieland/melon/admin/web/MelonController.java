package org.tieland.melon.admin.web;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tieland.melon.admin.common.ClientResult;
import org.tieland.melon.admin.common.ClientResultBuilder;
import org.tieland.melon.admin.common.GlobalCodeEnum;
import org.tieland.melon.admin.service.MelonService;
import org.tieland.melon.admin.vo.MelonVO;
import java.util.List;

/**
 * @author zhouxiang
 * @date 2019/8/28 14:59
 */
@Slf4j
@RestController
@RequestMapping("/melon")
public class MelonController {

    @Autowired
    private MelonService melonService;

    /**
     * 列表list
     * @return
     */
    @GetMapping("/list")
    public ClientResult<List<MelonVO>> list() throws Exception {
        return new ClientResultBuilder<List<MelonVO>>().success(melonService.findAll()).build();
    }

    /**
     * 单个详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ClientResult<MelonVO> get(@PathVariable("id") Long id) throws Exception{
        if(id == null){
            return new ClientResultBuilder<MelonVO>().error(GlobalCodeEnum.PARAMTER_ERROR).build();
        }
        return new ClientResultBuilder<MelonVO>().success(melonService.findById(id).get()).build();
    }

    /**
     * 新增
     * @param melonVO
     * @return
     */
    @PostMapping("/")
    public ClientResult<Boolean> add(@RequestBody MelonVO melonVO){
        log.info(" melonVO:{} ", melonVO);
        if(melonVO == null
                || melonVO.getMode() == null){
            return new ClientResultBuilder<Boolean>().error(GlobalCodeEnum.PARAMTER_ERROR).build();
        }

        ClientResult<Boolean> checkResult = check(melonVO);
        if(checkResult != null){
            return checkResult;
        }

        melonService.save(melonVO);
        return new ClientResultBuilder<Boolean>().success(Boolean.TRUE).build();
    }

    private ClientResult<Boolean> check(MelonVO melonVO){
        String[] forbiddenGroups = melonVO.getForbiddenGroups();
        String[] primaryGroups = melonVO.getPrimaryGroups();
        List<String> forbiddenGroupList = Lists.newArrayList();
        List<String> primaryGroupList = Lists.newArrayList();
        if(ArrayUtils.isNotEmpty(forbiddenGroups)){
            forbiddenGroupList.addAll(Lists.newArrayList(forbiddenGroups));
        }

        if(ArrayUtils.isNotEmpty(primaryGroups)){
            primaryGroupList.addAll(Lists.newArrayList(forbiddenGroups));
        }

        forbiddenGroupList.retainAll(primaryGroupList);
        if(CollectionUtils.isNotEmpty(forbiddenGroupList)){
            return new ClientResultBuilder<Boolean>().error(0000003, "常规组、禁止组存在重叠交叉").build();
        }

        return null;
    }

    /**
     * 修改
     * @param id
     * @param melonVO
     * @return
     */
    @PutMapping("/{id}")
    public ClientResult<Boolean> update(@PathVariable("id") Long id, @RequestBody MelonVO melonVO){
        log.info(" id:{}, melonVO:{} ", id, melonVO);
        if(melonVO == null
                || melonVO.getMode() == null){
            return new ClientResultBuilder<Boolean>().error(GlobalCodeEnum.PARAMTER_ERROR).build();
        }

        ClientResult<Boolean> checkResult = check(melonVO);
        if(checkResult != null){
            return checkResult;
        }

        melonService.save(melonVO);
        return new ClientResultBuilder<Boolean>().success(Boolean.TRUE).build();
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

        melonService.delete(id);
        return new ClientResultBuilder<Boolean>().success(Boolean.TRUE).build();
    }

}
