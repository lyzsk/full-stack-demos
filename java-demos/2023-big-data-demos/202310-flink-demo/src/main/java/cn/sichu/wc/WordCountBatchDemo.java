package cn.sichu.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * DataSet API, 不推荐
 *
 * @author sichu
 * @date 2023/10/16
 **/
public class WordCountBatchDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> lineDS = env.readTextFile("202310-flink-demo/input/words.txt");
        // 切分, 转换 (word, 1)
        FlatMapOperator<String, Tuple2<String, Integer>> wordAndOne =
            lineDS.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                    // 按空格切分单词
                    String[] words = s.split(" ");
                    for (String word : words) {
                        // 将单词转换为(word, 1)
                        Tuple2<String, Integer> wordTuple2 = Tuple2.of(word, 1);
                        // 用collector向下游发送数据
                        collector.collect(wordTuple2);
                    }
                }
            });
        // 按照word分组
        UnsortedGrouping<Tuple2<String, Integer>> wordAndOneGoupby = wordAndOne.groupBy(0);
        // 各分组内聚合
        AggregateOperator<Tuple2<String, Integer>> sum = wordAndOneGoupby.sum(1);
        sum.print();
    }
}
