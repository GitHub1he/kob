package com.he.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.he.backend.pojo.Posts;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostsMapper extends BaseMapper<Posts> {
}
