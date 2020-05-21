package org.tieland.melon.admin.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author zhouxiang
 * @date 2019/8/22 11:42
 */
@Data
@Entity
@Table(name = "gray_config")
public class GrayDO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "melon_id")
    private Long melonId;

    @Column(name = "gray_no")
    private String no;

    @Column(name = "rule")
    private String rule;

    @Column(name = "groups")
    private String groups;

    @Column(name = "condition_json")
    private String conditionJson;

    @Column(name = "condition_class")
    private String conditionClass;

    @Column(name = "status")
    private Integer status;
}
