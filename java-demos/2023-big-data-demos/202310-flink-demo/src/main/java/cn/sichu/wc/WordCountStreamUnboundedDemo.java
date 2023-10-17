package cn.sichu.wc;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author sichu
 * @date 2023/10/17
 **/
public class WordCountStreamUnboundedDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> socketDS = env.socketTextStream("hadoop102", 7777);
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum =
            socketDS.flatMap((String s, Collector<Tuple2<String, Integer>> collector) -> {
                String[] words = s.split(" ");
                for (String word : words) {
                    collector.collect(Tuple2.of(word, 1));
                }
            }).returns(Types.TUPLE(Types.STRING, Types.INT)).keyBy((Tuple2<String, Integer> value) -> value.f0).sum(1);
        sum.print();
        env.execute();
    }
}
