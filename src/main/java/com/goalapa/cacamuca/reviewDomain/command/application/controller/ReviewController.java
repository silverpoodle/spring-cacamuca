package com.goalapa.cacamuca.reviewDomain.command.application.controller;

import com.goalapa.cacamuca.reviewDomain.command.application.dto.ReviewDTO;
import com.goalapa.cacamuca.reviewDomain.command.application.dto.ReviewLikeDTO;
import com.goalapa.cacamuca.reviewDomain.command.application.service.ReviewService;
import com.goalapa.cacamuca.reviewDomain.query.application.controller.SelectReviewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/*")
public class ReviewController {
    private final ReviewService reviewService;
    private static final Logger logger = LoggerFactory.getLogger(SelectReviewController.class);

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/review")
    public String reviewPage(){
        return "review";
    }

    @PostMapping("/review")
    public String writeReview(@RequestParam List<MultipartFile> reviewPicUrl, @ModelAttribute ReviewDTO reviewDTO){
        reviewService.saveReview(reviewDTO, reviewPicUrl);

        return "redirect:/selectReviews";
    }

@PostMapping("/reviewDetail")
@ResponseBody
public Map<String, Object> CountHeart(Model model, @RequestBody HashMap<String, Object> parameter){
        String no = (String) parameter.get("no");
        Integer reviewNo =  Integer.parseInt(no);
        Integer memberNo = Integer.parseInt((String) parameter.get("member"));

        reviewService.countHeart(reviewNo, memberNo);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("no", Integer.valueOf(reviewNo).toString());
        responseMap.put("member", memberNo);
        return responseMap;
    }
}
