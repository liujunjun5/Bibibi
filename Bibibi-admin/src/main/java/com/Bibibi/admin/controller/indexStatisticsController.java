package com.Bibibi.admin.controller;

import com.Bibibi.entity.constants.Constants;
import com.Bibibi.entity.po.StatisticsInfo;
import com.Bibibi.entity.query.StatisticsInfoQuery;
import com.Bibibi.entity.query.UserInfoQuery;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.enums.StatisticsTypeEnum;
import com.Bibibi.service.StatisticsInfoService;
import com.Bibibi.service.UserInfoService;
import com.Bibibi.utils.DateUtils;
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

@RequestMapping("/index")
@RestController
@Validated
@Slf4j
public class indexStatisticsController extends ABaseController {

    @Resource
    private StatisticsInfoService statisticsInfoService;

    @Resource
    private UserInfoService userInfoService;

    @RequestMapping("/getActualTimeStatisticsInfo")
    public ResponseVO getActualTimeStatisticsInfo() {
        String preDate = DateUtils.getBeforeDayDate(Constants.ONE);
        StatisticsInfoQuery statisticsInfoQuery = new StatisticsInfoQuery();
        statisticsInfoQuery.setStatisticsDate(preDate);
        List<StatisticsInfo> preDayData = statisticsInfoService.findListTotalInfoByParam(statisticsInfoQuery);

        Integer userCount = userInfoService.findCountByParam(new UserInfoQuery());
        preDayData.forEach(item -> {
            if (StatisticsTypeEnum.FANS.getType().equals(item.getDataType())) {
                item.setStatisticsCount(userCount);
            }
        });

        Map<Integer, Integer> preDayDataMap = preDayData.stream().collect(Collectors.toMap(StatisticsInfo::getDataType, StatisticsInfo::getStatisticsCount,
                (item1, item2) -> item2));

        Map<String, Integer> totalCountInfo = statisticsInfoService.getStatisticsInfoActualTime(null);

        Map<String, Object> result = new HashMap<>();
        result.put("totalCountInfo", totalCountInfo);
        result.put("preDayData", preDayDataMap);
        return getSuccessResponseVO(result);
    }

    @RequestMapping("/getWeekStatisticsInfo")
    public ResponseVO getWeekStatisticsInfo(Integer dataType) {
        List<String> dateList = DateUtils.getBeforeDates(7);

        List<StatisticsInfo> statisticsInfoList = new ArrayList<>();
        StatisticsInfoQuery param = new StatisticsInfoQuery();
        param.setDataType(dataType);
        param.setStatisticsDateStart(dateList.get(0));
        param.setStatisticsDateEnd(dateList.get(dateList.size() - 1));
        param.setOrderBy("statistics_date asc");

        if (!StatisticsTypeEnum.FANS.getType().equals(dataType)) {
            statisticsInfoList = statisticsInfoService.findListTotalInfoByParam(param);
        } else {
            statisticsInfoList = statisticsInfoService.findUserCountTotalInfoByParam(param);
        }

        Map<String, StatisticsInfo> dataMap = statisticsInfoList.stream().collect(Collectors.toMap(item -> item.getStatisticsDate(), Function.identity(), (data1,
                                                                                                                                                           data2) -> data2));
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
