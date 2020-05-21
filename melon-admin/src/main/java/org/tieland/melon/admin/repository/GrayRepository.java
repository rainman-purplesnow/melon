package org.tieland.melon.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.tieland.melon.admin.domain.GrayDO;

import java.util.List;
import java.util.Optional;

/**
 * @author zhouxiang
 * @date 2019/8/28 15:18
 */
@Repository
public interface GrayRepository extends JpaRepository<GrayDO, Long> {

    @Override
    @Query(" select g from GrayDO g where status=1 and id=?1 ")
    Optional<GrayDO> findById(Long id);

    @Query(" select g from GrayDO g where status=1 and melon_id=?1 ")
    List<GrayDO> findbyMelonId(Long melonId);

    @Override
    @Modifying
    @Query(" update GrayDO g set g.status=0 where id=?1 ")
    void deleteById(Long id);
}
