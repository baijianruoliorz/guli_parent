package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-05-29
 */
@RestController
@RequestMapping("/eduservice/chapter")
@CrossOrigin
public class EduChapterController {
    @Autowired
    private EduChapterService chapterService;

  @Autowired
  private EduChapterMapper eduChapterMapper;

     //课程大纲列表根据id进行查询
    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId) {
        List<ChapterVo> list = chapterService.getChapterVideoByCourseId(courseId);

        return R.ok().data("allChapterVideo", list);
    }

    //添加章节
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter) {
        chapterService.save(eduChapter);
        return R.ok();
    }

    //根据章节id查询
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId) {
        EduChapter eduChapter = chapterService.getById(chapterId);
        return R.ok().data("chapter", eduChapter);
    }

    //修改章节
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter) {
        chapterService.updateById(eduChapter);
        return R.ok();
    }

    //删除的方法
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId) {
        //章节里面有小节：一级分类下面由二级分类
        //有两种实现方式：删章节也删小节，
        //第二种方式：不让章节进行删除，只有先删小节在删章节
        boolean flag = chapterService.deleteChapter(chapterId);

        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }

    }
    @GetMapping("test/getChapter/{id}")
    public R getChapter(@PathVariable String id){
        QueryWrapper<EduChapter> queryWrapper=new QueryWrapper<>();
        queryWrapper.and(i->i.eq("id",id).eq("course_id",14));
        List<EduChapter> list = chapterService.list(queryWrapper);
        return R.ok().data("list",list);
    }
    @GetMapping("test/bgetChapter/{id}")
    public R getB(@PathVariable String id){
        QueryWrapper<EduChapter> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id","15")
                .or()
                .eq("id","1")
                .or()
                .between("id",3,64);
//这里数字或字符串都行!!!("上面
        List<EduChapter> list = chapterService.list(queryWrapper);
        return R.ok().data("list",list);
    }
    @GetMapping("test/bbgetChapter/{id}")
    public R getBB(@PathVariable String id){
        QueryWrapper<EduChapter> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id","15")
                .between("id",3,64)
                .le("id",61);
//        以上等同于and 效果
//这里数字或字符串都行!!!("上面
        List<EduChapter> list = chapterService.list(queryWrapper);
        return R.ok().data("list",list);
    }
//    这里测试以下insert
//    复习seata有感而测
 @GetMapping("/test/insertChapter")
    public R insertTest(EduChapter chapter){
     int i = eduChapterMapper.create(chapter);
    // int insert = eduChapterMapper.insert(chapter); //推荐这种,不需要写ID,会自动生成哒
     System.out.println(i);
     return R.ok();
 }




}

