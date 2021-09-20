package io.simpleframework.sample.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@TableName(value = "string_id_model")
public class Demo {

    private String id;
    private String name;
    private Date gmtCreate;
    private Date gmtModified;

}
