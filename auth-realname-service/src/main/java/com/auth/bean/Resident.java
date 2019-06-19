package com.auth.bean;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Resident {

	private String idcard;
	private String name;
	private Integer sex;//0：女；1：男
	private String area;
	private String birthday;
}
