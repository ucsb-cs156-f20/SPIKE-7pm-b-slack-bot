package edu.ucsb.changeme.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.ramswaroop.jbot.core.slack.models.Attachment;
import me.ramswaroop.jbot.core.slack.models.RichMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * The token you get while creating a new Slash Command. You
     * should paste the token in application.properties file.
     */
    @Value("${slashCommandToken}")
    private String slackToken;


    /**
     * Slash Command handler. When a user types for example "/app help"
     * then slack sends a POST request to this endpoint. So, this endpoint
     * should match the url you set while creating the Slack Slash Command.
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
    @RequestMapping(value = "/api/public/slash-command",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RichMessage onReceiveSlashCommand(@RequestParam("token") String token,
                                             @RequestParam("team_id") String teamId,
                                             @RequestParam("team_domain") String teamDomain,
                                             @RequestParam("channel_id") String channelId,
                                             @RequestParam("channel_name") String channelName,
                                             @RequestParam("user_id") String userId,
                                             @RequestParam("user_name") String userName,
                                             @RequestParam("command") String command,
                                             @RequestParam("text") String text,
                                             @RequestParam("response_url") String responseUrl) {
        // validate token

        logger.info("slash command processing...");
        if (!token.equals(slackToken)) {
            return new RichMessage("Sorry: the slack bot received an invalid token.");
        }

        /** build response */
        RichMessage richMessage = new RichMessage("The is Slash Commander!");
        richMessage.setResponseType("in_channel");
        // set attachments
        int numAttachments = 8;
        Attachment[] attachments = new Attachment[numAttachments];
        for (int i=0; i<numAttachments; i++)
            attachments[i] = new Attachment();
        attachments[0].setText(String.format("team_id=%s",teamId));
        attachments[1].setText(String.format("team_domain=%s",teamDomain));
        attachments[2].setText(String.format("channel_id=%s",channelId));
        attachments[3].setText(String.format("channel_name=%s",channelName));
        attachments[4].setText(String.format("user_id=%s",userId));
        attachments[5].setText(String.format("user_name=%s",userName));
        attachments[6].setText(String.format("command=%s",command));
        attachments[7].setText(String.format("text=%s",text));

        richMessage.setAttachments(attachments);
        
        return richMessage.encodedMessage(); // don't forget to send the encoded message to Slack
    }

    

}