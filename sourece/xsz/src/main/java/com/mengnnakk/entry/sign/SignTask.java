package com.mengnnakk.entry.sign;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@TableName("sign_task")
public class SignTask {
    @TableId(type = IdType.ASSIGN_UUID)
    private String taskId;

    private String code;
    private String status; // OPEN、CLOSED
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
