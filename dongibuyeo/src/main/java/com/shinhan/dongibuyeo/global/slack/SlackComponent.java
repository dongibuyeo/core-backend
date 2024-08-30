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

    @Value("https://hooks.slack.com/services/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")
    private String SLACK_URL;

    private Slack slack = Slack.getInstance();

    public void sendSlackMessage(String title, HashMap<String,String> data) {
        try {
            StringBuilder messageBuilder = new StringBuilder(title);
            data.forEach((key, value) -> messageBuilder.append("\n").append(key).append(": ").append(value));

            slack.send(SLACK_URL, payload(
                    p -> p.text(messageBuilder.toString())
            ));
        } catch (IOException e) {
            e.printStackTrace();
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
