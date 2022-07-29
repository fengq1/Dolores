package com.cyy.developyment.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Title
 * @Author fengq1
 * @Version 1.0
 * @Time 2022年07月29日 10:48
 */
@Data
public class MissionInfo implements Serializable {

    private String name;

    private List<ProjectInfo> projects;

}
