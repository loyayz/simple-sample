package com.loyayz.sample;

import com.loyayz.simple.BaseModel;
import com.loyayz.simple.annotation.IdStrategy;
import com.loyayz.simple.annotation.IdStrategyType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class AutoIdModel implements BaseModel<AutoIdModel> {
    @IdStrategy(type = IdStrategyType.AUTO)
    private String id;
    private String name;

    public static AutoIdModel createNewModel() {
        return new AutoIdModel().setName(UUID.randomUUID().toString());
    }

}
