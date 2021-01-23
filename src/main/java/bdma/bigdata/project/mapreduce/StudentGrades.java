package bdma.bigdata.project.mapreduce;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class StudentGrades extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(HBaseConfiguration.create(), new StudentGrades(), args);
        System.exit(exitCode);
    }

    public int run(String[] args) throws Exception {

        String tableName = "21907361:S";
        Scan scan = new Scan();
        Job job = Job.getInstance(getConf(), getClass().getSimpleName());
        job.setJarByClass(getClass());
        TableMapReduceUtil.initTableMapperJob(tableName, scan, RowCounterMapper.class, ImmutableBytesWritable.class,
                Result.class, job);
        FileOutputFormat.setOutputPath(job, new Path(args[0]));
        job.setReducerClass(StudentGradesReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        return job.waitForCompletion(true) ? 0 : 1;
    }

    static class RowCounterMapper extends TableMapper<ImmutableBytesWritable, Result> {

        public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
            context.write(new ImmutableBytesWritable(row), value);
        }

    }

    static class StudentGradesReducer extends Reducer<ImmutableBytesWritable, Result, Text, Text> {
        public void reduce(ImmutableBytesWritable key, Iterable<Result> values, Context context) throws IOException, InterruptedException {
            for(Result result : values){
                String row_key = Bytes.toString(key.get());
                String firstName = Bytes.toString(result.getValue(Bytes.toBytes("#"), Bytes.toBytes("F")));
                String lastName = Bytes.toString(result.getValue(Bytes.toBytes("#"), Bytes.toBytes("L")));
                String program = Bytes.toString(result.getValue(Bytes.toBytes("#"), Bytes.toBytes("P")));
                String email = Bytes.toString(result.getValue(Bytes.toBytes("C"), Bytes.toBytes("E")));
                String value = firstName+" "+lastName+" "+email+ " "+program;
                context.write(new Text(row_key), new Text(value));
            }
        }
    }
}
