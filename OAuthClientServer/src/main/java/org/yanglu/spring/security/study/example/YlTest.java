package org.yanglu.spring.security.study.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // 生成无参构造函数（关键！）
@AllArgsConstructor // 生成全参构造函数（方便手动创建对象）
public class YlTest {
    public String name;
    private int age;
}
