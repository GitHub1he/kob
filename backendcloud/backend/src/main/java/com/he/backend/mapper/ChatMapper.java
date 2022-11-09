package com.he.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.he.backend.pojo.Chat;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper extends BaseMapper<Chat> {
}
