package com.mengnnakk.entry.sign;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


import java.time.LocalDateTime;
@Data
@TableName("sign_record")
public class SignRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String taskId;
    private String studentId;
    private LocalDateTime signTime;
}
