package com.obss.grapnel;

import com.obss.grapnel.model.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserProfileController {
    private final UserProfileService service;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "q.user-url")
    public void listen(String url) throws IOException {
        log.info(url);
        final Profile profile = service.getUserProfile(url);
        rabbitTemplate.convertAndSend("q.user-profile", profile);
    }
}
