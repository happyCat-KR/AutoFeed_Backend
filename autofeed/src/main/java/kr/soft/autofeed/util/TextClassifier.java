package kr.soft.autofeed.util;

import java.util.*;


/***
 * 카테고리 구분 기능
 */
public class TextClassifier {

    private static final Map<String, List<String>> categories = new HashMap<>();

    static {
        categories.put("음식", Arrays.asList("음식", "밥", "점심", "저녁", "아침", "식사", "한식", "중식", "일식", "양식", "패스트푸드", "치킨", "피자", "햄버거", "라면"));
        categories.put("운동", Arrays.asList("운동", "헬스", "헬스장", "러닝", "달리기", "마라톤", "요가", "필라테스", "수영", "자전거"));
        categories.put("여행", Arrays.asList("여행", "관광", "여행지", "비행기", "기차", "버스", "배", "항공권", "비자", "여권"));
        categories.put("영화감상", Arrays.asList("영화", "관람", "시사회", "예매", "팝콘", "배우", "감독", "장르", "드라마", "코미디"));
        categories.put("쇼핑", Arrays.asList("쇼핑", "구매", "온라인쇼핑", "오프라인", "백화점", "마트", "할인", "세일", "쿠폰", "적립금"));
    }


    

    public static String classifyText(String sentence) {
        Map<String, Integer> scores = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : categories.entrySet()) {
            String category = entry.getKey();
            int score = 0;
            for (String keyword : entry.getValue()) {
                if (sentence.contains(keyword)) {
                    score++;
                }
            }
            scores.put(category, score);
        }

        String bestCategory = "분류불가";
        int maxScore = 0;

        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                bestCategory = entry.getKey();
            }
        }

        return maxScore == 0 ? "분류불가" : bestCategory;
    }
}
