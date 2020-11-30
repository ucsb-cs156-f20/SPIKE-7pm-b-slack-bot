// package com.djamware.slackbot.bot;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;
// import org.springframework.web.socket.WebSocketSession;

// import me.ramswaroop.jbot.core.slack.Bot;
// import me.ramswaroop.jbot.core.slack.Controller;
// import me.ramswaroop.jbot.core.slack.EventType;
// import me.ramswaroop.jbot.core.slack.models.Event;
// import me.ramswaroop.jbot.core.slack.models.Message;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// @Component
// public class SlackBot extends Bot {
//     private static final Logger logger = LoggerFactory.getLogger(SlackBot.class);

//     @Value("${slackBotToken}")
//     private String slackToken;

//     @Override
//     public String getSlackToken() {
//         return slackToken;
//     }

//     @Override
//     public Bot getSlackBot() {
//         return this;
//     }

//     @Controller(events = { EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE })
//     public void onReceiveDM(WebSocketSession session, Event event) {
//         reply(session, event, new Message("Hi, I am " + slackService.getCurrentUser().getName()));
//     }

// }