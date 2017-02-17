package com.xkeshi.task.services.compnents.command.remote.ons;

import com.xkeshi.task.utils.FileUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ruancl@xkeshi.com on 2017/2/15.
 */
public class SerializeUtil {

  private static final Logger logger = LoggerFactory.getLogger(SerializeUtil.class);


  public static byte[] getbytesFromObject(Object o){
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    byte[] bytes = null;
    ObjectOutputStream objectOutputStream = null;
    try {
      objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      objectOutputStream.writeObject(o);
      bytes = byteArrayOutputStream.toByteArray();


    } catch (IOException e) {
      logger.error("io exception");
    }finally {
      FileUtil.closeOutPutStream(objectOutputStream,byteArrayOutputStream);
    }

    return bytes;
  }

  public static Object getObjectFromBytes(byte[] bytes){
    Object o = null;
    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
    ObjectInputStream objectInputStream = null;
    try {
      objectInputStream = new ObjectInputStream(inputStream);
      o = objectInputStream.readObject();
    } catch (IOException e) {
      logger.error("io exception");
    } catch (ClassNotFoundException e) {
      logger.error("ClassNotFoundException");
    }finally {
      FileUtil.closeInPutStream(inputStream,objectInputStream);
    }
    return o;
  }

}
