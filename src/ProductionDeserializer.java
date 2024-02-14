import com.google.gson.*;

import java.lang.reflect.Type;

public class ProductionDeserializer implements JsonDeserializer<Production> {

    @Override
    public Production deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        if ("Movie".equals(type)) {
            return context.deserialize(json, Movie.class);
        } else if ("Series".equals(type)) {
            return context.deserialize(json, Series.class);
        } else {
            throw new JsonParseException("Unknown type: " + type);
        }
    }
}