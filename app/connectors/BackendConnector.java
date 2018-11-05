package connectors;


import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import models.MessageInput;
import org.json.JSONException;
import org.json.JSONObject;
import play.libs.Json;
import play.libs.ws.*;

import javax.inject.Singleton;

import play.Logger;
import com.typesafe.config.ConfigFactory;


@Singleton
public class BackendConnector implements WSBodyReadables, WSBodyWritables {

    private static final Config conf = ConfigFactory.load();

    private static final String HOST = conf.getString("services.backend.host");
    private static final String PORT = conf.getString("services.backend.port");
    private static final String BASE_URL = String.format("http://%s:%s/backend/print", HOST, PORT);

    public static String sendMessage(MessageInput input) throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("message", input.getMessage());
        Logger.debug("created json obj " + obj.toString());
        return RestUtils.makeRequest(BASE_URL, "POST", obj);

    }

}