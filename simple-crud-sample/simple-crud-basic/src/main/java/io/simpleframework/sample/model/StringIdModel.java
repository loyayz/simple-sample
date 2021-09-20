package io.simpleframework.sample.model;

import io.simpleframework.crud.BaseModel;
import io.simpleframework.crud.annotation.IdStrategy;
import io.simpleframework.crud.core.ConditionType;
import io.simpleframework.crud.core.Conditions;
import io.simpleframework.crud.core.IdType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class StringIdModel implements BaseModel<StringIdModel> {
    @IdStrategy(type = IdType.UUID32)
    private String id;
    private String name;
    private Date gmtCreate;
    private Date gmtModified;

    public StringIdModel(String id) {
        this.id = id;
    }

    public static void rollback() {
        StringIdModel param = new StringIdModel();
        param.deleteByConditions(
                Conditions.of().addCondition("id", ConditionType.not_equal, "0")
        );

        List<StringIdModel> defaultModels = Arrays.asList(
                new StringIdModel("AAA").setName("张三").setGmtCreate(toDate(2020, 1, 1)).setGmtModified(toDate(2020, 2, 1)),
                new StringIdModel("BBB").setName("李四").setGmtCreate(toDate(2020, 1, 2)).setGmtModified(toDate(2020, 2, 2)),
                new StringIdModel("CCC").setName("王五").setGmtCreate(toDate(2020, 1, 3)).setGmtModified(toDate(2020, 2, 3)),
                new StringIdModel("DDD").setName("赵六").setGmtCreate(toDate(2020, 1, 4)).setGmtModified(toDate(2020, 2, 4))
        );
        param.batchInsert(defaultModels);
    }

    private static Date toDate(int year, int month, int dayOfMonth) {
        LocalDate date = LocalDate.of(year, month, dayOfMonth);
        return Date.from(date.atStartOfDay(ZoneOffset.systemDefault()).toInstant());
    }

}
