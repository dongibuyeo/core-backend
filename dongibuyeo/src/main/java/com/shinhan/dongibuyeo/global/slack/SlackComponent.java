package com.shinhan.dongibuyeo.global.slack;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.slack.api.webhook.WebhookPayloads.payload;

@Component
public class SlackComponent {

    @Value("${slack.url}")
    private String SLACK_URL;

    private Slack slack = Slack.getInstance();

    public void sendSlackMessage(String title, HashMap<String,String> data) {

        try {
            slack.send(SLACK_URL, payload(
                    p -> p.text(title)
                            .attachments(List.of(
                                    Attachment.builder()
                                            .fields(data.keySet().stream().map(key -> generateSlackField(key,data.get(key))).collect(Collectors.toList())).build()
                            ))
            ));
        } catch (IOException e) {

        }

    }

    private Field generateSlackField(String title, String value) {
        return Field.builder()
                .title(title)
                .value(value)
                .valueShortEnough(false)
                .build();
    }
}
