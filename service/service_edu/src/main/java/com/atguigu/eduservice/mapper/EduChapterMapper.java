package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-05-29
 */
@Mapper
@Repository
public interface EduChapterMapper extends BaseMapper<EduChapter> {

    int create(EduChapter eduChapter);
}
