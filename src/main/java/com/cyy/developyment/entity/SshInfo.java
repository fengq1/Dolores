package com.cyy.developyment.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Title
 * @Author fengq1
 * @Version 1.0
 * @Time 2022年07月27日 14:57
 */
@Data
@Accessors(chain = true)
public class SshInfo implements Serializable {

    private String name;

    private String username;

    private String password;

    private String ip;

    private Integer port;

}
