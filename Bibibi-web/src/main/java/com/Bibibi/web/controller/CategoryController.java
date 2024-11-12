package com.Bibibi.web.controller;

import com.Bibibi.entity.po.CategoryInfo;
import com.Bibibi.entity.query.CategoryInfoQuery;
import com.Bibibi.entity.vo.ResponseVO;
import com.Bibibi.exception.BusinessException;
import com.Bibibi.service.CategoryInfoService;
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

    @PostMapping("/loadAllCategory")
    public ResponseVO loadAllCategory() {
        return getSuccessResponseVO(categoryInfoService.getAllCategoryList());
    }

}
