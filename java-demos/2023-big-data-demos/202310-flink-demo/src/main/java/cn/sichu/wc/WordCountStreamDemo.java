package cn.sichu.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * DataStream API, 读文件(有界流)
 *
 * @author sichu
 * @date 2023/10/17
 **/
public class WordCountStreamDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> lineDS = env.readTextFile("202310-flink-demo/input/words.txt");
        SingleOutputStreamOperator<Tuple2<String, Integer>> wordAndOne =
            lineDS.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                    String[] words = s.split(" ");
                    for (String word : words) {
                        Tuple2<String, Integer> wordTuple2 = Tuple2.of(word, 1);
                        collector.collect(wordTuple2);
                    }
                }
            });
        KeyedStream<Tuple2<String, Integer>, String> wordAndOneKS =
            wordAndOne.keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
                public String getKey(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
                    return stringIntegerTuple2.f0;
                }
            });
        SingleOutputStreamOperator<Tuple2<String, Integer>> sumDS = wordAndOneKS.sum(1);
        sumDS.print();
        env.execute();
    }
}
