package edu.ucsb.changeme.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.ramswaroop.jbot.core.slack.models.Attachment;
import me.ramswaroop.jbot.core.slack.models.RichMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.ucsb.changeme.models.SlackSlashCommandParams;

/**
 * Sample Slash Command Handler.
 *
 * @author ramswaroop
 * @version 1.0.0, 20/06/2016
 */
@RestController
public class SlackSlashCommandController {

    private static final Logger logger = LoggerFactory.getLogger(SlackSlashCommandController.class);

    /**
     * The token you get while creating a new Slash Command. You should paste the
     * token in application.properties file.
     */
    @Value("${slashCommandToken}")
    private String slackToken;

    /**
     * Slash Command handler. When a user types for example "/app help" then slack
     * sends a POST request to this endpoint. So, this endpoint should match the url
     * you set while creating the Slack Slash Command.
     *
     * @param token
     * @param teamId
     * @param teamDomain
     * @param channelId
     * @param channelName
     * @param userId
     * @param userName
     * @param command
     * @param text
     * @param responseUrl
     * @return
     */
    @RequestMapping(value = "/api/public/slash-command", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RichMessage onReceiveSlashCommand(@RequestParam("token") String token,
            @RequestParam("team_id") String teamId, @RequestParam("team_domain") String teamDomain,
            @RequestParam("channel_id") String channelId, @RequestParam("channel_name") String channelName,
            @RequestParam("user_id") String userId, @RequestParam("user_name") String userName,
            @RequestParam("command") String command, @RequestParam("text") String text,
            @RequestParam("response_url") String responseUrl) {
        // validate token
        logger.info("slash command processing...");
        if (!token.equals(slackToken)) {
            return new RichMessage("Sorry: the slack bot received an invalid token.");
        }

        SlackSlashCommandParams params = new SlackSlashCommandParams();

        params.setToken(token);
        params.setTeamId(teamId);
        params.setTeamDomain(teamDomain);
        params.setChannelId(channelId);
        params.setChannelName(channelName);
        params.setUserId(userId);
        params.setCommand(command);
        params.setText(text);
        params.setResponseUrl(responseUrl);

        String textParts[] = params.getTextParts();

        if (textParts.length <= 0 || textParts[0].equals("")) {
            return emptyCommand(params);
        }

        String firstArg = textParts[0];

        if (firstArg.equals("status")) {
            return statusCommand(params);
        }

        if (firstArg.equals("time")) {
            return timeCommand(params);
        }

        if (firstArg.equals("debug")) {
            return debugCommand(params);
        }

        return unknownCommand(params);

    }

    public RichMessage unknownCommand(SlackSlashCommandParams params) {
        RichMessage richMessage = new RichMessage(String.format("Sorry but %s doesn't know the command %s",
                params.getCommand(), params.getTextParts()[0]));
        richMessage.setResponseType("ephemeral");
        return richMessage.encodedMessage(); // don't forget to send the encoded message to Slack
    }

    public RichMessage emptyCommand(SlackSlashCommandParams params) {
        RichMessage richMessage = new RichMessage(
                String.format("The %s bot knows these commands", params.getCommand()));

        richMessage.setResponseType("ephemeral");

        int numAttachments = 3;
        Attachment[] attachments = new Attachment[numAttachments];
        for (int i = 0; i < numAttachments; i++)
            attachments[i] = new Attachment();
        attachments[0].setText(String.format("%s status", params.getCommand()));
        attachments[1].setText(String.format("%s time", params.getCommand()));
        attachments[2].setText(String.format("%s debug", params.getCommand()));
        richMessage.setAttachments(attachments);

        return richMessage.encodedMessage(); // don't forget to send the encoded message to Slack
    }

    public RichMessage statusCommand(SlackSlashCommandParams params) {
        RichMessage richMessage = new RichMessage(
                String.format("From: %s Status is ok!", params.getCommand(), params.getTextParts()[0]));
        richMessage.setResponseType("ephemeral");
        return richMessage.encodedMessage(); // don't forget to send the encoded message to Slack
    }

    public RichMessage timeCommand(SlackSlashCommandParams params) {
        String message = String.format("From: %s... the time on the server is %s", params.getCommand(), timeNow());
        RichMessage richMessage = new RichMessage(message);
        richMessage.setResponseType("ephemeral");
        return richMessage.encodedMessage(); // don't forget to send the encoded message to Slack
    }

    public RichMessage debugCommand(SlackSlashCommandParams params) {
        /** build response */
        RichMessage richMessage = new RichMessage(String.format("Here is what %s knows:", params.getCommand()));
        richMessage.setResponseType("in_channel"); // other option is "ephemeral"

        // set attachments
        int numAttachments = 8;
        Attachment[] attachments = new Attachment[numAttachments];
        for (int i = 0; i < numAttachments; i++)
            attachments[i] = new Attachment();
        attachments[0].setText(String.format("team_id=%s", params.getTeamId()));
        attachments[1].setText(String.format("team_domain=%s", params.getTeamDomain()));
        attachments[2].setText(String.format("channel_id=%s", params.getChannelId()));
        attachments[3].setText(String.format("channel_name=%s", params.getChannelName()));
        attachments[4].setText(String.format("user_id=%s", params.getUserId()));
        attachments[5].setText(String.format("user_name=%s", params.getUserName()));
        attachments[6].setText(String.format("command=%s", params.getCommand()));
        attachments[7].setText(String.format("text=%s", params.getText()));

        richMessage.setAttachments(attachments);

        return richMessage.encodedMessage(); // don't forget to send the encoded message to Slack
    }

    private String timeNow() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

}