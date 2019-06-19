package com.auth.realname.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.auth.realname.bean.RealName;

@Mapper
public interface RealNameMapper {

	@Select("SELECT * FROM xw_realname_auth WHERE idcard=#{idcard}")
	RealName selectRealNameByIDcard(String idcard);
	
	/**
	 * 库中存在记录会返回0
	 */
	@Insert("INSERT IGNORE INTO xw_realname_auth(idcard,name,status,update_time,create_time) "
			+ "VALUES(#{idcard},#{name},#{status},#{updateTime},#{createTime});")
	//@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int insertRealName(RealName realname);
	
	@Insert("INSERT INTO xw_realname_auth(idcard,name,status,update_time,create_time) "
			+ "VALUES(#{idcard},#{name},#{status},#{updateTime},#{createTime}) "
			+ "ON DUPLICATE KEY UPDATE name=#{name},status=#{status},update_time=#{updateTime}")
	int insertOrUpdateRealName(RealName realname);
	
	@Update("<script>"
			+ "UPDATE xw_realname_auth SET name=#{name},"
			+ "<if test='sex!=null'>sex=#{sex},</if>"
			+ "<if test='birthday!=null'>birthday=#{birthday},</if>"
			+ "<if test='area!=null'>area=#{area},</if>"
			+ "status=#{status},update_time=#{updateTime} WHERE idcard=#{idcard}"
			+ "</script>")
	int updateRealNameByIDcard(RealName realname);
	
	@Update("UPDATE xw_realname_auth SET status=#{status},update_time=#{updateTime} WHERE idcard=#{idcard}")
	int updateStatusByIDcard(RealName realname);
}
