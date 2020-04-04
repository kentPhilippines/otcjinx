package deal.manage.mapper;


import org.apache.ibatis.annotations.Mapper;

import deal.manage.bean.Log;
@Mapper
public interface LogMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Log record);

    int insertSelective(Log record);

}