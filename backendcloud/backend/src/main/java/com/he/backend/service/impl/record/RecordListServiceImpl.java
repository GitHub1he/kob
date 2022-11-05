package com.he.backend.service.impl.record;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.he.backend.mapper.RecordMapper;
import com.he.backend.mapper.UserMapper;
import com.he.backend.pojo.Record;
import com.he.backend.pojo.User;
import com.he.backend.service.record.RecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RecordListServiceImpl implements RecordListService {
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public JSONObject getList(Integer page) {
        JSONObject res = new JSONObject();

        IPage<Record> recordIPage = new Page<>(page,10);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        List<Record> records = recordMapper.selectPage(recordIPage, queryWrapper).getRecords();

        List<JSONObject> items = new LinkedList<>();
        for (Record record : records) {
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            JSONObject item = new JSONObject();
            item.put("a_photo", userA.getPhoto());
            item.put("a_username", userA.getUsername());
            item.put("b_photo", userB.getPhoto());
            item.put("b_username", userB.getUsername());
            item.put("record", record);
            items.add(item);
        }
        res.put("records", items);
        res.put("records_count", recordMapper.selectCount(null));
        return res;
    }
}
