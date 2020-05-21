package org.tieland.melon.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.tieland.melon.admin.domain.GrayDO;
import org.tieland.melon.admin.domain.MelonDO;
import org.tieland.melon.admin.repository.GrayRepository;
import org.tieland.melon.admin.repository.MelonRepository;
import org.tieland.melon.admin.vo.GrayVO;
import org.tieland.melon.admin.vo.MelonVO;
import org.tieland.melon.core.MelonMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author zhouxiang
 * @date 2019/8/28 15:28
 */
@Component
@Slf4j
public class MelonServiceImpl implements MelonService {

    @Autowired
    private MelonRepository melonRepository;

    @Autowired
    private GrayRepository grayRepository;

    @Override
    public List<MelonVO> findAll() throws ClassNotFoundException {
        List<MelonDO> doList = melonRepository.findAll();
        if(CollectionUtils.isNotEmpty(doList)){
            List<MelonVO> list = new ArrayList<>();
            for(MelonDO melonDO:doList){
                List<GrayDO> grayDOList = grayRepository.findbyMelonId(melonDO.getId());
                if(CollectionUtils.isNotEmpty(grayDOList)){
                    MelonVO melonVO = convert(melonDO, grayDOList);
                    list.add(melonVO);
                }
            }

            return list;
        }

        return null;
    }

    @Override
    public Optional<MelonVO> findById(Long id) throws ClassNotFoundException {
        MelonVO melonVO = null;
        Optional<MelonDO> melonDO = melonRepository.findById(id);
        if(melonDO.isPresent()){
            List<GrayDO> grayDOList = grayRepository.findbyMelonId(melonDO.get().getId());
            if(CollectionUtils.isNotEmpty(grayDOList)){
                melonVO = convert(melonDO.get(), grayDOList);
            }else {
                melonVO = convert(melonDO.get());
            }
        }

        return Optional.ofNullable(melonVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MelonVO melonVO) {
        if(melonVO.getId() == null){
            MelonDO melonDO = convet(melonVO);
            melonRepository.save(melonDO);
            return;
        }

        MelonDO melonDO = convet(melonVO);
        melonRepository.save(melonDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        melonRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGray(GrayVO grayVO) {
        if(grayVO.getId() == null){
            GrayDO grayDO = convert(grayVO);
            grayRepository.save(grayDO);
            return;
        }

        GrayDO grayDO = convert(grayVO);
        grayRepository.save(grayDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGray(Long id) {
        grayRepository.deleteById(id);
    }

    @Override
    public GrayVO getGray(Long id) {
        Optional<GrayDO> grayDO = grayRepository.findById(id);
        if(grayDO.isPresent()){
            try {
                return convert(grayDO.get());
            } catch (ClassNotFoundException e) {
                log.error("",e);
            }
        }

        return null;
    }

    private GrayDO convert(GrayVO grayVO){
        GrayDO grayDO = new GrayDO();
        grayDO.setId(grayVO.getId());
        grayDO.setMelonId(grayVO.getMelonId());
        grayDO.setNo(grayVO.getGrayNo());
        grayDO.setRule(grayVO.getRule());
        grayDO.setStatus(1);
        if(ArrayUtils.isNotEmpty(grayVO.getGroups())){
            grayDO.setGroups(Joiner.on(",").join(grayVO.getGroups()));
        }
        grayDO.setConditionJson(grayVO.getJson().toJSONString());
        grayDO.setConditionClass(grayVO.getConditionClass());
        return grayDO;
    }

    private GrayVO convert(GrayDO grayDO) throws ClassNotFoundException {
        GrayVO grayVO = new GrayVO();
        grayVO.setId(grayDO.getId());
        grayVO.setGrayNo(grayDO.getNo());
        grayVO.setConditionClass(grayDO.getConditionClass());
        grayVO.setJson(JSONObject.parseObject(grayDO.getConditionJson()));
        grayVO.setMelonId(grayDO.getMelonId());
        grayVO.setRule(grayDO.getRule());

        if(StringUtils.isNotBlank(grayDO.getGroups())){
            List<String> groupList = Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings().split(grayDO.getGroups()));
            if(CollectionUtils.isNotEmpty(groupList)){
                String[] groups = new String[groupList.size()];
                groupList.toArray(groups);
                grayVO.setGroups(groups);
            }
        }

        return grayVO;
    }

    private MelonDO convet(MelonVO melonVO){
        MelonDO melonDO = new MelonDO();
        melonDO.setId(melonVO.getId());
        melonDO.setStatus(1);
        melonDO.setMode(melonVO.getMode().name());
        if(ArrayUtils.isNotEmpty(melonVO.getPrimaryGroups())){
            melonDO.setPrimaryGroups(Joiner.on(",").join(melonVO.getPrimaryGroups()));
        }

        if(ArrayUtils.isNotEmpty(melonVO.getForbiddenGroups())){
            melonDO.setForbiddenGroups(Joiner.on(",").join(melonVO.getForbiddenGroups()));
        }

        return melonDO;
    }

    private MelonVO convert(MelonDO melonDO){
        MelonVO melonVO = new MelonVO();
        melonVO.setId(melonDO.getId());
        melonVO.setMode(MelonMode.valueOf(melonDO.getMode()));

        if(StringUtils.isNotBlank(melonDO.getPrimaryGroups())){
            List<String> primaryGroupList = Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings().split(melonDO.getPrimaryGroups()));
            if(CollectionUtils.isNotEmpty(primaryGroupList)){
                String[] primaryGroups = new String[primaryGroupList.size()];
                primaryGroupList.toArray(primaryGroups);
                melonVO.setPrimaryGroups(primaryGroups);
            }
        }

        if(StringUtils.isNotBlank(melonDO.getForbiddenGroups())){
            List<String> forbiddenGroupList = Lists.newArrayList(Splitter.on(",").trimResults().omitEmptyStrings().split(melonDO.getForbiddenGroups()));
            if(CollectionUtils.isNotEmpty(forbiddenGroupList)){
                String[] forbiddenGroups = new String[forbiddenGroupList.size()];
                forbiddenGroupList.toArray(forbiddenGroups);
                melonVO.setForbiddenGroups(forbiddenGroups);
            }
        }

        return melonVO;
    }

    private MelonVO convert(MelonDO melonDO, List<GrayDO> grayDOList) throws ClassNotFoundException {
        if(melonDO == null){
            return null;
        }

        MelonVO melonVO = convert(melonDO);
        if(CollectionUtils.isNotEmpty(grayDOList)){
            List<GrayVO> list = new ArrayList<>();
            for(GrayDO grayDO:grayDOList){
                list.add(convert(grayDO));
            }
            melonVO.setGraySettingsList(list);
        }

        return melonVO;
    }
}
