package com.backend.server;

import com.backend.core.singleton.GameSingleton;

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
    /**
     *  URL-pattern
     */
    public static final String LOGIN_REQUEST =  "/(\\d*)/login";
    public static final String SCORE_REQUEST = "/(\\d*)/score\\?sessionkey=(.*)";
    public static final String HIGH_SCORE_LIST_REQUEST = "/(\\d*)/highscorelist";
    /**
     *  Request params
     */
    public static final String LEVEL_PARAMETER = "levelid";
    public static final String SESSION_KEY_PARAMETER = "sessionkey";
    public static final String SCORE_PARAMETER = "score";
    public static final String USER_ID_PARAMETER = "userid";
    /**
     *  Http Content type constants
     */
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TEXT = "text/plain";
    /**
     *  Http Content type constants
     */
    public final GameSingleton gameSingleton;

    /**
    *  Constructor
    */
    public KingHttpHandler(GameSingleton instance) {
        this.gameSingleton = instance;
    }


    public void handle(HttpExchange httpExchange) throws IOException {
        String httpBody = "";
        int httpCode = HttpURLConnection.HTTP_OK;
        String request = httpExchange.getRequestURI().toString();
        try{
            if(request.matches(LOGIN_REQUEST)){
                Map<String, String> loginParams = this.getLoginParameters(httpExchange);
                httpBody = gameSingleton.login(loginParams);
            }else if(request.matches(SCORE_REQUEST)){
                Map<String, String> scoreParams = this.getScoreParameters(httpExchange);
                gameSingleton.score(Integer.parseInt(scoreParams.get(LEVEL_PARAMETER)),Integer.parseInt(scoreParams.get(SCORE_PARAMETER)),scoreParams.get(SESSION_KEY_PARAMETER));
            } else if (request.matches(HIGH_SCORE_LIST_REQUEST)) {
                Map<String, String> highScoreListParams = this.getHighScoreListParams(httpExchange);
                httpBody = gameSingleton.getHighScoreList(Integer.parseInt(highScoreListParams.get(LEVEL_PARAMETER)));
            } else {
                httpCode = HttpURLConnection.HTTP_NOT_FOUND;
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
    /**
     * Method where parse the Parameters from the High Score Request
     *
     * @param httpExchange
     * @return
     */
    public Map<String,String> getHighScoreListParams(HttpExchange httpExchange) throws HttpMethodException {
        validHttpMethod(httpExchange, "GET");
        Map<String, String> parameters = new HashMap<>();
        String uri = httpExchange.getRequestURI().toString();
        String levelId = uri.split("/")[1];
        parameters.put(LEVEL_PARAMETER, levelId);
        return parameters;
    }
    /**
     * Method where parse the Parameters from the Score Request
     *
     * @param httpExchange
     * @return
     */
    public Map<String,String> getScoreParameters(HttpExchange httpExchange) throws HttpMethodException, BadRequestException {
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
    /**
     * Method where parse the Parameters from the Score Request
     *
     * @param httpExchange
     * @return
     */
    public String getScoreFromBodyRequest(HttpExchange httpExchange) throws BadRequestException {
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
    public static Map<String, String> getLoginParameters(HttpExchange httpExchange) throws HttpMethodException {
        validHttpMethod(httpExchange, "GET");
        Map<String, String> parameters = new HashMap<>();
        String uri = httpExchange.getRequestURI().toString();
        String userId = uri.split("/")[1];
        parameters.put(USER_ID_PARAMETER, userId);
        return parameters;
    }
    /**
     * Method to validate HttpMethod
     *
     * @param httpExchange
     */
    public static void validHttpMethod(HttpExchange httpExchange, String method) throws HttpMethodException {
        if (!method.equalsIgnoreCase(httpExchange.getRequestMethod())) {
            // Bad request
            throw new HttpMethodException("Http Method not Valid");
        }
    }
}
