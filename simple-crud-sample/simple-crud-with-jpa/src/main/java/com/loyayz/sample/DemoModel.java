package com.loyayz.sample;

import com.loyayz.simple.BaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "long_id_model")
public class DemoModel implements BaseModel<DemoModel> {
    @Id
    @Column(name = "id")
    private Long modelId;
    private String name;
    private Integer age;
    private String email;
    private Long roleId;
}
