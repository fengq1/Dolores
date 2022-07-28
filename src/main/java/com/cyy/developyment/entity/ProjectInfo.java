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
public class ProjectInfo implements Serializable {

    private String name;

    private String pomPath;

    private String warName;

    private String localPath;

    private String remotePath;

    private String sshName;

}
