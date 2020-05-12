package javaHadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class Exam01_SimpleFileReadWrite {

	public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Usage : Exam01_SimpleFileReadWrite <filename> <contents>");
            System.out.println();
        }
        Configuration conf = new Configuration();
        // Configuration class는 Hadoop의 설정값을 가져온다. 
        // Hadoop xml 설정값들을 확인.
        try {
            FileSystem hdfs = FileSystem.get(conf);
            // FileSystem은 Hadoop의 FileSystem. 
            // Hadoop의 설정값을 보고 파일시스템을 가져온다.
            Path path = new Path(args[0]);
            if (hdfs.exists(path)) {
                hdfs.delete(path, true);
            }
            // 파일 시스템을 가져온후 출력 스트림을 열어서 args[0]이름으로 파일을 생성한다. 
            // 그후 args[1]을 파일 내용으로 저장한다.
            // 그 후 다시 입력 스트림을 열어서 HDFS에서 해당 파일을 가져온 후 그 내용을 출력.
            FSDataOutputStream outStream = hdfs.create(path);
            outStream.writeUTF(args[1]);
            outStream.close();

            FSDataInputStream inputStream = hdfs.open(path);
            String inputString = inputStream.readUTF();
            inputStream.close();
            System.out.println("읽은 내용 : " + inputString);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
