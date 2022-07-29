package com.cyy.developyment.entity;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.cyy.developyment.service.SshInfoService;
import lombok.Data;
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
public class ProjectInfo implements Serializable {

    private String id;

    private String name;

    private String pomPath;

    private String warName;

    private String localPath;

    //远程服务器根路径
    private String remotePath;

    private String sshId;

    private String createTime;

    private SshInfo ssh;

    public ProjectInfo() {
        this.id = IdUtil.fastSimpleUUID();
        this.createTime = DateUtil.formatDateTime(new Date());
    }

    public void setSshId(String sshId) {
        this.sshId = sshId;
        this.ssh = SshInfoService.getById(sshId);
    }

    public String getLocalWarPath() {
        return pomPath + "/target/" + warName + ".war";
    }

    public String getRemoteBackupsPath() {
        return remotePath + "/backups/";
    }

    public String getRemoteBackupsWarPath() {
        return getRemoteBackupsPath() + warName + ".war";
    }

    public String getRemoteWebappsWarPath() {
        return getRemotePath() + "/webapps/" + warName + ".war";
    }
}
