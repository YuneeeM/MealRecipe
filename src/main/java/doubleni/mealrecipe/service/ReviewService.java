package doubleni.mealrecipe.service;

import doubleni.mealrecipe.config.exception.BaseException;
import doubleni.mealrecipe.model.*;
import doubleni.mealrecipe.model.DTO.GetReviewRecipeRes;
import doubleni.mealrecipe.model.DTO.GetReviewRes;
import doubleni.mealrecipe.model.Record;
import doubleni.mealrecipe.repository.RecipeRepository;
import doubleni.mealrecipe.repository.ReviewImageRepository;
import doubleni.mealrecipe.repository.ReviewRepository;
import doubleni.mealrecipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static doubleni.mealrecipe.config.exception.BaseResponseStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final ReviewImageRepository reviewImageRepository;

    //리뷰 작성
    public GetReviewRes postReview (String reviewContext, Double reviewRating, Long recipeId , MultipartFile imageFile, Long userId) throws BaseException {
        try{
            Optional<User> userOptional = userRepository.findById(userId);
            Optional<Recipe> recipeOptional = recipeRepository.findByRcpId(recipeId);

            if (userOptional.isPresent() && recipeOptional.isPresent()) {
                User user = userOptional.get();
                Recipe recipe = recipeOptional.get();

                Optional<Review> reviewOptional = reviewRepository.findByUserAndRecipe(user,recipe);
                if (reviewOptional.isPresent()){
                    throw new BaseException(REVIEW_ALREADY_EXISTS);
                }

                Review review = new Review();
                review.setReviewContext(reviewContext);
                review.setReviewRating(reviewRating);
                review.setReviewCreated(new Timestamp(System.currentTimeMillis()));
                review.setUser(user);
                review.setRecipe(recipe);



                if(imageFile != null){

                    // 이미지 파일 저장 경로
                    String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\templates\\image\\";
                    UUID uuid = UUID.randomUUID();
                    String originalFileName = uuid + "_" + imageFile.getOriginalFilename();
                    File saveFile = new File(projectPath +originalFileName);

                    // 이미지 URL 정보를 리스트에 추가
                    String imageUrl = "http://localhost:8080/review/images/" + originalFileName;

                    try {
                        imageFile.transferTo(saveFile);
                        ReviewImage reviewImage = new ReviewImage();
                        reviewImage.setImgName(originalFileName);
                        reviewImage.setImgOriName(imageFile.getOriginalFilename());
                        reviewImage.setImgPath(saveFile.getAbsolutePath());
                        reviewImage.setReview(review);
                        reviewImageRepository.save(reviewImage);

                        review.setReviewImageUrl(imageUrl);

                    } catch (IOException e){
                        throw new RuntimeException("이미지 저장에 실패하였습니다.", e);
                    }

                }

                reviewRepository.save(review);

                GetReviewRes getReviewRes = new GetReviewRes();
                getReviewRes.setReviewId(review.getReviewId());
                getReviewRes.setUserId(review.getUser().getId());
                getReviewRes.setRecipeId(review.getRecipe().getRcpId());
                getReviewRes.setReviewContext(review.getReviewContext());
                getReviewRes.setReviewImageUrl(review.getReviewImageUrl());
                getReviewRes.setReviewRating(review.getReviewRating());
                getReviewRes.setReviewCreated(review.getReviewCreated());
                getReviewRes.setReviewModified(review.getModifiedDate());
                getReviewRes.setNickName(review.getUser().getNickname());
                getReviewRes.setRecipeName(review.getRecipe().getRcpNm());

                return getReviewRes;

            } else {
                throw new BaseException(USERS_EMPTY_USER_ID);
            }
        } catch (BaseException exception){
            if(exception.getStatus().equals(REVIEW_ALREADY_EXISTS)){
                throw exception;
            } else if (exception.getStatus().equals(USERS_EMPTY_USER_ID)) {
                throw exception;
            } else {
                throw new BaseException(POST_REVIEWS_FAILS);
            }
        }
    }



    //reviewId 조회
    public GetReviewRecipeRes getReviewId (Long reviewId) throws BaseException{
        try{
            Optional<Review> reviewOptional = reviewRepository.findByReviewId(reviewId);
            if (reviewOptional.isPresent()){
                Review review = reviewOptional.get();

                GetReviewRecipeRes getReviewRes = new GetReviewRecipeRes();
                getReviewRes.setReviewId(review.getReviewId());
                getReviewRes.setUserId(review.getUser().getId());
                getReviewRes.setRecipeId(review.getRecipe().getRcpId());
                getReviewRes.setReviewContext(review.getReviewContext());
                getReviewRes.setReviewImageUrl(review.getReviewImageUrl());
                getReviewRes.setReviewRating(review.getReviewRating());
                getReviewRes.setReviewCreated(review.getReviewCreated());
                getReviewRes.setReviewModified(review.getModifiedDate());
                getReviewRes.setNickName(review.getUser().getNickname());
                getReviewRes.setRecipeName(review.getRecipe().getRcpNm());
                getReviewRes.setReviewAverage(review.getRecipe().getAverageRating());

                return getReviewRes;
            }
            else {
                throw new BaseException(REVIEW_NO_EXISTS);
            }
        } catch (BaseException exception){
            if(exception.getStatus().equals(REVIEW_NO_EXISTS)){
                throw exception;
            }
            else{
                throw new BaseException(DATABASE_ERROR);
            }
        }
    }




    //reviewId 삭제
    public void ReviewIdDelete (Long reviewId) throws BaseException{
        try{
            Optional<Review> reviewOptional = reviewRepository.findByReviewId(reviewId);
            if (reviewOptional.isPresent()){
                Review review = reviewOptional.get();

                reviewRepository.delete(review);

            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //reviewId 의 이미지 삭제
    public void ReviewIdDeleteImage (Long reviewId) throws BaseException{
        try{
            Optional<Review> reviewOptional = reviewRepository.findByReviewId(reviewId);
            if (reviewOptional.isPresent()){
                Review review = reviewOptional.get();

                Optional<ReviewImage> imageOptional = reviewImageRepository.findByReview(review);
                if (imageOptional.isPresent()){
                    ReviewImage reviewImage = imageOptional.get();
                    reviewImageRepository.delete(reviewImage);
                }
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 사용자가 작성한 리뷰 조회
    public List<GetReviewRecipeRes> getReviewByUser(Long userIdx) throws BaseException {
        try {
            Optional<User> userOptional = userRepository.findById(userIdx);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                List<Review> reviewList = reviewRepository.findByUserOrderByReviewRatingDesc(user);
                if (reviewList.size() == 0){
                    throw new BaseException(REVIEW_NO_EXISTS);
                }

                // Review 엔티티를 GetReviewRes로 변환
                List<GetReviewRecipeRes> getReviewResList = new ArrayList<>();
                for (Review review : reviewList) {
                    GetReviewRecipeRes reviewRes = new GetReviewRecipeRes();
                    reviewRes.setReviewId(review.getReviewId());
                    reviewRes.setUserId(review.getUser().getId());
                    reviewRes.setRecipeId(review.getRecipe().getRcpId());
                    reviewRes.setReviewContext(review.getReviewContext());
                    reviewRes.setReviewImageUrl(review.getReviewImageUrl());
                    reviewRes.setReviewRating(review.getReviewRating());
                    reviewRes.setReviewCreated(review.getReviewCreated());
                    reviewRes.setReviewModified(review.getModifiedDate());
                    reviewRes.setNickName(review.getUser().getNickname());
                    reviewRes.setRecipeName(review.getRecipe().getRcpNm());
                    reviewRes.setReviewAverage(review.getRecipe().getAverageRating());

                    getReviewResList.add(reviewRes);
                }

                return getReviewResList;
            } else {
                // 사용자를 찾지 못한 경우 에러 처리
                throw new BaseException(USERS_NOT_EXISTS);
            }
        } catch (BaseException exception) {
            if(exception.getStatus().equals(USERS_NOT_EXISTS)){
                throw exception;
            } else if (exception.getStatus().equals(REVIEW_NO_EXISTS)) {
                throw exception;
            } else {
                throw new BaseException(REVIEW_NO_EXISTS);
            }
        }
    }



    // 레시피별 작성한 리뷰 조회
    public List<GetReviewRes> getReviewByRecipeId(Long rcpId) throws BaseException {
        try {
            Optional<Recipe> recipeOptional = recipeRepository.findByRcpId(rcpId);

            if (recipeOptional.isPresent()) {
                Recipe recipe = recipeOptional.get();
                List<Review> reviewList = reviewRepository.findByRecipeOrderByReviewRatingDesc(recipe);
                if (reviewList.size() == 0){
                    throw new BaseException(REVIEW_NO_EXISTS);
                }

                // Review 엔티티를 GetReviewRes로 변환
                List<GetReviewRes> getReviewResList = new ArrayList<>();
                for (Review review : reviewList) {
                    GetReviewRes reviewRes = new GetReviewRes();
                    reviewRes.setReviewId(review.getReviewId());
                    reviewRes.setUserId(review.getUser().getId());
                    reviewRes.setRecipeId(review.getRecipe().getRcpId());
                    reviewRes.setReviewContext(review.getReviewContext());
                    reviewRes.setReviewImageUrl(review.getReviewImageUrl());
                    reviewRes.setReviewRating(review.getReviewRating());
                    reviewRes.setReviewCreated(review.getReviewCreated());
                    reviewRes.setReviewModified(review.getModifiedDate());
                    reviewRes.setNickName(review.getUser().getNickname());
                    reviewRes.setRecipeName(review.getRecipe().getRcpNm());

                    getReviewResList.add(reviewRes);
                }

                return getReviewResList;
            } else {
                // 레시피를 찾지 못한 경우 에러 처리
                throw new BaseException(RECIPE_NOT_EXISTS);
            }
        } catch (BaseException exception) {
            if(exception.getStatus().equals(RECIPE_NOT_EXISTS)){
                throw exception;
            } else if (exception.getStatus().equals(REVIEW_NO_EXISTS)) {
                throw exception;
            } else {
                throw new BaseException(REVIEW_NO_EXISTS);
            }
        }
    }



    public List<GetReviewRecipeRes> getReviewByAll() throws BaseException {
        try {
                List<Review> reviewList = reviewRepository.findAllByOrderByReviewRatingDesc();

                // Review 엔티티를 GetReviewRes로 변환
                List<GetReviewRecipeRes> getReviewResList = new ArrayList<>();
                for (Review review : reviewList) {
                    GetReviewRecipeRes reviewRes = new GetReviewRecipeRes();
                    reviewRes.setReviewId(review.getReviewId());
                    reviewRes.setUserId(review.getUser().getId());
                    reviewRes.setRecipeId(review.getRecipe().getRcpId());
                    reviewRes.setReviewContext(review.getReviewContext());
                    reviewRes.setReviewImageUrl(review.getReviewImageUrl());
                    reviewRes.setReviewRating(review.getReviewRating());
                    reviewRes.setReviewCreated(review.getReviewCreated());
                    reviewRes.setReviewModified(review.getModifiedDate());
                    reviewRes.setNickName(review.getUser().getNickname());
                    reviewRes.setRecipeName(review.getRecipe().getRcpNm());
                    reviewRes.setReviewAverage(review.getRecipe().getAverageRating());


                    getReviewResList.add(reviewRes);
                }

                return getReviewResList;

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    //리뷰 수정
    public GetReviewRes reviewfixinfo(String reviewContext, Double reviewRating, MultipartFile imageFile, Long reviewId) throws BaseException {
        try {
            Optional<Review> reviewOptional = reviewRepository.findByReviewId(reviewId);

            if (reviewOptional.isPresent()) {
                Review review = reviewOptional.get();

                if (reviewContext != null) {
                    review.setReviewContext(reviewContext);
                }

                if (reviewRating != 0.0) {
                    review.setReviewRating(reviewRating);
                }

                if (imageFile != null) {

                    // 이미지 파일 저장 경로
                    String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\templates\\image\\";
                    UUID uuid = UUID.randomUUID();
                    String originalFileName = uuid + "_" + imageFile.getOriginalFilename();
                    File saveFile = new File(projectPath + originalFileName);

                    // 이미지 URL 정보를 리스트에 추가
                    String imageUrl = "http://localhost:8080/review/images/" + originalFileName;

                    try {
                        Optional<ReviewImage> existingImage = reviewImageRepository.findByReview(review);
                        if (existingImage.isPresent()) {
                            ReviewImage image = existingImage.get();
                            imageFile.transferTo(saveFile);
                            image.setImgName(originalFileName);
                            image.setImgOriName(imageFile.getOriginalFilename());
                            image.setImgPath(saveFile.getAbsolutePath());
                            image.setReview(review);
                            reviewImageRepository.save(image);
                        } else {
                            imageFile.transferTo(saveFile);
                            ReviewImage reviewImage = new ReviewImage();
                            reviewImage.setImgName(originalFileName);
                            reviewImage.setImgOriName(imageFile.getOriginalFilename());
                            reviewImage.setImgPath(saveFile.getAbsolutePath());
                            reviewImage.setReview(review);
                            reviewImageRepository.save(reviewImage);
                        }

                        review.setReviewImageUrl(imageUrl);


                    } catch (IOException e) {
                        throw new RuntimeException("이미지 저장에 실패하였습니다.", e);
                    }
                }

                review.setModifiedDate(new Timestamp(System.currentTimeMillis()));

                reviewRepository.save(review);


                GetReviewRes getReviewRes = new GetReviewRes();
                getReviewRes.setReviewId(review.getReviewId());
                getReviewRes.setUserId(review.getUser().getId());
                getReviewRes.setRecipeId(review.getRecipe().getRcpId());
                getReviewRes.setReviewContext(review.getReviewContext());
                getReviewRes.setReviewImageUrl(review.getReviewImageUrl());
                getReviewRes.setReviewRating(review.getReviewRating());
                getReviewRes.setReviewCreated(review.getReviewCreated());
                getReviewRes.setReviewModified(review.getModifiedDate());
                getReviewRes.setNickName(review.getUser().getNickname());
                getReviewRes.setRecipeName(review.getRecipe().getRcpNm());

                return getReviewRes;


            } else {
                throw new BaseException(UPDATE_FAIL_FILES);
            }
        } catch (BaseException exception){
            if(exception.getStatus().equals(UPDATE_FAIL_FILES)){
                throw exception;
            } else {
                throw new BaseException(DATABASE_ERROR);
            }
        }

    }


}
