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
@Table(name = "test_user_account")
public class UserAccount implements BaseModel<UserAccount> {
    private String id;
    private String userId;
    private String accountName;
    private String accountPassword;

    public static void rollback() {
        new UserAccount().deleteByConditions(
                Conditions.of().addCondition("id", ConditionType.not_equal, 0)
        );
    }
}
