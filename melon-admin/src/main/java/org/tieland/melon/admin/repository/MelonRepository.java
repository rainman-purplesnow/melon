package org.tieland.melon.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.tieland.melon.admin.domain.MelonDO;

import java.util.List;
import java.util.Optional;

/**
 * @author zhouxiang
 * @date 2019/8/28 15:18
 */
@Repository
public interface MelonRepository extends JpaRepository<MelonDO, Long> {

    @Override
    @Query(" select m from MelonDO m where status = 1")
    List<MelonDO> findAll();

    @Override
    @Query(" select m from MelonDO m where status = 1 and id=?1")
    Optional<MelonDO> findById(Long id);

    @Override
    @Modifying
    @Query(" update MelonDO m set m.status=0 where id=?1 ")
    void deleteById(Long id);
}
