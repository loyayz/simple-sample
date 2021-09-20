package io.simpleframework.sample.two;

import io.simpleframework.crud.BaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TwodsModel implements BaseModel<TwodsModel> {
    private Long id;
    private String name;
    private Integer age;

    public TwodsModel(Long id) {
        super();
        this.setId(id);
    }
}
