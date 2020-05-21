package org.tieland.melon.admin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tieland.melon.admin.common.ClientResult;
import org.tieland.melon.admin.common.ClientResultBuilder;
import org.tieland.melon.admin.common.GlobalCodeEnum;
import org.tieland.melon.admin.service.CacheService;
import org.tieland.melon.admin.service.MelonService;
import org.tieland.melon.admin.vo.MelonVO;

import java.util.Optional;

/**
 * @author zhouxiang
 * @date 2019/8/29 14:41
 */
@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private MelonService melonService;

    @Autowired
    private CacheService cacheService;

    /**
     * 刷新
     * @param melonId
     * @return
     */
    @GetMapping("/refresh/{id}")
    public ClientResult<Boolean> refresh(@PathVariable("id") Long melonId) throws Exception{
        if(melonId == null){
            new ClientResultBuilder<MelonVO>().error(GlobalCodeEnum.PARAMTER_ERROR).build();
        }

        Optional<MelonVO> melonVO = melonService.findById(melonId);
        if(!melonVO.isPresent()){
            return new ClientResultBuilder<Boolean>().success(Boolean.FALSE).build();
        }

        cacheService.refresh(melonVO.get());
        return new ClientResultBuilder<Boolean>().success(Boolean.TRUE).build();
    }

    /**
     * 失效清除
     * @param melonId
     * @return
     */
    @DeleteMapping("/{id}")
    public ClientResult<Boolean> delete(@PathVariable("id") Long melonId){
        if(melonId == null){
            new ClientResultBuilder<MelonVO>().error(GlobalCodeEnum.PARAMTER_ERROR).build();
        }

        cacheService.evict(melonId);
        return new ClientResultBuilder<Boolean>().success(Boolean.TRUE).build();
    }

}
