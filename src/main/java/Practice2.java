/**
 * Second Practice
 * Jean-Baptiste Favrot
 * TODO : reducer not good fot this practice
 */
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;



public class Practice2 {
    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable>{

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            String[] data = value.toString().split(";");
            context.write(new Text(Practice2.AgeClassConverter(Integer.parseInt(data[1]))),new IntWritable(Integer.parseInt(data[3])));
        }
    }

    private static String AgeClassConverter(Integer age)
    {
        if(age<5) return "0";
        if(age<12) return "1";
        if(age<17) return "2";
        if(age<25) return "3";
        if(age<35) return "4";
        if(age<45) return "5";
        if(age<60) return "6";
        return "7";
    }

    public static class IntSumReducer
            extends Reducer<Text,IntWritable,Text,IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(Practice2.class);
        job.setMapperClass(Practice2.TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}