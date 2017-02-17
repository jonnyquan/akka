package com.xkeshi.task.handlers;

import com.utils.FileEntry;
import com.utils.FileHandler;
import com.xkeshi.core.utils.CollectionUtils;
import com.xkeshi.task.dtos.ExportTaskDTO;
import com.xkeshi.task.dtos.ImportTaskDTO;
import com.xkeshi.task.enums.PackageMethod;
import com.xkeshi.task.enums.ServiceSupport;
import com.xkeshi.task.utils.FileUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by ruancl@xkeshi.com on 2017/1/10.
 * PI  导入操作时候的参数对象
 * PO  导出操作时候的参数对象
 * R   数据库操作对象
 */
public abstract class AbstractDataProcessor<PI, PO, R> implements DataProcessor {

  private static final Logger logger = LoggerFactory.getLogger(AbstractDataProcessor.class);

  @Autowired
  private FileHandler fileHandler;
  /**
   * 每个文件里面的最大记录条数
   */
  @Value("${single.file.rows}")
  private int SINGLE_FILE_ROWS;
  /**
   * 每次查询的最大记录数
   */
  @Value("${page.size.one.query}")
  private int PAGE_SIZE_ONE_QUERY;

  public AbstractDataProcessor(FileHandler fileHandler) {
    //no spring
    this.fileHandler = fileHandler;
  }

  public AbstractDataProcessor() {
  }

  public abstract ServiceSupport matchServiceSupport();

  protected abstract List<R> selectDb(PO param, int offset, int countSize);

  protected abstract boolean resolveFileDataAndInsertIntoDb(PI importParam, byte[] bytes);

  protected abstract byte[] transferDataToByte(List<R> list);


  private Object writeDbFromFile(ImportTaskDTO<PI> importTaskDTO) {
    HashSet<String> paths = importTaskDTO.getPaths();
    if (CollectionUtils.isEmpty(paths)) {
      throw new NullPointerException("未发现任何可以下载的路径");
    }
    Map<String, InputStream> map = fileHandler.downLoadFiles(paths);
    if (map == null) {
      throw new NullPointerException("找不到可以下载的文件");
    }
    StringBuilder sb = new StringBuilder();
    map.forEach(
        (k, o) -> {
          ByteArrayOutputStream tempArray = new ByteArrayOutputStream();
          try {
            byte[] buff = new byte[1024];
            int rc;
            while ((rc = o.read(buff, 0, 1024)) > 0) {
              tempArray.write(buff, 0, rc);
            }
            //根据各自规则将流转换成自己的对象 并插入数据库
            if (resolveFileDataAndInsertIntoDb(importTaskDTO.getImportParam(),
                tempArray.toByteArray())) {
              sb.append(k).append(" : ").append("ok; ");
            } else {
              sb.append(k).append(" : ").append("error; ");
            }

          } catch (IOException e) {
            logger.error("io流读取异常");
          } finally {
            try {
              o.close();
              tempArray.close();
            } catch (IOException e) {
              logger.error("io流关闭异常");
            }
          }
        });
    return sb.toString();

  }

  /**
   * 每个文件限制记录条数  对每次查询出来的记录数进行切割
   */
  private List<List<R>> cutData(List<R> list, int singleFileRows) {
    if (CollectionUtils.isEmpty(list)) {
      throw new NullPointerException("未查到数据");
    }
    //分割结果 文件切割
    List<List<R>> cutRs = new ArrayList<>();
    int size = list.size();
    if (size > singleFileRows) {
      int mod = cutIntoSlicesCount(size, singleFileRows);

      for (int i = 0; i < mod; i++) {
        cutRs.add(new ArrayList<>(singleFileRows));
      }
      int j = 0;
      for (R r : list) {
        while (cutRs.get(j).size() == singleFileRows) {
          j++;
        }
        cutRs.get(j).add(r);
      }
    } else {
      cutRs.add(list);
    }
    return cutRs;
  }

  private List<String> writeData(String ext, PackageMethod packageExt, boolean ifPackage,
      List<List<R>> cutRs) {
    List<String> paths;
    List<FileEntry> entries = new ArrayList<>();
    if (ifPackage) {
      List<InputStream> inputStreamList = new ArrayList<>();
      cutRs.forEach(rs ->
          inputStreamList.add(new ByteArrayInputStream(transferDataToByte(rs)))
      );
      String path = fileHandler
          .uploadFile(FileUtil.packageFiles(inputStreamList, ext, packageExt.getName()),
              packageExt.getName());
      paths = Arrays.asList(path);
    } else {
      cutRs.forEach(rs ->
          entries.add(new FileEntry(ext, new ByteArrayInputStream(transferDataToByte(rs))))
      );
      paths = fileHandler.uploadFiles(entries);
    }
    return paths;
  }


  private Object writeFileFromDb(ExportTaskDTO<PO> outputTaskDTO) {
    //根据自己的参数定义查询各自数据库
    final int rowCount = outputTaskDTO.getRouCount();
    final int singleFileRows;
    final boolean fileFix = outputTaskDTO.getFileFix();
    if (outputTaskDTO.getRowsMaxOneFile() == null) {
      singleFileRows = SINGLE_FILE_ROWS;
    } else {
      singleFileRows = outputTaskDTO.getRowsMaxOneFile();
    }
    List<Page> pages = null;
    if (rowCount > PAGE_SIZE_ONE_QUERY) {
      pages = cutIntoSlices(rowCount);
    } else {
      pages = Arrays.asList(new Page(0, rowCount));
    }
    final PO param = outputTaskDTO.getParams();
    String ext = outputTaskDTO.getFileExt().getName();
    final PackageMethod packageMethod = outputTaskDTO.getPackageMethod();
    final Boolean multiFilesPac = outputTaskDTO.getMultiFilesPackage();
    List<R> rsList = new ArrayList<>();
    List<String> paths = new ArrayList<>();

    pages.forEach(page -> {
      //文件填满至最大值
      if (!fileFix) {
        paths.addAll(writeData(
            ext,
            packageMethod,
            multiFilesPac,
            cutData(
                selectDb(param, page.offSet, page.pageSize), singleFileRows
            )));

      } else {
        rsList.addAll(selectDb(param, page.offSet, page.pageSize));
      }
    });
    if (fileFix) {
      paths.addAll(writeData(
          ext,
          packageMethod,
          multiFilesPac,
          cutData(
              rsList, singleFileRows
          )));
    }
    StringBuilder sb = new StringBuilder();
    paths.forEach(p -> sb.append(p).append(':'));
    return sb.toString();
  }


  private int cutIntoSlicesCount(int total, int section) {
    return total % section == 0 ? total / section : total / section + 1;
  }

  /**
   * 分页查询切分
   */
  private List<Page> cutIntoSlices(int rowCount) {
    int pageCount = cutIntoSlicesCount(rowCount, PAGE_SIZE_ONE_QUERY);
    List<Page> pages = new ArrayList<>();
    while (pageCount > 0) {
      pageCount--;
      pages.add(new Page(pageCount * PAGE_SIZE_ONE_QUERY, PAGE_SIZE_ONE_QUERY));
    }
    return pages;
  }

  private class Page {

    int offSet;
    int pageSize;

    public Page(int offSet, int pageSize) {
      this.offSet = offSet;
      this.pageSize = pageSize;
    }
  }

  @Override
  public Object handleImport(ImportTaskDTO importTaskDTO) {
    return this.writeDbFromFile(importTaskDTO);
  }

  @Override
  public Object handleExport(ExportTaskDTO outputTaskDTO) {
    return this.writeFileFromDb(outputTaskDTO);
  }

  public static void main(String[] args) {
    //没有填满rs 则不进行上传直到填满
    List<Integer> results = new ArrayList<>();
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    results.add(1);
    int singleSize = 10;
    int listCount = 3;
    List<Integer> arrays = new ArrayList<>();

    for (Integer integer : results) {
      if (arrays.size() < singleSize) {
        arrays.add(integer);
      } else {
        System.out.println("满10条生产一个文件1");
        arrays.clear();//清空list
        listCount--;
      }
    }
    if (listCount == 1) {
      //最后一组可能填不满直接生成文件
      System.out.println("满10条生产一个文件2");
    }
  }

}
