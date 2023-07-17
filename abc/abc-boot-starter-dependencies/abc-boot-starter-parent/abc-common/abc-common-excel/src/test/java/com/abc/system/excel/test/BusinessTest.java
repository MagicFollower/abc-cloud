package com.abc.system.excel.test;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 关于excel模块的实际使用场景分析
 *
 * @Description 关于excel模块的实际使用场景分析
 * @Author Trivis
 * @Date 2023/5/22 19:00
 * @Version 1.0
 */
class BusinessTest {


    @Test
    void test02() {
        HashMap stringHashMap = new HashMap<String, String>();
        System.out.println(stringHashMap.get("asdasda"));
    }


    /**
     * 解析Excel后，我们会得到Excel表格中的数据，我们约定使用JSONObject存储解析后的数据，因为这样可以方便地在各个类型之间转换；
     * 1.获取数据后我们需要确保解析的数据是唯一不重复的；
     * 2.我们需要从中提取“待添加的数据”和“待更新的数据”，这两个操作设计集合的差集与交集运算；这也是本测试用例的目的所在。
     */
    @Test
    void test01() {
        // assertEquals(1, 1);

        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        List<Integer> list3 = new ArrayList<>(Arrays.asList(1, 2, 3, 11, 12, 13));
        // list1-list2
        list1.removeAll(list3);
        System.out.println("list1 = " + list1);
        // list1 ∩ list2
        list2.retainAll(list3);
        System.out.println("list2 = " + list2);


        // 🤔️如果根据对象中的两个属性判断呢（code+name）
        User user1 = new User("id-1", "code-1", "name-1", "memo-1");
        User user2 = new User("id-2", "code-2", "name-2", "memo-2");
        User user3 = new User("id-3", "code-3", "name-3", "memo-3");
        User user4 = new User("id-4", "code-4", "name-4", "memo-4");
        User user5 = new User("id-5", "code-5", "name-5", "memo-5");
        Collection<User> userListFromImport = Lists.newArrayList(user1, user2, user3, user4, user5);
        // 从DB中获取联合唯一索引字段，拼接为String
        List<String> userListFromDB = Lists.newArrayList(user1, user2)
                .stream().map(x -> x.getCode().concat(x.getName())).collect(Collectors.toList());
        // 获取DB中已存在的记录(A ∩ B)
        List<User> collect1 = userListFromImport.stream().filter(x ->
                userListFromDB.contains(x.getCode().concat(x.getName()))).collect(Collectors.toList());
        System.out.println(JSONObject.toJSONString(collect1, JSONWriter.Feature.PrettyFormat));
        // 获取DB中不存在的记录(A - B), 方式一
        List<User> collect2 = userListFromImport.stream().filter(x ->
                !userListFromDB.contains(x.getCode().concat(x.getName()))).collect(Collectors.toList());
        System.out.println(JSONObject.toJSONString(collect2, JSONWriter.Feature.PrettyFormat));
        // 获取DB中不存在的记录(A - B), 方式二
        userListFromImport.removeAll(collect1);
        System.out.println("userListFromImport = \n"
                + JSONObject.toJSONString(userListFromImport, JSONWriter.Feature.PrettyFormat));

    }
}

@Data
@NoArgsConstructor
class User implements Serializable {
    private String id;

    private String code;
    private String name;
    private String memo;

    public User(String id, String code, String name, String memo) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.memo = memo;
    }
}
