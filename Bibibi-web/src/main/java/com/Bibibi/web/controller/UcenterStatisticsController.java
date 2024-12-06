package com.Bibibi.web.controller;

import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.dto.TokenUserInfoDto;
import com.Bibibi.entity.po.StatisticsInfo;
import com.Bibibi.entity.query.StatisticsInfoQuery;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.service.StatisticsInfoService;
import com.Bibibi.utils.DateUtils;
import com.Bibibi.web.annotation.GlobalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequestMapping("/ucenter")
@RestController
@Validated
@Slf4j
public class UcenterStatisticsController extends ABaseController {

    @Resource
    private StatisticsInfoService statisticsInfoService;

    @RequestMapping("/getActualTimeStatisticsInfo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO getActualTimeStatisticsInfo() {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();
        String preDate = DateUtils.getBeforeDayDate(Constants.ONE);
        StatisticsInfoQuery statisticsInfoQuery = new StatisticsInfoQuery();
        statisticsInfoQuery.setStatisticsDate(preDate);
        statisticsInfoQuery.setUserId(tokenUserInfoDto.getUserId());
        List<StatisticsInfo> preDayData = statisticsInfoService.findListByParam(statisticsInfoQuery);
        Map<Integer, Integer> preDayDataMap = preDayData.stream().collect(Collectors.toMap(StatisticsInfo::getDataType, StatisticsInfo::getStatisticsCount,
                (item1, item2) -> item2));
        Map<String, Integer> totalCountInfo = statisticsInfoService.getStatisticsInfoActualTime(tokenUserInfoDto.getUserId());
        Map<String, Object> result = new HashMap<>();
        result.put("preDayData", preDayDataMap);
        result.put("totalCountInfo", totalCountInfo);
        return getSuccessResponseVO(result);
    }

    @RequestMapping("/getWeekStatisticsInfo")
    @GlobalInterceptor(checkLogin = true)
    public ResponseVO getWeekStatisticsInfo(Integer dataType) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfoDto();

        List<String> dateList = DateUtils.getBeforeDates(7);
        StatisticsInfoQuery statisticsInfoQuery = new StatisticsInfoQuery();
        statisticsInfoQuery.setDataType(dataType);
        statisticsInfoQuery.setUserId(tokenUserInfoDto.getUserId());
        statisticsInfoQuery.setStatisticsDateStart(dateList.get(0));
        statisticsInfoQuery.setStatisticsDateEnd(dateList.get(dateList.size() - 1));
        List<StatisticsInfo> statisticsInfoList = statisticsInfoService.findListByParam(statisticsInfoQuery);

        Map<String, StatisticsInfo> dataMap = statisticsInfoList.stream().collect(Collectors.toMap(item -> item.getStatisticsDate(), Function.identity(), (date1, date2) -> date2));
        List<StatisticsInfo> resultDataList = new ArrayList<>();
        for (String date : dateList) {
            StatisticsInfo dataItem = dataMap.get(date);
            if (dataItem == null) {
                dataItem = new StatisticsInfo();
                dataItem.setStatisticsCount(0);
                dataItem.setStatisticsDate(date);
            }
            resultDataList.add(dataItem);
        }

        return getSuccessResponseVO(resultDataList);
    }
}
