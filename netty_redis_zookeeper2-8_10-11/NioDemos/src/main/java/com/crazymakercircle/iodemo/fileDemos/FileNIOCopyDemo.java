package com.crazymakercircle.iodemo.fileDemos;

import com.crazymakercircle.NioDemoConfig;
import com.crazymakercircle.util.IOUtil;
import com.crazymakercircle.util.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by 尼恩@ 疯创客圈
 */

public class FileNIOCopyDemo {

    /**
     * 演示程序的入口函数
     *
     * @param args
     */
    public static void main(String[] args) {
        //演示复制资源文件
        nioCopyResouceFile();

    }


    /**
     * 复制两个资源目录下的文件
     */
    public static void nioCopyResouceFile() {
        String sourcePath = NioDemoConfig.FILE_RESOURCE_SRC_PATH;
        String srcPath = IOUtil.getResourcePath(sourcePath);
        Logger.debug("srcPath=" + srcPath);

        String destShortPath = NioDemoConfig.FILE_RESOURCE_DEST_PATH;
        String destdePath = IOUtil.builderResourcePath(destShortPath);
        Logger.debug("destdePath=" + destdePath);

        nioCopyFile(srcPath, destdePath);
    }


    /**
     * 复制文件
     *
     * @param srcPath
     * @param destPath
     */
    public static void nioCopyFile(String srcPath, String destPath) {

        File srcFile = new File(srcPath);
        File destFile = new File(destPath);

        try {
            //如果目标文件不存在，则新建
            if (!destFile.exists()) {
                destFile.createNewFile();
            }


            long startTime = System.currentTimeMillis();

            FileInputStream fis = null;
            FileOutputStream fos = null;
            FileChannel inChannel = null;
            FileChannel outchannel = null;
            try {
                fis = new FileInputStream(srcFile);
                fos = new FileOutputStream(destFile);
                inChannel = fis.getChannel();
                outchannel = fos.getChannel();

                int length = -1;
                ByteBuffer buf = ByteBuffer.allocate(1024);
                //从输入通道读取到buf写入到buf，则buf是写入模式
                while ((length = inChannel.read(buf)) != -1) {

                    //第一次切换：翻转buf,变成读取模式
                    buf.flip();

                    int outlength = 0;
                    Logger.debug("清除之前的buf：" + buf);
                    //将buf写入到输出的通道，即buf为读取模式
                    while ((outlength = outchannel.write(buf)) != 0) {
                        System.out.println("写入字节数：" + outlength);
                    }
                    //第二次切换：清除buf,变成写入模式
                    buf.clear();
                    Logger.debug("清除之后的buf：" + buf);
                }


                //强制刷新磁盘
                outchannel.force(true);
            } finally {
                IOUtil.closeQuietly(outchannel);
                IOUtil.closeQuietly(fos);
                IOUtil.closeQuietly(inChannel);
                IOUtil.closeQuietly(fis);
            }
            long endTime = System.currentTimeMillis();
            Logger.debug("base 复制毫秒数：" + (endTime - startTime));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
