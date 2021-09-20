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
@Table(name = "test_user")
public class User implements BaseModel<User> {
    private String id;
    private String name;

    public static void rollback() {
        new User().deleteByConditions(
                Conditions.of().addCondition("id", ConditionType.not_equal, 0)
        );
        UserAccount.rollback();
    }
}
