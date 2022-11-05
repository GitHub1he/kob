package com.he.backend.service.record;

import com.alibaba.fastjson2.JSONObject;

public interface RecordListService {
    JSONObject getList(Integer page);
}
