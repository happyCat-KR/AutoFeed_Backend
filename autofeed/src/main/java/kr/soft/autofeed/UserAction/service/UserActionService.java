package kr.soft.autofeed.UserAction.service;

import org.springframework.stereotype.Service;

import ch.qos.logback.core.joran.action.Action;
import kr.soft.autofeed.ActionType.dao.ActionTypeRepository;
import kr.soft.autofeed.UserAction.dao.UserActionRepository;
import kr.soft.autofeed.domain.Hashtag;
import kr.soft.autofeed.domain.User;
import kr.soft.autofeed.domain.UserAction;
import kr.soft.autofeed.hashtag.dao.HashtagRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserActionService {

    final private ActionTypeRepository actionTypeRepository;
    final private UserActionRepository userActionRepository;


    public void regist(User user, Hashtag hashtag, String actionTypeCode) {
        UserAction userAction = UserAction.builder()
                    .user(user)
                    .hashtag(hashtag)
                    .actionType(actionTypeRepository.findById(actionTypeCode)
                            .orElseThrow(() -> new IllegalArgumentException("액션타입이 없습니다.")))
                    .delCheck(false)
                    .build();

        userActionRepository.save(userAction);
    }
}
  