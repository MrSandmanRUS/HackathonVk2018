import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class Load {
    public static void main(String[] args) {
        /*test
        args[0] = "fffe9b1a-7e1b-48a7-b973-15ba4d9ecc89";
        args[1] = "-1.0";
        args[2] = "-1.0";
        args[3] = "0.0";
        args[4] = "-1.0";
        args[5] = "-1.0";
        args[6] = "-1.0";
        args[7] = "-1.0";
        args[8] = "-1.0";
        args[9] = "-1.0";
        args[10] = "-1.0";
        args[11] = "-1.0";*/



        String userId = args[0];
        ArrayList<String> coef = new ArrayList<>();
        for (int count = 1; count < 12; ++count) {
            coef.add(args[count]);
        }
        try {
            // ====== Init HDFS File System Object
            Configuration conf = new Configuration();
            // Set FileSystem URI
            conf.set("fs.defaultFS", "hdfs://localhost:8020/");
            // Because of Maven
            conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
            conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
            // Set HADOOP user
            System.setProperty("HADOOP_USER_NAME", "hadoop");
            System.setProperty("hadoop.home.dir", "/user/hadoop/");
            //Get the filesystem - HDFS
            FileSystem fs = FileSystem.get(URI.create("hdfs://localhost:9000/user/hadoop"), conf);
            // Because of Maven// Because of Maven

            //==== Create folder if not exists
            //Path workingDir=fs.getWorkingDirectory();
            Path newFolderPath= new Path("/user/hadoop/test/");
            if(!fs.exists(newFolderPath)) {
                // Create new Directory
                fs.mkdirs(newFolderPath);
            }

            //==== Write file
            //Create a path
            Path hdfswritepath = new Path(newFolderPath + "/" + userId);
            //Init output stream
            FSDataOutputStream outputStream=fs.create(hdfswritepath);
            //Cassical output stream usage
            String strFile = "0 ";
            for (int count = 0; count < 11; ++count) {
                strFile = strFile + (count + 1) + ":" + coef.get(count) + " ";
            }
            outputStream.writeBytes(strFile);
            outputStream.close();

        } catch (Exception exc) {
            exc.printStackTrace();
        }

        SparkSession spark = SparkSession
                .builder()
                .appName("LoadModel")
                .getOrCreate();


        //String path = "sample_multiclass_classification_data.txt";

        //Dataset<Row> dataFrame = spark.read().format("libsvm").load(path);
        Dataset<Row> dataFrame = spark.read().format("libsvm").load("/user/hadoop/test/" + userId);

        // Split the data into train and test
        //Dataset<Row>[] splits = dataFrame.randomSplit(new double[]{0.6, 0.4}, 1234L);
        //Dataset<Row> train = splits[0];
        //Dataset<Row> test = splits[1];

        MultilayerPerceptronClassificationModel model = MultilayerPerceptronClassificationModel.load("model_big");

        Dataset<Row> result = model.transform(dataFrame);
        //Dataset<Row> predictionAndLabels = result.select("prediction", "label");
        Dataset<Row> prediction = result.select("prediction");

        //MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
         //       .setMetricName("accuracy");

        //System.out.println("Test set accuracy = " + evaluator.evaluate(predictionAndLabels));

        //List<String> listOne = predictionAndLabels.as(Encoders.STRING()).collectAsList();
        //System.out.println(predictionAndLabels.toString());
        List<String> listTwo = prediction.map(row -> row.mkString(), Encoders.STRING()).collectAsList();
        String predictRes = "";
        for (String str : listTwo) {
            predictRes = str;
            System.out.println(str);
        }
        System.out.println("RESULT = " + predictRes);
        //spark.close();
        /*try {
            Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
            Connection connection = DriverManager.getConnection("jdbc:phoenix:localhost:2181/hbase");
            PreparedStatement ps = connection.prepareStatement("upsert into SUSER(ID, MAINTAG, COEF) values (?,?,?)");
            int i = 1;
            ps.setString(i++, userId);
            ps.setString(i++, predictRes);
            ps.setArray(i++, connection.createArrayOf("VARCHAR", coef.toArray(new String[0])));

            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (Exception exc) {
            exc.printStackTrace();
        }*/
        //System.out.println(listTwo);
    }


}
