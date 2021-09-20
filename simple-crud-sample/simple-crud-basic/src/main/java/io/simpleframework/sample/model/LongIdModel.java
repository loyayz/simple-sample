package io.simpleframework.sample.model;

import io.simpleframework.crud.annotation.Column;
import io.simpleframework.crud.core.ConditionType;
import io.simpleframework.crud.core.Conditions;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LongIdModel extends MyBaseModel<LongIdModel> {
    private Long id;
    @Column(name = "name")
    private String userName;
    private Integer age;
    private String email;
    private Long roleId;

    public LongIdModel(Long id) {
        super();
        this.setId(id);
    }

    public static void rollback() {
        LongIdModel param = new LongIdModel();
        param.deleteByConditions(
                Conditions.of().addCondition("id", ConditionType.not_equal, 0)
        );
        List<LongIdModel> defaultModels = Arrays.asList(
                new LongIdModel(1L).setUserName("张三").setAge(10).setEmail("test1@loyayz.com").setRoleId(1L),
                new LongIdModel(2L).setUserName("李四").setAge(20).setEmail("test2@loyayz.com").setRoleId(2L),
                new LongIdModel(3L).setUserName("王五").setAge(28).setEmail("test3@loyayz.com").setRoleId(2L),
                new LongIdModel(4L).setUserName("赵六").setAge(21).setEmail("test4@loyayz.com").setRoleId(3L)
        );
        param.batchInsert(defaultModels);
    }
}
