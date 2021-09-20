package io.simpleframework.sample.test;

import io.simpleframework.crud.annotation.Condition;
import io.simpleframework.crud.core.ConditionType;
import io.simpleframework.crud.core.QueryConfig;
import io.simpleframework.crud.core.QuerySorter;
import io.simpleframework.sample.model.LongIdModel;
import io.simpleframework.sample.model.StringIdModel;
import lombok.Data;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public final class BasicQueryTest {

    public static void test() {
        BasicCommandTest.execThanRollback(null);

        findById();
        listByIds();
        listByCondition();
        listBySorter();
        listByConfig();
        listByAnnotation();
        countAndExist();
    }

    /**
     * 根据id查询
     */
    public static void findById() {
        LongIdModel longIdModel = new LongIdModel().findById(2L);
        Assertions.assertEquals(2L, longIdModel.getId());
        Assertions.assertEquals("李四", longIdModel.getUserName());
        Assertions.assertEquals(20, longIdModel.getAge());
        Assertions.assertEquals("test2@loyayz.com", longIdModel.getEmail());
        Assertions.assertEquals(2L, longIdModel.getRoleId());

        StringIdModel stringIdModel = new StringIdModel().findById("CCC");
        Assertions.assertEquals("王五", stringIdModel.getName());
        Assertions.assertNotNull(stringIdModel.getGmtCreate());
        Assertions.assertNotNull(stringIdModel.getGmtModified());

        Assertions.assertNull(new LongIdModel().findById(null));
        Assertions.assertNull(new LongIdModel().findById(10L));
        Assertions.assertNull(new StringIdModel().findById(null));
        Assertions.assertNull(new StringIdModel().findById(""));
    }

    /**
     * 根据ids查询
     */
    public static void listByIds() {
        Assertions.assertArrayEquals(
                new LongIdModel().listByIds(Arrays.asList(1L, 2L, 3L, 4L)).toArray(),
                new Object[]{
                        new LongIdModel().findById(1L),
                        new LongIdModel().findById(2L),
                        new LongIdModel().findById(3L),
                        new LongIdModel().findById(4L)
                }
        );
        Assertions.assertArrayEquals(
                new LongIdModel().listByIds(Arrays.asList(2L, 4L)).toArray(),
                new Object[]{
                        new LongIdModel().findById(2L),
                        new LongIdModel().findById(4L),
                }
        );
        Assertions.assertArrayEquals(
                new StringIdModel().listByIds(Arrays.asList("BBB", "CCC")).toArray(),
                new Object[]{
                        new StringIdModel().findById("BBB"),
                        new StringIdModel().findById("CCC")
                }
        );
        Assertions.assertTrue(new LongIdModel().listByIds(null).isEmpty());
        Assertions.assertTrue(new LongIdModel().listByIds(new ArrayList<>()).isEmpty());
        Assertions.assertTrue(new StringIdModel().listByIds(null).isEmpty());
        Assertions.assertTrue(new StringIdModel().listByIds(new ArrayList<>()).isEmpty());
    }

    /**
     * 条件查询
     */
    public static void listByCondition() {
        // 无条件查询（查全部）
        Assertions.assertArrayEquals(
                new LongIdModel().listByCondition().toArray(),
                new LongIdModel().listByIds(Arrays.asList(1, 2, 3, 4)).toArray());
        // 单条件查询
        Assertions.assertTrue(new LongIdModel(10L).listByCondition().isEmpty());
        Assertions.assertArrayEquals(
                new LongIdModel().setRoleId(2L).listByCondition().toArray(),
                new LongIdModel().listByIds(Arrays.asList(2, 3)).toArray());
        // 多条件查询
        LongIdModel queryModel = new LongIdModel().setRoleId(2L).setUserName("李四");
        List<LongIdModel> models = queryModel.listByCondition();
        Assertions.assertEquals(1, models.size());
        Assertions.assertEquals("李四", models.get(0).getUserName());
        // 日期条件
        StringIdModel stringQueryModel = new StringIdModel()
                .setGmtCreate(toDate(2020, 1, 1));
        Assertions.assertEquals("张三", stringQueryModel.listByCondition().get(0).getName());
        stringQueryModel.setGmtCreate(toDate(2020, 1, 2));
        Assertions.assertEquals("李四", stringQueryModel.listByCondition().get(0).getName());
        stringQueryModel.setGmtCreate(toDate(2020, 1, 3));
        Assertions.assertEquals("王五", stringQueryModel.listByCondition().get(0).getName());
        stringQueryModel.setGmtModified(toDate(2020, 2, 3));
        Assertions.assertEquals("王五", stringQueryModel.listByCondition().get(0).getName());
        stringQueryModel.setGmtModified(toDate(2020, 2, 4));
        Assertions.assertEquals(0, stringQueryModel.listByCondition().size());
    }

    /**
     * 查询并排序
     */
    public static void listBySorter() {
        // 无条件查询（查全部）
        // id 倒序
        Assertions.assertArrayEquals(
                new LongIdModel().listBySorter(QuerySorter.desc(LongIdModel::getId)).toArray(),
                new Object[]{
                        new LongIdModel().findById(4L),
                        new LongIdModel().findById(3L),
                        new LongIdModel().findById(2L),
                        new LongIdModel().findById(1L)
                });
        // roleId 倒序
        Assertions.assertArrayEquals(
                new LongIdModel().listBySorter(QuerySorter.desc(LongIdModel::getRoleId)).toArray(),
                new Object[]{
                        new LongIdModel().findById(4L),
                        new LongIdModel().findById(2L),
                        new LongIdModel().findById(3L),
                        new LongIdModel().findById(1L)
                }
        );
        // roleId 倒序 + id 倒序
        Assertions.assertArrayEquals(
                new LongIdModel().listBySorter(QuerySorter.of().addDesc(LongIdModel::getRoleId).addDesc(LongIdModel::getId)).toArray(),
                new Object[]{
                        new LongIdModel().findById(4L),
                        new LongIdModel().findById(3L),
                        new LongIdModel().findById(2L),
                        new LongIdModel().findById(1L)
                }
        );
        // 单条件查询
        LongIdModel queryModel = new LongIdModel().setRoleId(2L);
        // id 倒序
        Assertions.assertArrayEquals(
                queryModel.listBySorter(QuerySorter.desc(LongIdModel::getId)).toArray(),
                new Object[]{
                        new LongIdModel().findById(3L),
                        new LongIdModel().findById(2L),
                }
        );
        // age 升序
        Assertions.assertArrayEquals(
                queryModel.listBySorter(QuerySorter.asc(LongIdModel::getAge)).toArray(),
                new LongIdModel().listByIds(Arrays.asList(2, 3)).toArray()
        );
        // age 倒序
        Assertions.assertArrayEquals(
                queryModel.listBySorter(QuerySorter.desc(LongIdModel::getAge)).toArray(),
                new Object[]{
                        new LongIdModel().findById(3L),
                        new LongIdModel().findById(2L),
                }
        );
    }

    /**
     * 动态条件查询
     */
    public static void listByConfig() {
        StringIdModel queryModel = new StringIdModel();
        List<StringIdModel> models = queryModel.listByCondition();
        Assertions.assertEquals(4, models.size());
        for (StringIdModel model : models) {
            Assertions.assertNotNull(model.getId());
            Assertions.assertNotNull(model.getName());
            Assertions.assertNotNull(model.getGmtCreate());
            Assertions.assertNotNull(model.getGmtModified());
        }
        // 指定要查询的字段
        QueryConfig queryConfig = QueryConfig.of()
                .addSelect(StringIdModel::getId)
                .addSelect(StringIdModel::getGmtCreate);
        for (StringIdModel model : queryModel.listByCondition(queryConfig)) {
            Assertions.assertNotNull(model.getId());
            Assertions.assertNull(model.getName());
            Assertions.assertNotNull(model.getGmtCreate());
            Assertions.assertNull(model.getGmtModified());
        }
        // 查询条件
        queryModel.setId("AAA");
        Assertions.assertEquals(1, queryModel.listByCondition(queryConfig).size());
        // 查询条件指定类型
        queryConfig.addCondition(StringIdModel::getId, ConditionType.not_equal);
        Assertions.assertEquals(3, queryModel.listByCondition(queryConfig).size());
        // 同字段多种条件
        queryConfig.addCondition(StringIdModel::getId, ConditionType.not_equal, "BBB");
        Assertions.assertEquals(2, queryModel.listByCondition(queryConfig).size());
        queryConfig.addCondition(StringIdModel::getId, ConditionType.like_all, "C");
        Assertions.assertEquals(1, queryModel.listByCondition(queryConfig).size());
        // 多条件
        queryConfig.addCondition(StringIdModel::getName, ConditionType.in, "李四", "赵六");
        Assertions.assertEquals(0, queryModel.listByCondition(queryConfig).size());

        // null
        queryConfig = QueryConfig.of();
        queryConfig.addCondition(StringIdModel::getName, ConditionType.not_null);
        Assertions.assertEquals(1, queryModel.listByCondition(queryConfig).size());
        queryModel = new StringIdModel();
        Assertions.assertEquals(4, queryModel.listByCondition(queryConfig).size());
        queryConfig.addCondition(StringIdModel::getName, ConditionType.is_null);
        Assertions.assertEquals(0, queryModel.listByCondition(queryConfig).size());

        // 日期条件
        queryConfig = QueryConfig.of();
        queryModel = new StringIdModel()
                .setGmtCreate(toDate(2020, 1, 1));
        Assertions.assertEquals(1, queryModel.listByCondition(queryConfig).size());
        queryConfig.addCondition(StringIdModel::getGmtCreate, ConditionType.not_equal);
        Assertions.assertEquals(3, queryModel.listByCondition(queryConfig).size());
        queryModel.setGmtCreate(toDate(2020, 1, 2));
        Assertions.assertEquals(3, queryModel.listByCondition(queryConfig).size());

        queryConfig = QueryConfig.of();
        queryConfig.addCondition(StringIdModel::getGmtCreate, ConditionType.greater_than);
        Assertions.assertEquals(2, queryModel.listByCondition(queryConfig).size());

        queryConfig = QueryConfig.of();
        queryConfig.addCondition(StringIdModel::getGmtCreate, ConditionType.great_equal);
        Assertions.assertEquals(3, queryModel.listByCondition(queryConfig).size());
        queryConfig.addCondition(StringIdModel::getGmtCreate, ConditionType.less_than, toDate(2020, 1, 4));
        Assertions.assertEquals(2, queryModel.listByCondition(queryConfig).size());
    }

    public static void listByAnnotation() {
        LongIdQry qry = new LongIdQry();
        qry.setId(2L);
        Assertions.assertEquals(1, new LongIdModel().listByAnnotation(qry).size());

        qry = new LongIdQry();
        List<Long> ids = new ArrayList<>();
        ids.add(3L);
        ids.add(4L);
        qry.setIds(ids);
        Assertions.assertEquals(2, new LongIdModel().listByAnnotation(qry).size());

        qry = new LongIdQry();
        Assertions.assertEquals(3, new LongIdModel().setRoleId(1L).listByAnnotation(qry).size());
    }

    public static void countAndExist() {
        // 无条件查询统计
        Assertions.assertAll(
                () -> Assertions.assertEquals(4, new LongIdModel().countByCondition()),
                () -> Assertions.assertTrue(new LongIdModel().existByCondition())
        );
        // 单条件查询统计
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, new LongIdModel(1L).countByCondition()),
                () -> Assertions.assertEquals(1, new LongIdModel().setUserName("李四").countByCondition()),
                () -> Assertions.assertEquals(2, new LongIdModel().setRoleId(2L).countByCondition()),
                () -> Assertions.assertTrue(new LongIdModel(1L).existByCondition()),
                () -> Assertions.assertTrue(new LongIdModel().setUserName("李四").existByCondition()),
                () -> Assertions.assertTrue(new LongIdModel().setRoleId(2L).existByCondition())
        );
        // 多条件查询统计
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, new LongIdModel(1L).setUserName("张三").countByCondition()),
                () -> Assertions.assertEquals(0, new LongIdModel(1L).setUserName("李四").countByCondition()),
                () -> Assertions.assertTrue(new LongIdModel(1L).setUserName("张三").existByCondition()),
                () -> Assertions.assertFalse(new LongIdModel(1L).setUserName("李四").existByCondition())
        );
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
