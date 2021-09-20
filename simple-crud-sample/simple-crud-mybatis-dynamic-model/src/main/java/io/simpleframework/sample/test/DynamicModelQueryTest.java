package io.simpleframework.sample.test;

import io.simpleframework.crud.BaseModelMapper;
import io.simpleframework.crud.Models;
import io.simpleframework.crud.annotation.Condition;
import io.simpleframework.crud.core.ConditionType;
import io.simpleframework.crud.core.QueryConfig;
import io.simpleframework.crud.core.QuerySorter;
import lombok.Data;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

import static io.simpleframework.sample.Application.LONG_ID_MODEL_NAME;
import static io.simpleframework.sample.Application.STRING_ID_MODEL_NAME;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class DynamicModelQueryTest {

    public static void test() {
        findById();
        listByIds();
        listByCondition();
        listBySorter();
        listByConfig();
        listByAnnotation();
        countAndExist();
    }

    public static BaseModelMapper<Map<String, Object>> longIdModelMapper() {
        return Models.mapper(LONG_ID_MODEL_NAME);
    }

    public static BaseModelMapper<Map<String, Object>> stringIdModelMapper() {
        return Models.mapper(STRING_ID_MODEL_NAME);
    }

    /**
     * 根据id查询
     */
    public static void findById() {
        Map<String, Object> longIdModel = longIdModelMapper().findById(2L);
        Assertions.assertNotNull(longIdModel);
        Assertions.assertEquals(2L, longIdModel.get("id"));
        Assertions.assertEquals("李四", longIdModel.get("name"));
        Assertions.assertEquals(20, longIdModel.get("age"));
        Assertions.assertEquals("test2@loyayz.com", longIdModel.get("email"));
        Assertions.assertEquals(2L, longIdModel.get("roleId"));

        Map<String, Object> stringIdModel = stringIdModelMapper().findById("CCC");
        Assertions.assertNotNull(stringIdModel);
        Assertions.assertEquals("王五", stringIdModel.get("name"));
        Assertions.assertNotNull(stringIdModel.get("gmtCreate"));
        Assertions.assertNull(stringIdModel.get("gmtModified"));

        Assertions.assertNull(longIdModelMapper().findById(null));
        Assertions.assertNull(longIdModelMapper().findById(10L));
        Assertions.assertNull(stringIdModelMapper().findById(null));
        Assertions.assertNull(stringIdModelMapper().findById(""));
    }

    /**
     * 根据ids查询
     */
    public static void listByIds() {
        Assertions.assertArrayEquals(
                longIdModelMapper().listByIds(Arrays.asList(1L, 2L, 3L, 4L)).toArray(),
                new Object[]{
                        longIdModelMapper().findById(1L),
                        longIdModelMapper().findById(2L),
                        longIdModelMapper().findById(3L),
                        longIdModelMapper().findById(4L),
                }
        );
        Assertions.assertArrayEquals(
                longIdModelMapper().listByIds(Arrays.asList(2L, 4L)).toArray(),
                new Object[]{
                        longIdModelMapper().findById(2L),
                        longIdModelMapper().findById(4L),
                }
        );
        Assertions.assertArrayEquals(
                stringIdModelMapper().listByIds(Arrays.asList("BBB", "CCC")).toArray(),
                new Object[]{
                        stringIdModelMapper().findById("BBB"),
                        stringIdModelMapper().findById("CCC")
                }
        );
        Assertions.assertTrue(longIdModelMapper().listByIds(null).isEmpty());
        Assertions.assertTrue(longIdModelMapper().listByIds(new ArrayList<>()).isEmpty());
        Assertions.assertTrue(stringIdModelMapper().listByIds(null).isEmpty());
        Assertions.assertTrue(stringIdModelMapper().listByIds(new ArrayList<>()).isEmpty());
    }

    /**
     * 条件查询
     */
    public static void listByCondition() {
        // 无条件查询（查全部）
        Assertions.assertArrayEquals(
                longIdModelMapper().listByCondition(null).toArray(),
                longIdModelMapper().listByIds(Arrays.asList(1, 2, 3, 4)).toArray());
        // 单条件查询
        Map<String, Object> param = new HashMap<>(3);
        param.put("id", 10L);
        Assertions.assertTrue(longIdModelMapper().listByCondition(param).isEmpty());

        param = new HashMap<>(3);
        param.put("roleId", 2L);
        Assertions.assertArrayEquals(
                longIdModelMapper().listByCondition(param).toArray(),
                longIdModelMapper().listByIds(Arrays.asList(2, 3)).toArray());
        // 多条件查询
        param = new HashMap<>(3);
        param.put("roleId", 2L);
        param.put("name", "李四");
        List<Map<String, Object>> models = longIdModelMapper().listByCondition(param);
        Assertions.assertEquals(1, models.size());
        Assertions.assertEquals("李四", models.get(0).get("name"));
        // 日期条件
        param = new HashMap<>(3);
        param.put("gmtCreate", toDate(2020, 1, 1));
        Assertions.assertEquals("张三", stringIdModelMapper().listByCondition(param).get(0).get("name"));
        param.put("gmtCreate", toDate(2020, 1, 2));
        Assertions.assertEquals("李四", stringIdModelMapper().listByCondition(param).get(0).get("name"));
        param.put("gmtCreate", toDate(2020, 1, 3));
        Assertions.assertEquals("王五", stringIdModelMapper().listByCondition(param).get(0).get("name"));

        param.put("gmtModified", toDate(2020, 2, 3));
        Assertions.assertEquals("王五", stringIdModelMapper().listByCondition(param).get(0).get("name"));
        param.put("gmtModified", toDate(2020, 2, 4));
        Assertions.assertEquals("王五", stringIdModelMapper().listByCondition(param).get(0).get("name"));
    }

    /**
     * 查询并排序
     */
    public static void listBySorter() {
        // 无条件查询（查全部）
        // id 倒序
        Assertions.assertArrayEquals(
                longIdModelMapper().listBySorter(QuerySorter.desc("id")).toArray(),
                new Object[]{
                        longIdModelMapper().findById(4L),
                        longIdModelMapper().findById(3L),
                        longIdModelMapper().findById(2L),
                        longIdModelMapper().findById(1L)
                });
        // roleId 倒序
        Assertions.assertArrayEquals(
                longIdModelMapper().listBySorter(QuerySorter.desc("roleId")).toArray(),
                new Object[]{
                        longIdModelMapper().findById(4L),
                        longIdModelMapper().findById(2L),
                        longIdModelMapper().findById(3L),
                        longIdModelMapper().findById(1L)
                }
        );
        // roleId 倒序 + id 倒序
        Assertions.assertArrayEquals(
                longIdModelMapper().listBySorter(QuerySorter.of().addDesc("roleId").addDesc("id")).toArray(),
                new Object[]{
                        longIdModelMapper().findById(4L),
                        longIdModelMapper().findById(3L),
                        longIdModelMapper().findById(2L),
                        longIdModelMapper().findById(1L)
                }
        );
        // 单条件查询
        Map<String, Object> param = new HashMap<>(3);
        param.put("roleId", 2L);
        // id 倒序
        Assertions.assertArrayEquals(
                longIdModelMapper().listBySorter(param, QuerySorter.desc("id")).toArray(),
                new Object[]{
                        longIdModelMapper().findById(3L),
                        longIdModelMapper().findById(2L),
                }
        );
        // age 升序
        Assertions.assertArrayEquals(
                longIdModelMapper().listBySorter(param, QuerySorter.asc("age")).toArray(),
                longIdModelMapper().listByIds(Arrays.asList(2, 3)).toArray()
        );
        // age 倒序
        Assertions.assertArrayEquals(
                longIdModelMapper().listBySorter(param, QuerySorter.desc("age")).toArray(),
                new Object[]{
                        longIdModelMapper().findById(3L),
                        longIdModelMapper().findById(2L),
                }
        );
    }

    /**
     * 动态条件查询
     */
    public static void listByConfig() {
        List<Map<String, Object>> models = stringIdModelMapper().listByCondition(null);
        Assertions.assertEquals(4, models.size());
        for (Map<String, Object> model : models) {
            Assertions.assertNotNull(model.get("id"));
            Assertions.assertNotNull(model.get("name"));
            Assertions.assertNotNull(model.get("gmtCreate"));
            Assertions.assertNull(model.get("gmtModified"));
        }
        // 指定要查询的字段
        QueryConfig queryConfig = QueryConfig.of()
                .addSelect("id")
                .addSelect("gmtCreate");
        for (Map<String, Object> model : stringIdModelMapper().listByCondition(null, queryConfig)) {
            Assertions.assertNotNull(model.get("id"));
            Assertions.assertNull(model.get("name"));
            Assertions.assertNotNull(model.get("gmtCreate"));
            Assertions.assertNull(model.get("gmtModified"));
        }
        // 查询条件
        Map<String, Object> param = new HashMap<>(3);
        param.put("id", "AAA");
        Assertions.assertEquals(1, stringIdModelMapper().listByCondition(param, queryConfig).size());
        // 查询条件指定类型
        queryConfig.addCondition("id", ConditionType.not_equal);
        Assertions.assertEquals(3, stringIdModelMapper().listByCondition(param, queryConfig).size());
        // 同字段多种条件
        queryConfig.addCondition("id", ConditionType.not_equal, "BBB");
        Assertions.assertEquals(2, stringIdModelMapper().listByCondition(param, queryConfig).size());
        queryConfig.addCondition("id", ConditionType.like_all, "C");
        Assertions.assertEquals(1, stringIdModelMapper().listByCondition(param, queryConfig).size());
        // 多条件
        queryConfig.addCondition("name", ConditionType.in, "李四", "赵六");
        Assertions.assertEquals(0, stringIdModelMapper().listByCondition(param, queryConfig).size());

        // 日期条件
        queryConfig = QueryConfig.of();
        param = new HashMap<>(3);
        param.put("gmtCreate", toDate(2020, 1, 1));
        Assertions.assertEquals(1, stringIdModelMapper().listByCondition(param, queryConfig).size());
        queryConfig.addCondition("gmtCreate", ConditionType.not_equal);
        Assertions.assertEquals(3, stringIdModelMapper().listByCondition(param, queryConfig).size());
        param.put("gmtCreate", toDate(2020, 1, 2));
        Assertions.assertEquals(3, stringIdModelMapper().listByCondition(param, queryConfig).size());

        queryConfig = QueryConfig.of();
        queryConfig.addCondition("gmtCreate", ConditionType.greater_than);
        Assertions.assertEquals(2, stringIdModelMapper().listByCondition(param, queryConfig).size());

        queryConfig = QueryConfig.of();
        queryConfig.addCondition("gmtCreate", ConditionType.great_equal);
        Assertions.assertEquals(3, stringIdModelMapper().listByCondition(param, queryConfig).size());
        queryConfig.addCondition("gmtCreate", ConditionType.less_than, toDate(2020, 1, 4));
        Assertions.assertEquals(2, stringIdModelMapper().listByCondition(param, queryConfig).size());
    }

    public static void listByAnnotation() {
        LongIdQry qry = new LongIdQry();
        qry.setId(2L);
        Assertions.assertEquals(1, longIdModelMapper().listByAnnotation(qry).size());

        qry = new LongIdQry();
        List<Long> ids = new ArrayList<>();
        ids.add(3L);
        ids.add(4L);
        qry.setIds(ids);
        Assertions.assertEquals(2, longIdModelMapper().listByAnnotation(qry).size());

        qry = new LongIdQry();
        Map<String, Object> param = new HashMap<>(3);
        param.put("roleId", 1L);
        Assertions.assertEquals(3, longIdModelMapper().listByAnnotation(param, qry).size());
    }

    public static void countAndExist() {
        // 无条件查询统计
        Assertions.assertEquals(4, longIdModelMapper().countByCondition(null));
        Assertions.assertTrue(longIdModelMapper().existByCondition(null));
        // 单条件查询统计
        Map<String, Object> param = new HashMap<>(3);
        param.put("id", 1L);
        Assertions.assertEquals(1, longIdModelMapper().countByCondition(param));
        Assertions.assertTrue(longIdModelMapper().existByCondition(param));

        param = new HashMap<>(3);
        param.put("name", "李四");
        Assertions.assertEquals(1, longIdModelMapper().countByCondition(param));
        Assertions.assertTrue(longIdModelMapper().existByCondition(param));

        param = new HashMap<>(3);
        param.put("roleId", 2L);
        Assertions.assertEquals(2, longIdModelMapper().countByCondition(param));
        Assertions.assertTrue(longIdModelMapper().existByCondition(param));

        // 多条件查询统计
        param = new HashMap<>(3);
        param.put("id", 1L);
        param.put("name", "张三");
        Assertions.assertEquals(1, longIdModelMapper().countByCondition(param));
        Assertions.assertTrue(longIdModelMapper().existByCondition(param));
        param.put("name", "李四");
        Assertions.assertEquals(0, longIdModelMapper().countByCondition(param));
        Assertions.assertFalse(longIdModelMapper().existByCondition(param));
    }

    private static Date toDate(int year, int month, int dayOfMonth) {
        LocalDate date = LocalDate.of(year, month, dayOfMonth);
        return Date.from(date.atStartOfDay(ZoneOffset.systemDefault()).toInstant());
    }

    @Data
    private static class LongIdQry {
        @Condition
        private Long id;
        @Condition(field = "id", type = ConditionType.in)
        private List<Long> ids;
        @Condition(field = "roleId", type = ConditionType.not_equal)
        private Long role;
    }

}
