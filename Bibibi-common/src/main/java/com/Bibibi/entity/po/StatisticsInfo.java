package com.Bibibi.entity.po;

import java.io.Serializable;

/**
 * @Description:数据统计
 * @date:2024-12-04
 * @author: liujun
 */
public class StatisticsInfo implements Serializable {
	/**
	 * 统计日期
	 */
	private String statisticsDate;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 数据统计类型
	 */
	private Integer dataType;

	/**
	 * 统计数量
	 */
	private Integer statisticsCount;


	public void setStatisticsDate(String statisticsDate) {
		this.statisticsDate = statisticsDate;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public void setStatisticsCount(Integer statisticsCount) {
		this.statisticsCount = statisticsCount;
	}

	public String getStatisticsDate() {
		return this.statisticsDate;
	}

	public String getUserId() {
		return this.userId;
	}

	public Integer getDataType() {
		return this.dataType;
	}

	public Integer getStatisticsCount() {
		return this.statisticsCount;
	}

	@Override
	public String toString() {
		return "统计日期:" + (statisticsDate == null ? "null" : statisticsDate) + ",用户ID:" + (userId == null ? "null" : userId) + ",数据统计类型:" + (dataType == null ? "null" : dataType) + ",统计数量:" + (statisticsCount == null ? "null" : statisticsCount);
	}
}