package kr.soft.autofeed.util;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class CategoryCrawlerUtil {

    public static List<String> getRelatedWordsFromNaver(String keyword) {
        List<String> results = new ArrayList<>();
        try {
            String url = "https://search.naver.com/search.naver?query=" + keyword;
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0") // 네이버 차단 방지
                    .get();

            Elements elements = doc.select(".lst_related_srch a"); // 연관검색어 영역 (UI 바뀌면 수정 필요)

            for (var element : elements) {
                results.add(element.text());
            }

            results.add(0, keyword); // 원 키워드 포함
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}