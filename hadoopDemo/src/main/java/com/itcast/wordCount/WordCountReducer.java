package com.itcast.wordCount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WordCountReducer extends Reducer<Text, IntWritable,Text,IntWritable> {
    @Override
    protected void reduce(Text key,Iterable<IntWritable>value,Reducer<Text,IntWritable,Text,IntWritable>.Context context) throws IOException, InterruptedException {
        int count=0;
        for (IntWritable intWritable : value) {
            count+=intWritable.get();
        }
        context.write(key,new IntWritable(count));
    }
}
