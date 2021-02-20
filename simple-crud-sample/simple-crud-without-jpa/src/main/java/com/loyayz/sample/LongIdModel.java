package com.loyayz.sample;

import com.loyayz.simple.BaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LongIdModel implements BaseModel<LongIdModel> {
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private Long roleId;

    public LongIdModel(Long id) {
        super();
        this.setId(id);
    }
}
