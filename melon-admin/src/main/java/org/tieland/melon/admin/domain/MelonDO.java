package org.tieland.melon.admin.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author zhouxiang
 * @date 2019/8/28 14:31
 */
@Data
@Table(name = "melon_config")
@Entity
public class MelonDO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mode")
    private String mode;

    @Column(name = "primary_groups")
    private String primaryGroups;

    @Column(name = "forbidden_groups")
    private String forbiddenGroups;

    @Column(name = "status")
    private Integer status;
}
