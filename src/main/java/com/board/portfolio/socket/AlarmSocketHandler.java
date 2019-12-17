package com.board.portfolio.socket;

import com.board.portfolio.domain.entity.*;
import com.board.portfolio.exception.NotAllowAccessException;
import com.board.portfolio.exception.NotFoundPostException;
import com.board.portfolio.repository.AlarmRepository;
import com.board.portfolio.repository.BoardRepository;
import com.board.portfolio.security.jwt.JwtDecoder;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AlarmSocketHandler extends TextWebSocketHandler {

    private Map<String, Object> httpSessions = new HashMap();
    private BoardRepository boardRepository;
    private AlarmRepository alarmRepository;
    private JwtDecoder jwtDecoder;
    private ModelMapper modelMapper;

    @Autowired
    public AlarmSocketHandler(BoardRepository boardRepository,
                              AlarmRepository alarmRepository,
                              JwtDecoder jwtDecoder,
                              ModelMapper modelMapper){
        this.boardRepository = boardRepository;
        this.alarmRepository = alarmRepository;
        this.modelMapper = modelMapper;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String email = getEmailFromJWT(session);
        httpSessions.put(email, session);
    }
    private String getEmailFromJWT(WebSocketSession session){
        String cookieString = session.getHandshakeHeaders().get("cookie").get(0);
        String jwt;

        if(cookieString == null){
            throw new NotAllowAccessException("need sign in");
        }
        jwt = cookieString.split("=")[1];
        if(jwt.equals("")){
            throw new NotAllowAccessException("need sign in");
        }
        return jwtDecoder.decodeJwt(jwt).getClaims().get("email").asString();
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String email = getEmailFromJWT(session);
        httpSessions.remove(email);
    }

    @Transactional
    public void commentAlarmProcess(Long boardId, Account triggerAccount){
        Board board = boardRepository.findById(boardId).orElseThrow(()->new NotFoundPostException());
        Account targetAccount = board.getAccount();
        if(targetAccount==null)
            return;

        Alarm alarm = Alarm.builder()
                .targetAccount(targetAccount)
                .triggerAccount(triggerAccount)
                .eventType(AlarmEventType.WRITE_COMMENT)
                .eventContentId(boardId.toString())
                .build();
        alarmRepository.save(alarm);

        JSONObject jsonObject = buildCommentJObject(boardId , triggerAccount.getNickname());
        WebSocketSession session = (WebSocketSession)httpSessions.get(targetAccount.getEmail());
        if(session == null){
            return;
        }
        sendAlarm(jsonObject,session);
    }
    private JSONObject buildCommentJObject(Long boardId, String nickname){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("boardId", boardId);
        jsonObject.put("trigger", nickname);
        jsonObject.put("eventType", AlarmEventType.WRITE_COMMENT.toString());
        return jsonObject;
    }

    @Transactional
    public void replyCommentAlarmProcess(Comment comment, Account triggerAccount){
        Account targetAccount = comment.getAccount();
        if(targetAccount==null)
            return;


        Alarm alarm = Alarm.builder()
                .targetAccount(targetAccount)
                .triggerAccount(triggerAccount)
                .eventType(AlarmEventType.REPLY_COMMENT)
                .eventContentId(comment.getBoard().getBoardId().toString())
                .build();
        alarmRepository.save(alarm);

        Long boardId = comment.getBoard().getBoardId();
        String nickname = triggerAccount.getNickname();
        JSONObject jsonObject = buildReplyCommentJObject(boardId, nickname);

        WebSocketSession session = (WebSocketSession)httpSessions.get(targetAccount.getEmail());
        if(session == null){
            return;
        }
        sendAlarm(jsonObject,session);

    }
    private JSONObject buildReplyCommentJObject(Long boardId, String nickname){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("boardId", boardId);
        jsonObject.put("trigger", nickname);
        jsonObject.put("eventType", AlarmEventType.REPLY_COMMENT.toString());
        return jsonObject;
    }

    private void sendAlarm(JSONObject jsonObject, WebSocketSession session){
        try {
            session.sendMessage(new TextMessage(jsonObject.toJSONString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
