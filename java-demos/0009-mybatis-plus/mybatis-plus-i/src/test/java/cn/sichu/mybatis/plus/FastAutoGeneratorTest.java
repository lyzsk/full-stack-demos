package cn.sichu.mybatis.plus;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @author sichu
 * @date 2022/12/07
 **/
public class FastAutoGeneratorTest {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/mybatis_plus?characterEncoding=utf-8&useSSL=false",
                "root", "root").globalConfig(builder -> {
                builder.author("sichu") // 设置作者
                    // .enableSwagger()    // 开启 swagger 模式
                    .fileOverride() // 覆盖已生成文件
                    .outputDir("C://users/sichu/mybatis_plus"); // 指定输出目录
            }).packageConfig(builder -> {
                builder.parent("cn.sichu.mybatis")  // 设置父包名
                    .moduleName("plus")  // 设置父包模块名
                    .pathInfo(
                        Collections.singletonMap(OutputFile.mapperXml, "C://users/sichu/mybatis_plus")); // 设置mapper.xml生成路径
            }).strategyConfig(builder -> {
                builder.addInclude("t_user")    // 设置要生成的表名
                    .addTablePrefix("t_", "c_");    // 设置过滤表前缀
            }).templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板, 默认是velocity引擎模板
            .execute();
    }
}
