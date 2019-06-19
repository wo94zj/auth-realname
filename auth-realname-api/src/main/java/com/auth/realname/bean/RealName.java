package com.auth.realname.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RealName {

	private Integer id;
	private String idcard;//身份证号
	private String name;//名称
	private Integer sex;//女：0；男：1
	private String birthday;
	private String area;
	private Integer status;
	private Long updateTime;
	private Long createTime;
}
