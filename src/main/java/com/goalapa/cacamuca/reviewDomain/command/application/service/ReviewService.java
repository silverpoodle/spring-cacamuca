package com.goalapa.cacamuca.reviewDomain.command.application.service;


import com.goalapa.cacamuca.likeDomain.command.domain.aggregate.entity.Like;
import com.goalapa.cacamuca.likeDomain.command.domain.repository.LikeRepository;
import com.goalapa.cacamuca.reportDomain.command.domain.aggregate.entity.Report;
import com.goalapa.cacamuca.reportDomain.command.domain.aggregate.vo.ReportMemberVO;
import com.goalapa.cacamuca.reportDomain.command.domain.aggregate.vo.ReportedMemberVO;
import com.goalapa.cacamuca.reportDomain.command.domain.aggregate.vo.ReviewVO;
import com.goalapa.cacamuca.reportDomain.command.domain.repository.ReportRepository;
import com.goalapa.cacamuca.reviewDomain.command.application.dto.ReviewDTO;
import com.goalapa.cacamuca.reviewDomain.command.domain.aggregate.entity.Review;
import com.goalapa.cacamuca.reviewDomain.command.domain.aggregate.entity.ReviewPic;
import com.goalapa.cacamuca.reviewDomain.command.domain.aggregate.vo.ReviewWriter;
import com.goalapa.cacamuca.reviewDomain.command.domain.repository.ReviewPicRepository;
import com.goalapa.cacamuca.reviewDomain.command.domain.repository.ReviewRepository;
import com.goalapa.cacamuca.reviewDomain.command.infrastructure.service.ReviewValidationServiceImpl;
import com.goalapa.cacamuca.reviewDomain.query.application.service.SelectReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;


@Service
public class ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;
    private final ReviewPicRepository reviewPicRepository;
    private final SelectReviewService selectReviewService;
    private final ReviewValidationServiceImpl reviewValidationService;
    private final ReportRepository reportRepository;

    private static String root = "C:\\app-file";
    private static String filePath = root + "/uploadFiles";

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, LikeRepository likeRepository, ReviewPicRepository reviewPicRepository, SelectReviewService selectReviewService, ReviewValidationServiceImpl reviewValidationService, ReportRepository reportRepository) {
        this.reviewRepository = reviewRepository;
        this.likeRepository = likeRepository;
        this.reviewPicRepository = reviewPicRepository;
        this.selectReviewService = selectReviewService;
        this.reviewValidationService = reviewValidationService;
        this.reportRepository = reportRepository;
    }

    @Transactional
    public void saveReview(ReviewDTO reviewDTO, int loginMemberNo) {
        LocalDate date = LocalDate.now();
        ReviewWriter reviewWriter = new ReviewWriter(loginMemberNo);
        Review review = new Review(reviewDTO.getReviewContent(), reviewDTO.getCountry(), reviewDTO.getFoodType(), reviewDTO.getFoodName(), date, reviewDTO.getReviewRate(), reviewWriter, reviewDTO.getFoodNo()
                , reviewDTO.getReviewKeyword(), reviewDTO.getReviewPrice(), reviewDTO.getReviewLink(), 0, 0);

        System.out.println("review = " + review);

        reviewRepository.save(review);
    }

    @Transactional
    public void saveReview(ReviewDTO reviewDTO, List<MultipartFile> reviewPicUrl, int loginMemberNo) {
        LocalDate date = LocalDate.now();
        ReviewWriter reviewWriter = new ReviewWriter(loginMemberNo);
        Review review = new Review(reviewDTO.getReviewContent(), reviewDTO.getCountry(), reviewDTO.getFoodType(), reviewDTO.getFoodName(), date, reviewDTO.getReviewRate(), reviewWriter, reviewDTO.getFoodNo()
                , reviewDTO.getReviewKeyword(), reviewDTO.getReviewPrice(), reviewDTO.getReviewLink(), 0, 0);

        List<String> fileNames = new ArrayList<>();
        ReviewPic reviewPic = new ReviewPic();

        for (MultipartFile file : reviewPicUrl) {
            if (file.isEmpty()) {
                continue;
            }

            String fileName = StringUtils.cleanPath(file.getOriginalFilename());

            try {
                String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;;
                File uploadDir = new File(filePath);

                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String uploadPath = filePath + File.separator + uniqueFileName;
                file.transferTo(new File(uploadPath));

                reviewPic = new ReviewPic(uniqueFileName);
                fileNames.add(uploadPath);

                reviewPic.setReviewNo(review);

                reviewPicRepository.save(reviewPic);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    public void countHeart(Integer no, int loginMemberNo) {
        Review review = reviewRepository.findById(no).get();

        boolean checkHeartCondition = reviewValidationService.checkHeartCondition(review, no, loginMemberNo);

        if(checkHeartCondition == true){
            Like like = new Like();

            like.setReviewNo(no);
            like.setMemberNo(loginMemberNo);

            review.setLikeCnt(review.getLikeCnt() + 1);
            likeRepository.save(like);
        }
    }

    @Transactional
    public boolean countReport(int reportReason, int reviewNo, int loginMemberNo, int memberNo) {
        Review review = reviewRepository.findById(reviewNo).get();
        ReviewVO reviewVO = new ReviewVO(reviewNo);
        ReportMemberVO reportMemberVO = new ReportMemberVO(loginMemberNo);
        ReportedMemberVO reportedMemberVO = new ReportedMemberVO(memberNo);

        boolean checkCondition = reviewValidationService.checkReportCondition(review, reviewVO, reportMemberVO, reportedMemberVO);

        if (checkCondition==true){
            Report report = new Report(reviewVO, reportMemberVO, reportedMemberVO, reportReason);

//            review.setReportCnt(review.getReportCnt()+1);
            reportRepository.save(report);
        }

        return checkCondition;
    }
}
