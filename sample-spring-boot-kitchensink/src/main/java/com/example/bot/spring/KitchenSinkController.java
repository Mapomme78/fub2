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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import javax.swing.event.ListSelectionEvent;

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
import com.linecorp.bot.model.PushMessage;
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
        checkBirthday();
        TextMessageContent message = event.getMessage();
        handleTextContent(event.getReplyToken(), event, message);
    }

    @EventMapping
    public void handleStickerMessageEvent(MessageEvent<StickerMessageContent> event) {
        checkBirthday();
   }

    @EventMapping
    public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        checkBirthday();
   }

    @EventMapping
    public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws IOException {
        checkBirthday();
   }

    @EventMapping
    public void handleAudioMessageEvent(MessageEvent<AudioMessageContent> event) throws IOException {
        checkBirthday();
   }

    @EventMapping
    public void handleVideoMessageEvent(MessageEvent<VideoMessageContent> event) throws IOException {
        checkBirthday();
   }

    @EventMapping
    public void handleUnfollowEvent(UnfollowEvent event) {
        checkBirthday();
   }

    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        checkBirthday();
   }

    @EventMapping
    public void handleJoinEvent(JoinEvent event) {
        checkBirthday();
   }

    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) {
        checkBirthday();
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
        checkBirthday();
   }

    @EventMapping
    public void handleOtherEvent(Event event) {
        checkBirthday();
        log.info("Received message(Ignored): {}", event);
    }

	private static final SimpleDateFormat minimalSDF = new SimpleDateFormat("dd-MM");
	private static final String INSERT_STATEMENT = "INSERT INTO birthdays (pseudo, date, lastWished) VALUES(?, ?, ?)";
	private static final String DEL_STATEMENT = "DELETE FROM birthdays WHERE pseudo LIKE ?";
	
    private Message addOrReplaceBirthday(String name, String dateAsString) {
       try {
            log.info("date lue="+minimalSDF.parse(dateAsString));
           } catch (Exception e) {
            return new TextMessage("Impossible de stocker la date d'anniversaire. Le format est jour-mois");
           }
    	try (	Connection connection = KitchenSinkApplication.getConnection();
              PreparedStatement delStmt = connection.prepareStatement(DEL_STATEMENT);
    			PreparedStatement stmt = connection.prepareStatement(INSERT_STATEMENT)) {
           delStmt.setString(1, name.trim());
           delStmt.executeUpdate();
    		stmt.setString(1, name.trim());
    		stmt.setString(2, dateAsString);
    		stmt.setInt(3, 0);
    		stmt.execute();
    	} catch (Exception e) {
    		log.error("", e);
    		return new TextMessage("Impossible de stocker la date d'anniversaire");
    	}
    	return new TextMessage("Date d'anniversaire de "+name+" enregistrée");
    }
    
    private Message removeBirthday(String name) {
    	try (	Connection connection = KitchenSinkApplication.getConnection();
    			PreparedStatement stmt = connection.prepareStatement(DEL_STATEMENT)) {
    		stmt.setString(1, name.trim());
    		stmt.executeUpdate();
    	} catch (Exception e) {
    		log.error("", e);
    		return new TextMessage("Impossible de supprimer la date d'anniversaire");
    	}
    	return new TextMessage("Date d'anniversaire de "+name+" supprimée");
    }
        
   private List<Message> listBirthdays() {
    	Date currentDate = new Date(System.currentTimeMillis());
    	int currentYear = Calendar.getInstance().get(Calendar.YEAR);
       Map<Date, String> lignes = new TreeMap<>();
		List<Message> ret = new ArrayList<>();
		StringBuffer sb = new StringBuffer(500);
    	try (	Connection connection = KitchenSinkApplication.getConnection();
    			Statement stmt = connection.createStatement()) {
    		ResultSet rs = stmt.executeQuery("SELECT * from birthdays");
    		while (rs.next()) {
    			String name = rs.getString(1);
    			String date = rs.getString(2);
    			int lastWished = rs.getInt(3);
    			
    			StringBuffer newLineToAdd = new StringBuffer();
    			newLineToAdd.append(name);
    			newLineToAdd.append(":");
    			newLineToAdd.append(date);
    			if (lastWished == 0) {
    				newLineToAdd.append(" -> jamais souhaité");
    			} else {
    				newLineToAdd.append(" -> souhaité en ");
    				newLineToAdd.append(lastWished);
    			}
               newLineToAdd.append("\n");
               Date dateReele = minimalSDF.parse(date);
               dateReele.setYear(currentYear-1900);
               if (dateReele.before(currentDate)) {
                   dateReele.setYear(currentYear-1900+1);
               }
               log.info("date prochain anniv "+name+"="+dateReele);
                if (lignes.get(dateReele)!=null) {
                   lignes.put(dateReele, lignes.get(dateReele)+ newLineToAdd.toString());
                } else {
                   lignes.put(dateReele, newLineToAdd.toString());
                }
     		}
   	} catch (Exception e) {
    		log.error("", e);
    		return Collections.singletonList(new TextMessage("Echec lors de la récupération de la liste d'anniversaires, désolé..."));
    	}
       for (Date dateAnniv:lignes.keySet()) {
           if (sb.length() + lignes.get(dateAnniv).length() > 500) {
    		     ret.add(new TextMessage(sb.toString()));
    			  sb = new StringBuffer(500);
    		}
    		sb.append(lignes.get(dateAnniv));
       }
if (sb.length() != 0) {
    			ret.add(new TextMessage(sb.toString()));
    		} else if (ret.isEmpty()) {
             return Collections.singletonList(new TextMessage("La liste d'anniversaires est vide, désolé..."));
           }
    		return ret;
    }

    private void checkBirthday() {
    	String currentDate = minimalSDF.format(new Date(System.currentTimeMillis()));
    	int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    	try (	Connection connection = KitchenSinkApplication.getConnection();
    			Statement stmt = connection.createStatement()) {
    		ResultSet rs = stmt.executeQuery("SELECT * from birthdays");
    		while (rs.next()) {
    			String name = rs.getString(1);
    			String date = rs.getString(2);
    			int lastWished = rs.getInt(3);
    			
    			if (currentDate.equals(date) && lastWished != currentYear) {
        			//lineMessagingClient.pushMessage(new PushMessage("Cfdf6437983461c70bd606684ccf5d925", new TextMessage("Bon anniversaire "+name)));
        			lineMessagingClient.pushMessage(new PushMessage("C051e35526afe7c0927737b2aa0ff16dc", new TextMessage("Bon anniversaire "+name)));
        			try (   PreparedStatement delStmt = connection.prepareStatement(DEL_STATEMENT);
                          PreparedStatement updateStmt = connection.prepareStatement(INSERT_STATEMENT)) {
                      delStmt.setString(1, name);
                      delStmt.executeUpdate();
        				updateStmt.setString(1, name);
        				updateStmt.setString(2, date);
        				updateStmt.setInt(3, currentYear);
        				updateStmt.execute();
        			}
    			}
    		}
    	} catch (Exception e) {
    		log.error("", e);
    	}
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
        if (text.startsWith("anniv ")) {
        	text = text.replaceFirst("anniv ", "");
        	if (text.equalsIgnoreCase("liste")) {
        		this.reply(replyToken, listBirthdays());
        	} else if (text.startsWith("ajout ")) {
            	text = text.replaceFirst("ajout ", "");
        		String[] decoupage = text.split(" ");
        		if (decoupage.length >= 2) {
        			String date = decoupage[decoupage.length-1];
        			String name = text.replaceAll(date, "");
        			this.reply(replyToken, addOrReplaceBirthday(name, date));
        		} else {
        			this.reply(replyToken, new TextMessage("Le format attendu est: @fub anniv ajout Machintruc 29-02"));
        		}
	    	} else if (text.startsWith("suppr ")) {
            	text = text.replaceFirst("suppr ", "");
    			this.reply(replyToken, removeBirthday(text));
	    	}
        	return;
        }
        switch (text.toLowerCase()) {
        	case "testfub": // groupe "Francs Unis" = C051e35526afe7c0927737b2aa0ff16dc
        		String useId = event.getSource().getUserId();
        		String senderId = event.getSource().getSenderId();
                this.replyText(replyToken, "user ID="+useId+", senderId="+senderId+", source class="+event.getSource().getClass()+",emoji=\u100075");
        		break;
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
           case "exercices":
           case "exos":
           case "exercice":
           case "exo":
               this.reply(replyToken, Arrays.asList(
            		   new TextMessage("Liste d'exercices pour aider à améliorer ses compétences d'attaquant.\nDans l'idéal, les exercices peuvent être pratiqués avec les défis d'autres Francs de même niveau (pour éviter de dépenser des unités)"),
            		   new ImagemapMessage(
                       createUri("/static/exos_v1"),
                       "Image cliquable des exercices",
                       new ImagemapBaseSize(1040, 1040),
                       Arrays.asList(
                               new MessageImagemapAction(
                                       "@fub exos déploiement",
                                       new ImagemapArea(
                                               0, 0, 260, 400
                                       )
                               ),
                               new MessageImagemapAction(
                                       "@fub exos artillerie",
                                       new ImagemapArea(
                                               260, 0, 640, 400
                                       )
                               )
                       )
               )));
               break;
           case "merveilles":
           case "merveille":
               this.reply(replyToken, Arrays.asList(
            		   new TextMessage("Explication des merveilles\n\nVous avez 3 types de merveilles: offensive, défensive et économique. Certaines merveilles sont de plusieurs types en même temps (ex: armée de terre cuite offensive et défensive)"),
            		   new ImagemapMessage(
                       createUri("/static/merveilles_v1"),
                       "Image cliquable des merveilles",
                       new ImagemapBaseSize(713, 1040),
                       Arrays.asList(
                               new MessageImagemapAction(
                                       "@fub merveilles bronze",
                                       new ImagemapArea(
                                               220, 0, 420, 180
                                       )
                               ),
                               new MessageImagemapAction(
                                       "@fub merveilles classique",
                                       new ImagemapArea(
                                               710, 0, 1040, 180
                                       )
                               ),
                               new MessageImagemapAction(
                                       "@fub merveilles poudre",
                                       new ImagemapArea(
                                               250, 250, 460, 430
                                       )
                               ),
                               new MessageImagemapAction(
                                       "@fub merveilles industriel",
                                       new ImagemapArea(
                                               700, 250, 1040, 440
                                       )
                               ),
                               new MessageImagemapAction(
                                       "@fub merveilles nucleaire",
                                       new ImagemapArea(
                                               250, 490, 440, 713
                                       )
                               )
                       )
               )));
               break;
           case "bases":
           case "base":
               this.reply(replyToken, Arrays.asList(
            		   new TextMessage("Exemples de bases GM bien construites, qui peuvent servir d'inspiration..."),
            		   new ImagemapMessage(
                       createUri("/static/bases_v1"),
                       "Image cliquable des bases",
                       new ImagemapBaseSize(713, 1040),
                       Arrays.asList(
                               new MessageImagemapAction(
                                       "@fub bases pierre",
                                       new ImagemapArea(
                                               0, 0, 220, 180
                                       )
                               ),
                               new MessageImagemapAction(
                                       "@fub bases bronze",
                                       new ImagemapArea(
                                               220, 0, 420, 180
                                       )
                               ),
                               new MessageImagemapAction(
                                       "@fub bases fer",
                                       new ImagemapArea(
                                               420, 0, 680, 180
                                       )
                               ),
                               new MessageImagemapAction(
                                       "@fub bases classique",
                                       new ImagemapArea(
                                               710, 0, 1040, 180
                                       )
                               ),
                               new MessageImagemapAction(
                                       "@fub bases moyen age",
                                       new ImagemapArea(
                                               0, 250, 250, 430
                                       )
                               ),
                               new MessageImagemapAction(
                                       "@fub bases poudre",
                                       new ImagemapArea(
                                               250, 250, 460, 430
                                       )
                               ),
                               new MessageImagemapAction(
                                       "@fub bases lumières",
                                       new ImagemapArea(
                                               500, 250, 630, 430
                                       )
                               ),
                               new MessageImagemapAction(
                                       "@fub bases indust",
                                       new ImagemapArea(
                                               700, 250, 1040, 440
                                       )
                               ),
                               new MessageImagemapAction(
                                       "@fub bases mond",
                                       new ImagemapArea(
                                               0, 490, 200, 713
                                       )
                               ),
                               new MessageImagemapAction(
                                       "@fub bases nuc",
                                       new ImagemapArea(
                                               250, 490, 440, 713
                                       )
                               ),
                               new MessageImagemapAction(
                                       "@fub bases guerre froide",
                                       new ImagemapArea(
                                               470, 490, 690, 713
                                       )
                               )
                       )
               )));
               break;
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
