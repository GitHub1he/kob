package com.he.backend.service.ranklist;

import com.alibaba.fastjson2.JSONObject;

public interface RankListService {
    JSONObject getList(Integer page);
}
