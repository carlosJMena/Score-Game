package com.backend.server;

import com.backend.core.controller.GameController;

import com.backend.core.exceptions.BadRequestException;
import com.backend.core.exceptions.HttpMethodException;
import com.backend.core.exceptions.SessionException;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpHandler;
import sun.net.www.protocol.http.HttpURLConnection;

public class KingHttpHandler implements HttpHandler {
    /*
     *  Request for the different services
     */
    private static final String LOGIN_REQUEST =  "/(\\d*)/login";
    private static final String SCORE_REQUEST = "/(\\d*)/score\\?sessionkey=(.*)";
    private static final String HIGH_SCORE_LIST_REQUEST = "/(\\d*)/highscorelist";
    /*
     *  Request params
     */
    public static final String PARAMETER_ATTRIBUTE = "parameters";
    public static final String REQUEST_PARAMETER = "request";
    public static final String LEVEL_PARAMETER = "levelid";
    public static final String SESSION_KEY_PARAMETER = "sessionkey";
    public static final String SCORE_PARAMETER = "score";
    public static final String USER_ID_PARAMETER = "userid";

    /*
     *  Http Content type constants
     */
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TEXT = "text/plain";


    private final GameController gameController;

    /*
    *  Constructor
    */
    public KingHttpHandler(GameController instance) {
        this.gameController = instance;
    }


    public void handle(HttpExchange httpExchange) throws IOException {
        String httpBody = "";
        int httpCode = HttpURLConnection.HTTP_OK;
        String request = httpExchange.getRequestURI().toString();
        try{
            if(request.matches(LOGIN_REQUEST)){
                Map<String, String> loginParams = this.getLoginParameters(httpExchange);
                httpBody = gameController.login(loginParams);
            }else if(request.matches(SCORE_REQUEST)){
                Map<String, String> scoreParams = this.getScoreParameters(httpExchange);
                gameController.score(Integer.parseInt(scoreParams.get(LEVEL_PARAMETER)),Integer.parseInt(scoreParams.get(SCORE_PARAMETER)),scoreParams.get(SESSION_KEY_PARAMETER));
            } else if (request.matches(HIGH_SCORE_LIST_REQUEST)) {
                Map<String, String> highScoreListParams = this.getHighScoreListParams(httpExchange);
                httpBody = gameController.getHighScoreList(Integer.parseInt(highScoreListParams.get(LEVEL_PARAMETER)));
            } else {
                System.out.println("A HUIRRR!!");
            }

        }catch(HttpMethodException e){
            httpCode = HttpURLConnection.HTTP_BAD_METHOD;
            httpBody = e.getMessage();
        }catch (SessionException e){
            httpCode = HttpURLConnection.HTTP_NOT_FOUND;
            httpBody = e.getMessage();
        }catch (BadRequestException e){
            httpCode = HttpURLConnection.HTTP_BAD_REQUEST;
            httpBody = e.getMessage();
        }catch (Exception e){
            httpCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
            httpBody = e.getMessage();
        }

        httpExchange.getResponseHeaders().add(CONTENT_TYPE, CONTENT_TEXT);
        httpExchange.sendResponseHeaders(httpCode, httpBody.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(httpBody.getBytes());
        os.close();

    }

    private Map<String,String> getHighScoreListParams(HttpExchange httpExchange) throws HttpMethodException {
        validHttpMethod(httpExchange, "GET");
        Map<String, String> parameters = new HashMap<>();
        String uri = httpExchange.getRequestURI().toString();
        String levelId = uri.split("/")[1];
        parameters.put(LEVEL_PARAMETER, levelId);
        return parameters;
    }

    private Map<String,String> getScoreParameters(HttpExchange httpExchange) throws HttpMethodException, BadRequestException {
        validHttpMethod(httpExchange, "POST");
        Map<String, String> parameters = new HashMap<>();
        String uri = httpExchange.getRequestURI().toString();
        String score = getScoreFromBodyRequest(httpExchange);
        String levelId = uri.split("/")[1];
        parameters.put(LEVEL_PARAMETER,levelId);
        parameters.put(SESSION_KEY_PARAMETER,uri.split(SESSION_KEY_PARAMETER)[1].replace("=",""));
        parameters.put(SCORE_PARAMETER, score);
        return parameters;
    }

    private String getScoreFromBodyRequest(HttpExchange httpExchange) throws BadRequestException {
        String score;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            try {
                score = bufferedReader.readLine();
            } finally {
                bufferedReader.close();
                inputStreamReader.close();
            }
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
        return score;
    }

    /**
     * Method where parse the Parameters from the Login Request
     *
     * @param httpExchange
     * @return
     */
    private static Map<String, String> getLoginParameters(HttpExchange httpExchange) throws HttpMethodException {
        validHttpMethod(httpExchange, "GET");
        Map<String, String> parameters = new HashMap<>();
        String uri = httpExchange.getRequestURI().toString();
        String userId = uri.split("/")[1];
        parameters.put(USER_ID_PARAMETER, userId);
        return parameters;
    }
    /**
     * Method to validate if the request use the correct HttpMethod (GET or POST),
     * is not throws an Exception
     *
     * @param httpExchange
     * @param method
     */
    private static void validHttpMethod(HttpExchange httpExchange, String method) throws HttpMethodException {
        if (!method.equalsIgnoreCase(httpExchange.getRequestMethod())) {
            // Bad request
            throw new HttpMethodException("Http Method not Valid");
        }
    }
}
