package com.msy.plus.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "attachment")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 文件位置
     */
    @Column(name = "location")
    private String location;

    /**
     * image,images
     */
    @Column(name = "category")
    private String category;

    /**
     * 原文件名
     */
    @Column(name = "oriFileName")
    private String orifilename;

    /**
     *  文件路径
     */
    @Column(name = "url")
    private String url;

    /**
     * 创建时间
     */
    @Column(name = "inputTime")
    private Date inputtime;

    /**
     *  创建人 填写为当前登录用户
     */
    @Column(name = "inputUser")
    private Integer inputuser;



}
