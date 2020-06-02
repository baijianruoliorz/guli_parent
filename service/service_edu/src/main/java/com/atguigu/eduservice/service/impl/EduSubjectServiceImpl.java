//package com.atguigu.eduservice.service.impl;
//
//import com.alibaba.excel.EasyExcel;
//import com.atguigu.eduservice.entity.EduSubject;
//import com.atguigu.eduservice.entity.excel.SubjectData;
//import com.atguigu.eduservice.entity.subject.OneSubject;
//import com.atguigu.eduservice.entity.subject.TwoSubject;
//import com.atguigu.eduservice.listener.SubjectExcelListener;
//import com.atguigu.eduservice.mapper.EduSubjectMapper;
//import com.atguigu.eduservice.service.EduSubjectService;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.fasterxml.jackson.databind.util.BeanUtil;
//import org.springframework.beans.BeanUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.security.auth.Subject;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * <p>
// * 课程科目 服务实现类
// * </p>
// *
// * @author testjava  这货是code generater
// * @since 2020-05-28
// */
//@Service
//public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
//  //添加课程分类
//    @Override
//    public void saveSubject(MultipartFile file,EduSubjectService subjectService) {
//      try {
//        InputStream in=file.getInputStream();
//        EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();
//      }catch (Exception e){
//        e.printStackTrace();
//      }
//    }
//
//
//  //分析： 要返回数组，有一级有二级 ---》1,2，查询所有一级，二级，有数据才能封装
//  //3.4.封装一级二级分类
//  //
//
//  @Override
//  public List<OneSubject> getAllOneTwoSubject() {
//    //表中有parentId=0的就是一级分类，可以查出
//    QueryWrapper<EduSubject> wrapperOne=new QueryWrapper<>();
//    wrapperOne.eq("parent_id","0");
//    //service调用mapper可以直接basemapper,不用注入了！！！
//    List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);
//    //还可以this.list(wrapperOne)
//
//    //二级分类是parent!=0
//    QueryWrapper<EduSubject> wrapperTwo=new QueryWrapper<>();
//    //ne表示不等于
//    wrapperOne.ne("parent_id","0");
//    //service调用mapper可以直接basemapper,不用注入了！！！
//    List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperOne);
//    //上面方法使用与只有二级分类的情况，如果有三级分类，得另找他法
//
//    //创建list集合，用于存贮最终数据
//    List<OneSubject> finalSubjectList=new ArrayList<>();
//
//
//    //封装一级分类
//    //目标是把eduSubject变成OneSubject,查询所有的一级分类都得遍历，得到每一个一级分类对象
//    for(int i=0;i<oneSubjectList.size();i++){
//      //得到每个对象
//      EduSubject eduSubject = oneSubjectList.get(i);
//      //由于这两个对象并不一样，所有要放到oneSubject中有两种做法
//      //1.复杂zuofa
//     /* OneSubject oneSubject=new OneSubject();
//      oneSubject.setId(eduSubject.getId());
//      oneSubject.setTitle(eduSubject.getTitle());
//      finalSubjectList.add(oneSubject);*/
//     //工具类完成的做法,只会找到对应的值来封装，不用担心~~
//      OneSubject oneSubject=new OneSubject();
//      BeanUtils.copyProperties(eduSubject,oneSubject);
//      finalSubjectList.add(oneSubject);
//
//      //在一级分类中嵌套for循环
//      //创建list集合
//      List<TwoSubject> twoFinalSubject=new ArrayList<>();
//      //遍历二级
//      for(int m=0;m<twoSubjectList.size();m++){
//        EduSubject tSubject = twoSubjectList.get(m);
//        //判断二级分类属于哪个一级分类：parentId=id
//        if(tSubject.getParentId().equals(eduSubject.getId())){
//          //把tSubject的值放到twoFinalList里面去
//          TwoSubject twoSubject=new TwoSubject();
//          BeanUtils.copyProperties(tSubject,twoSubject);
//          twoFinalSubject.add(twoSubject);
//        }
//
//      }
//      //把一级分类下面的所有二级分类放到一级分类
//      oneSubject.setChildren(twoFinalSubject);
//    }
//
//    //封装二级分类
//
//
//    //有了所有数据了
//    return finalSubjectList;
//
//  }
//}
package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-28
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

  //添加课程分类
  @Override
  public void saveSubject(MultipartFile file,EduSubjectService subjectService) {
    try {
      //文件输入流
      InputStream in = file.getInputStream();
      //调用方法进行读取
      EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  //课程分类列表（树形）
  @Override
  public List<OneSubject> getAllOneTwoSubject() {
    //1 查询所有一级分类  parentid = 0
    QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
    wrapperOne.eq("parent_id","0");
    List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

    //2 查询所有二级分类  parentid != 0
    QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
    wrapperTwo.ne("parent_id","0");
    List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);

    //创建list集合，用于存储最终封装数据
    List<OneSubject> finalSubjectList = new ArrayList<>();

    //3 封装一级分类
    //查询出来所有的一级分类list集合遍历，得到每个一级分类对象，获取每个一级分类对象值，
    //封装到要求的list集合里面 List<OneSubject> finalSubjectList
    for (int i = 0; i < oneSubjectList.size(); i++) { //遍历oneSubjectList集合
      //得到oneSubjectList每个eduSubject对象
      EduSubject eduSubject = oneSubjectList.get(i);
      //把eduSubject里面值获取出来，放到OneSubject对象里面
      OneSubject oneSubject = new OneSubject();
//            oneSubject.setId(eduSubject.getId());
//            oneSubject.setTitle(eduSubject.getTitle());
      //eduSubject值复制到对应oneSubject对象里面
      BeanUtils.copyProperties(eduSubject,oneSubject);
      //多个OneSubject放到finalSubjectList里面
      finalSubjectList.add(oneSubject);

      //在一级分类循环遍历查询所有的二级分类
      //创建list集合封装每个一级分类的二级分类
      List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
      //遍历二级分类list集合
      for (int m = 0; m < twoSubjectList.size(); m++) {
        //获取每个二级分类
        EduSubject tSubject = twoSubjectList.get(m);
        //判断二级分类parentid和一级分类id是否一样
        if(tSubject.getParentId().equals(eduSubject.getId())) {
          //把tSubject值复制到TwoSubject里面，放到twoFinalSubjectList里面
          TwoSubject twoSubject = new TwoSubject();
          BeanUtils.copyProperties(tSubject,twoSubject);
          twoFinalSubjectList.add(twoSubject);
        }
      }
      //把一级下面所有二级分类放到一级分类里面
      oneSubject.setChildren(twoFinalSubjectList);
    }
    return finalSubjectList;
  }
}
