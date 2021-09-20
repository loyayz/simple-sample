package io.simpleframework.sample.model;

import io.simpleframework.crud.BaseModel;
import io.simpleframework.crud.annotation.IdStrategy;
import io.simpleframework.crud.core.ConditionType;
import io.simpleframework.crud.core.Conditions;
import io.simpleframework.crud.core.IdType;
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
    @IdStrategy(type = IdType.AUTO_INCREMENT)
    private String id;
    private String name;

    public static AutoIdModel createNewModel() {
        return new AutoIdModel().setName(UUID.randomUUID().toString());
    }

    public static void rollback() {
        new AutoIdModel().deleteByConditions(
                Conditions.of().addCondition("id", ConditionType.not_equal, "0")
        );
    }
}
