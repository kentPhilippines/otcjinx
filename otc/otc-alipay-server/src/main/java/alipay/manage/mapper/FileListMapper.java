package alipay.manage.mapper;

import alipay.manage.bean.FileList;
import alipay.manage.bean.FileListExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FileListMapper {
    int countByExample(FileListExample example);

    int deleteByExample(FileListExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FileList record);

    int insertSelective(FileList record);

    List<FileList> selectByExampleWithBLOBs(FileListExample example);

    List<FileList> selectByExample(FileListExample example);

    FileList selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FileList record, @Param("example") FileListExample example);

    int updateByExampleWithBLOBs(@Param("record") FileList record, @Param("example") FileListExample example);

    int updateByExample(@Param("record") FileList record, @Param("example") FileListExample example);

    int updateByPrimaryKeySelective(FileList record);

    int updateByPrimaryKeyWithBLOBs(FileList record);

    int updateByPrimaryKey(FileList record);
}