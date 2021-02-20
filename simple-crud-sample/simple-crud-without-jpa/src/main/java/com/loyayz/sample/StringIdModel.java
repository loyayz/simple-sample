package com.loyayz.sample;

import com.loyayz.simple.BaseModel;
import com.loyayz.simple.annotation.IdStrategy;
import com.loyayz.simple.annotation.IdStrategyType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@Accessors(fluent = true)
@NoArgsConstructor
public class StringIdModel implements BaseModel<StringIdModel> {
    @IdStrategy(type = IdStrategyType.UUID)
    private String id;
    private String name;
    private Date gmtCreate;
    private Date gmtModified;

    public StringIdModel(String id) {
        this.id = id;
    }
}
