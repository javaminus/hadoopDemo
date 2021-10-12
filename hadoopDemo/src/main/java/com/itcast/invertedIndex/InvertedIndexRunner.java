package com.itcast.invertedIndex;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.junit.Test;

import java.io.IOException;


public class InvertedIndexRunner {
    @Test
    public void Test() throws IOException, ClassNotFoundException, InterruptedException {
        Configuration configuration=new Configuration();
        Job job=Job.getInstance(configuration);
        job.setJarByClass(InvertedIndexRunner.class);

        job.setMapperClass(InvertedIndexMapper.class);
        job.setCombinerClass(InvertedIndexCombiner.class);
        job.setReducerClass(InvertedIndexReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.setInputPaths(job,new Path("D:\\InvertedIndex\\input"));
        FileOutputFormat.setOutputPath(job,new Path("D:\\InvertedIndex\\output"));

        boolean res=job.waitForCompletion(true);
        System.exit(res?0:1);
    }
}
