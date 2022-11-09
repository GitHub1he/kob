package com.he.backend.consumerchat.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRecord {
    private Integer sendId;
    private String content;
    private Date sendtime;
}
