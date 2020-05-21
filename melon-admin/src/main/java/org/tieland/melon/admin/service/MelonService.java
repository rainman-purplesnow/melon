package org.tieland.melon.admin.service;

import org.tieland.melon.admin.vo.GrayVO;
import org.tieland.melon.admin.vo.MelonVO;
import java.util.List;
import java.util.Optional;

/**
 * @author zhouxiang
 * @date 2019/8/28 15:26
 */
public interface MelonService {

    List<MelonVO> findAll() throws ClassNotFoundException;

    Optional<MelonVO> findById(Long id) throws ClassNotFoundException;

    void save(MelonVO melonVO);

    void delete(Long id);

    void saveGray(GrayVO grayVO);

    void deleteGray(Long id);

    GrayVO getGray(Long id);
}
