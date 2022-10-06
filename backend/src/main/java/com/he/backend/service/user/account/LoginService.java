package com.he.backend.service.user.account;

import java.util.Map;

public interface LoginService {
    public Map<String,String> gettoken(String username, String password);
}
