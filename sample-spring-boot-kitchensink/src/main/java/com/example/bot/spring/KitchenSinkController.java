/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import com.linecorp.bot.model.message.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.VideoMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.AudioMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.VideoMessage;
import com.linecorp.bot.model.message.imagemap.ImagemapArea;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction;
import com.linecorp.bot.model.message.imagemap.URIImagemapAction;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LineMessageHandler
public class KitchenSinkController {
    @Autowired
    private LineMessagingClient lineMessagingClient;

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        TextMessageContent message = event.getMessage();
        handleTextContent(event.getReplyToken(), event, message);
    }

    @EventMapping
    public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
   }

    @EventMapping
    public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
   }

    @EventMapping
    public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws IOException {
   }

    @EventMapping
    public void handleAudioMessageEvent(MessageEvent<AudioMessageContent> event) throws IOException {
   }

    @EventMapping
    public void handleVideoMessageEvent(MessageEvent<VideoMessageContent> event) throws IOException {
   }

    @EventMapping
    public void handleUnfollowEvent(UnfollowEvent event) {
   }

    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
   }

    @EventMapping
    public void handleJoinEvent(JoinEvent event) {
   }

    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) {
        String replyToken = event.getReplyToken();
        switch (event.getPostbackContent().getData()) {
        case "lancement_mobilisation":
        	String datetime = event.getPostbackContent().getParams().get("datetime");
           String imageUrl = createUri("/static/buttons/GM.jpg");

        	this.replyText(replyToken, "Déclaration de Guerre!!! \n"
+"A tous les Francs Unis \n"
+"\n"
+"Une GM sera organisée "+datetime+"\n"
+"Inscription ici ( dans commentaires )\n"
+"\n"
+"IMPORTANT : les joueurs qui participent aux GM seront d'accord de se rendre disponible sur les 2 phases de combats qui sont les moments importants et décisifs (sauf en cas d'imprevu, travail ou obligations seront tout à fait compréhensibles) \n"
+"\n"
+"- phase 1 premier soir et phase 2 en fonction des résultats. Les coalitions defensives doivent être armées le jour du lancement.\n"
+"Remplissage de fortins pour tout le monde( si nécessaire )\n"
+"Renforts Cité (Nos Mondiaux Nucléaires voir Guerre froide)\n"
+"Formation des troupes en fonction de l'ennemi (s'il faut, demandez conseils)\n"
+"\n"
+"Chefs  de guerre  pas désignés");
        	break;
        default:
            this.replyText(replyToken, "Got postback data " + event.getPostbackContent().getData() + ", param " + event.getPostbackContent().getParams().toString());
        }
       this.replyText(replyToken, "Got postback data " + event.getPostbackContent().getData() + ", param " + event.getPostbackContent().getParams().toString());
    }

    @EventMapping
    public void handleBeaconEvent(BeaconEvent event) {
   }

    @EventMapping
    public void handleOtherEvent(Event event) {
        log.info("Received message(Ignored): {}", event);
    }

    private void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages))
                    .get();
            log.info("Sent messages: {}", apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyText(@NonNull String replyToken, @NonNull String message) {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }
        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "……";
        }
        this.reply(replyToken, new TextMessage(message));
    }

	static private SimpleDateFormat basicSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    private void handleTextContent(String replyToken, Event event, TextMessageContent content)
            throws Exception {
        String text = content.getText().trim();
        if (!text.startsWith("@fub ")) {
        	return;
        }
        text = text.replaceFirst("@fub ", "");
        log.info("Got text message from {}: {}", replyToken, text);
        switch (text.toLowerCase()) {
        	case "hello":
        	case "coucou":
        	case "hi":
        	case "cc":
        	case "salut":
        	case "allo":
           case "bonjour": {
                String userId = event.getSource().getUserId();
                if (userId != null) {
                    lineMessagingClient
                            .getProfile(userId)
                            .whenComplete((profile, throwable) -> {
                                if (throwable != null) {
                                   this.replyText(replyToken, "Bonjour (ajoute-moi en ami)\nComment puis-je t'aider?");
                                   return;
                                }
                                this.replyText(replyToken, "Bonjour "+ profile.getDisplayName() +"\nComment puis-je t'aider?");
                           });
                } else {
                    this.replyText(replyToken, "Bot can't use profile API without user ID");
                }
               break;
            }
           case "mobilisation": {
                String imageUrl = createUri("/static/buttons/GM.jpg");
                ButtonsTemplate buttonsTemplate = new ButtonsTemplate(
                        imageUrl,
                        "Mobilisation GM",
                        "Lancer un appel à mobilisation",
                        Arrays.asList(
                        		new DatetimePickerAction("Datetime",
                                        "lancement_mobilisation",
                                        "datetime",
                                        basicSDF.format(new Date(System.currentTimeMillis())),
                                        "2100-12-31T23:59",
                                        "1900-01-01T00:00")
                        ));
                TemplateMessage templateMessage = new TemplateMessage("Button alt text", buttonsTemplate);
                this.reply(replyToken, templateMessage);
                break;
            }
           case "richmenu": {
        	   lineMessagingClient.getRichMenuList().whenComplete((richMenuListResponse, throwable) -> {
                   if (throwable != null) {
                       this.replyText(replyToken, "erreur:"+throwable);
                       return;
                    }
                    this.replyText(replyToken, "RichMenuListResponse="+richMenuListResponse);
               });
        	   break;
           }
           default:
                log.info("Ignored unknown message {}: {}", replyToken, text);
                break;
        }
    }

    private static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .path(path).build()
                                          .toUriString();
    }

    private void system(String... args) {
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        try {
            Process start = processBuilder.start();
            int i = start.waitFor();
            log.info("result: {} =>  {}", Arrays.toString(args), i);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            log.info("Interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}
