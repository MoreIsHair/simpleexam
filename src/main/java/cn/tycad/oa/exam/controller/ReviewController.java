package cn.tycad.oa.exam.controller;

import cn.tycad.oa.exam.model.vo.Result;
import cn.tycad.oa.exam.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * @author YY
 * @date 2019/8/1
 * @description
 */
@Api("评价界面")
@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @GetMapping("/getReview")
    @ApiOperation(value = "获取评价排行列表")
    public Result<Map> getReview(@ApiParam(name = "minTime",value = "最小时间")@RequestParam(value = "minTime",required = false) Date min
            , @ApiParam(name = "maxTime",value = "最大时间") @RequestParam(value = "maxTime",required = false) Date max){
        return Result.success(reviewService.getRankList(min,max));
    }
}
