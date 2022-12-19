package cn.sichu;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * @author sichu
 * @date 2022/12/16
 **/
public class CodeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create(
                "jdbc:mysql://localhost:3306/springboot_vue?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC",
                "root", "root").globalConfig(builder -> {
                builder.author("sichu") // 设置作者
                    // .enableSwagger() // 开启 swagger 模式
                    .fileOverride() // 覆盖已生成文件
                    .outputDir("C://users/sichu/dev/mpGenerator"); // 指定输出目录
            }).packageConfig(builder -> {
                builder.parent("cn.sichu") // 设置父包名
                    .moduleName("") // 设置父包模块名
                    .pathInfo(Collections.singletonMap(OutputFile.mapperXml,
                        "C://users/sichu/dev/mpGenerator")); // 设置mapperXml生成路径
            }).strategyConfig(builder -> {
                builder.addInclude("sys_menu", "sys_role", "sys_role_menu", "sys_user", "sys_user_role") // 设置需要生成的表名
                    .addTablePrefix("sys_"); // 设置过滤表前缀
            }).templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
            .execute();
    }
}
