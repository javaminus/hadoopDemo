package com.itcast.hdfsdemo;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Before;
import org.junit.Test;

public class HDFS_CRUD {
	FileSystem fs = null;

	@Before
	public void init() throws Exception {
		// 构造一个配置参数对象,设置一个参数：我们要访问的hdfs的URI
		Configuration conf = new Configuration();
		// 这里指定使用的是HDFS文件系统
		conf.set("fs.defaultFS", "hdfs://hadoop01:9000");
		// 通过如下的方式进行客户端身份的设置
		System.setProperty("HADOOP_USER_NAME", "root");
		// 通过FileSystem的静态方法获取文件系统客户端对象
		fs = FileSystem.get(conf);
	}

	@Test
	public void testAddFileToHdfs() throws IOException {
		// 要上传的文件所在本地路径
		Path src = new Path("D:/test.txt");
		// 要上传到hdfs的目标路径
		Path dst = new Path("/testFile");
		// 上传文件方法
		fs.copyFromLocalFile(src, dst);
		// 关闭资源
		fs.close();
	}

	// 从hdfs中复制文件到本地文件系统
	@Test
	public void testDownloadFileToLocal() throws IllegalArgumentException, IOException {
		// 下载文件
		fs.copyToLocalFile(new Path("/testFile"), new Path("D:/"));
	}

	// 创建，删除，重命名文件
	@Test
	public void testMkdirAndDeleteAndRename() throws Exception {
		// 创建目录
		fs.mkdirs(new Path("/a/b/c"));
		fs.mkdirs(new Path("/a2/b2/c2"));
		// 重命名文件或文件夹
		fs.rename(new Path("/a"), new Path("/a3"));
		// 删除文件夹，如果是非空文件夹，参数2必须给值true
		fs.delete(new Path("/a2"), true);
	}

	// 查看目录信息，只显示文件
	@Test
	public void testListFiles() throws FileNotFoundException, IllegalArgumentException, IOException {
		// 获取迭代器对象
		RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);
		while (listFiles.hasNext()) {
			LocatedFileStatus fileStatus = listFiles.next();
			// 打印当前文件名
			System.out.println(fileStatus.getPath().getName());
			// 打印当前文件块大小
			System.out.println(fileStatus.getBlockSize());
			// 打印当前文件权限
			System.out.println(fileStatus.getPermission());
			// 打印当前文件内容长度
			System.out.println(fileStatus.getLen());
			// 获取该文件块信息（包含长度，数据块，datanode的信息）
			BlockLocation[] blockLocations = fileStatus.getBlockLocations();
			for (BlockLocation bl : blockLocations) {
				System.out.println("block-length:" + bl.getLength() + "--" + "block-offset:" + bl.getOffset());
				String[] hosts = bl.getHosts();
				for (String host : hosts) {
					System.out.println(host);
				}
			}
			System.out.println("----------------------------");
		}
	}

	// 查看文件及文件夹信息
	@Test
	public void testListAll() throws FileNotFoundException, IllegalArgumentException, IOException {
		// 获取HDFS系统中文件和目录的元数据等信息
		FileStatus[] listStatus = fs.listStatus(new Path("/"));
		String flag = "d--             ";
		for (FileStatus fstatus : listStatus) {
			// 判断是文件还是文件夹
			if (fstatus.isFile())
				flag = "f--         ";
			System.out.println(flag + fstatus.getPath().getName());
		}
	}

}
