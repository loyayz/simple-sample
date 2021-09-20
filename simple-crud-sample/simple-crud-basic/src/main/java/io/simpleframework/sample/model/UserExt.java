package io.simpleframework.sample.model;

import io.simpleframework.crud.BaseModel;
import io.simpleframework.crud.annotation.Table;
import io.simpleframework.crud.core.ConditionType;
import io.simpleframework.crud.core.Conditions;
import lombok.Data;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@Table(name = "test_user_ext")
public class UserExt implements BaseModel<UserExt> {
    private String userId;
    private String ext;

    public static void rollback() {
        new UserExt().deleteByConditions(
                Conditions.of().addCondition("id", ConditionType.not_equal, 0)
        );
    }
}
