package com.Bibibi.admin.controller;

import com.Bibibi.entity.po.CategoryInfo;
import com.Bibibi.entity.query.CategoryInfoQuery;
import com.Bibibi.entity.query.VideoInfoQuery;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.service.CategoryInfoService;
import com.Bibibi.service.VideoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController extends ABaseController{

    @Resource
    private CategoryInfoService categoryInfoService;

    @Resource
    private VideoInfoService videoInfoService;

    @RequestMapping("/loadCategory")
    public ResponseVO loadCategory(CategoryInfoQuery query) {
        query.setOrderBy("sort asc");
        query.setConvert2Tree(true);
        List<CategoryInfo> categoryInfoList = categoryInfoService.findListByParam(query);
        return getSuccessResponseVO(categoryInfoList);
    }

    /**
     * 讲清楚干嘛的
     * @param pCategoryId 写好各个参数是什么
     * @param categoryId
     * @param categoryCode
     * @param categoryName
     * @param icon
     * @param background
     * @return
     * @throws BusinessException
     */
    @RequestMapping("/saveCategory")
    public ResponseVO saveCategory(@NotNull Integer pCategoryId,
                                   Integer categoryId,
                                   @NotEmpty String categoryCode,
                                   @NotEmpty String categoryName,
                                   String icon,
                                   String background) throws BusinessException {
        CategoryInfo categoryInfo = new CategoryInfo();
        categoryInfo.setCategoryId(categoryId);
        categoryInfo.setCategoryName(categoryName);
        categoryInfo.setPCategoryId(pCategoryId);
        categoryInfo.setIcon(icon);
        categoryInfo.setBackground(background);
        categoryInfo.setCategoryCode(categoryCode);

        categoryInfoService.saveCategory(categoryInfo);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/delCategory")
    public ResponseVO delCategory(@NotNull Integer categoryId) throws BusinessException {
        VideoInfoQuery videoInfoQuery = new VideoInfoQuery();
        videoInfoQuery.setCategoryIdOrPCategoryId(categoryId);
        Integer count = videoInfoService.findCountByParam(videoInfoQuery);
        if (count > 0) {
            throw new BusinessException("分类下有视频信息，无法删除");
        }
        categoryInfoService.delCategory(categoryId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/changeSort")
    public ResponseVO changeSort(@NotNull Integer pCategoryId, @NotEmpty String categoryIds) throws BusinessException {
        categoryInfoService.changeSort(pCategoryId, categoryIds);
        return getSuccessResponseVO(null);
    }
}
