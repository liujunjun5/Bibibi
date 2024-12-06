package com.Bibibi.entity.query;


/**
 * @Description:数据统计查询对象
 * @date:2024-12-04
 * @author: liujun
 */
public class StatisticsInfoQuery extends BaseQuery{
	/**
	 * 统计日期
	 */
	private String statisticsDate;

	private String statisticsDateFuzzy;

	/**
	 * 用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 数据统计类型
	 */
	private Integer dataType;

	/**
	 * 统计数量
	 */
	private Integer statisticsCount;

	public void setStatisticsDateFuzzy(String statisticsDateFuzzy) {
		this.statisticsDateFuzzy = statisticsDateFuzzy;
	}

	public String getStatisticsDateFuzzy() {
		return this.statisticsDateFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy() {
		return this.userIdFuzzy;
	}

	public void setStatisticsDate(String statisticsDate) {
		this.statisticsDate = statisticsDate;
	}

	public String getStatisticsDate() {
		return this.statisticsDate;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Integer getDataType() {
		return this.dataType;
	}

	public void setStatisticsCount(Integer statisticsCount) {
		this.statisticsCount = statisticsCount;
	}

	public Integer getStatisticsCount() {
		return this.statisticsCount;
	}

}