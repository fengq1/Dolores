package com.cyy.developyment.entity;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Title
 * @Author fengq1
 * @Version 1.0
 * @Time 2022年07月27日 14:57
 */
@Data
@Accessors(chain = true)
public class SshInfo implements Serializable {


    private String id;

    //是否跳板机
    private boolean jump;

    private String jumpIp;

    private Integer jumpPort;

    private String jumpUsername;

    private String jumpPassword;

    private String name;

    private String username;

    private String password;

    private String ip;

    private Integer port;

    private String createTime;

    public SshInfo() {
        this.id = IdUtil.fastSimpleUUID();
        this.createTime = DateUtil.formatDateTime(new Date());
    }

    public String getInfo() {
        return name + "," + (jump ? jumpIp + " - " + ip : ip);
    }
}
