package com.auth.util;

import java.util.Objects;
import java.util.regex.Pattern;

/**
一、中国居民身份证号码编码规则：
第一、二位表示省（自治区、直辖市、特别行政区）。 
第三、四位表示市（地级市、自治州、盟及国家直辖市所属市辖区和县的汇总码）。其中，01-20，51-70表示省直辖市；21-50表示地区（自治州、盟）。 
第五、六位表示县（市辖区、县级市、旗）。01-18表示市辖区或地区（自治州、盟）辖县级市；21-80表示县（旗）；81-99表示省直辖县级市。 
第七、十四位表示出生年月日（单数字月日左侧用0补齐）。其中年份用四位数字表示，年、月、日之间不用分隔符。例如：1981年05月11日就用19810511表示。 
第十五、十七位表示顺序码。对同地区、同年、月、日出生的人员编定的顺序号。其中第十七位奇数分给男性，偶数分给女性。 
第十八位表示校验码。作为尾号的校验码，是由号码编制单位按统一的公式计算出来的，校验码如果出现数字10，就用X来代替，详情参考下方计算方法。

其中第一代身份证号码为15位。年份两位数字表示，没有校验码。
前六位详情请参考省市县地区代码
X是罗马字符表示数字10，罗马字符（1-12）：Ⅰ、Ⅱ、Ⅲ、Ⅳ、Ⅴ、Ⅵ、Ⅶ、Ⅷ、Ⅸ、Ⅹ、Ⅺ、Ⅻ……，详情请参考罗马字符


二、中国居民身份证校验码算法步骤如下：

将身份证号码前面的17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7－9－10－5－8－4－2－1－6－3－7－9－10－5－8－4－2。
将这17位数字和系数相乘的结果相加。
用加出来和除以11，取余数。
余数只可能有0－1－2－3－4－5－6－7－8－9－10这11个数字。其分别对应的最后一位身份证的号码为1－0－X－9－8－7－6－5－4－3－2。
通过上面计算得知如果余数是3，第18位的校验码就是9。如果余数是2那么对应的校验码就是X，X实际是罗马数字10。

参考：http://www.ip33.com/shenfenzheng.html


看18位的身份证正则：
[1-9]\d{5}                 前六位地区，非0打头
(18|19|([23]\d))\d{2}      出身年份，覆盖范围为 1800-3999 年
((0[1-9])|(10|11|12))      月份，01-12月
(([0-2][1-9])|10|20|30|31) 日期，01-31天
\d{3}[0-9Xx]：              顺序码三位 + 一位校验码

15位的身份证：
[1-9]\d{5}                  前六位地区，非0打头     
\d{2}                       出生年份后两位00-99
((0[1-9])|(10|11|12))       月份，01-12月
(([0-2][1-9])|10|20|30|31)  日期，01-31天
\d{3}                       顺序码三位，没有校验码

参考：https://segmentfault.com/a/1190000016696368
 *
 */
public class IDcardUtil {

	// 17 位加权因子
    private static final int[] RATIO_ARR = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    // 校验码列表
    private static final char[] CHECK_CODE_LIST = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    
    //身份证号正则表达式
    private static final Pattern IDCARD_PATTERN = Pattern.compile("(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)");
    
	public static boolean checkIDcard(String idcard) {
		if(Objects.isNull(idcard)) {
			return false;
		}
		
		if(!RegExUtil.isMatch(idcard, IDCARD_PATTERN)) {
			return false;
		}
		
		if(idcard.length() == 18) {
			if(check18IDcard(idcard)) {
				return true;
			}
			return false;
		}
		
		return true;
	}
	
	private static boolean check18IDcard(String idcard) {
		if(idcard.length() != 18) {
			return false;
		}
		
		char[] idcards = idcard.toCharArray();
		char checkCode = idcards[17];
		
		int sum = 0;
		for (int i = 0; i < 17; i++) {
			int intValue = Character.getNumericValue(idcards[i]);
			sum = sum + intValue * RATIO_ARR[i];
		}
		
		int remainder = sum % 11;
		return CHECK_CODE_LIST[remainder] == Character.toUpperCase(checkCode);
	}
	
	public static void main(String[] args) {
		System.out.println(IDcardUtil.checkIDcard(""));
	}
}
