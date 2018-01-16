package comgreedyai.github.quest;

import android.app.LauncherActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

class RedisServiceQuestion {

    private static Service service;

    interface Service {

        // Retrieve the value stored under a key
        @GET("GET/{key}")
        Call<GetResponse> getQuestion(@Path("key") String key);

        // Delete the key and its value
        @GET("DEL/{key}")
        Call<DelResponse> deleteQuestion(@Path("key") String key);

        // Store a value (in a Java class ListItem) under a key
        @PUT("SET/{key}")
        Call<SetResponse> addQuestion(@Path("key") String key, @Body Question body);

        // Get all keys that match the given pattern
        @GET("KEYS/{pattern}*")
        Call<KeysResponse> allKeys(@Path("pattern") String pattern);
    }

    class SetResponse {
    }

    class DelResponse {
    }

    class GetResponse {
        @SerializedName("GET")
        Question item; // ListItem is a Java class that holds data in your RecyclerView
    }

    class KeysResponse {
        @SerializedName("KEYS")
        ArrayList<String> keys;
    }

    // Required because Redis stores JSON as strings
    public static class GetResponseSerializer implements JsonDeserializer<GetResponse> {
        @Override
        public GetResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
                throws JsonParseException {
            String content = je.getAsJsonObject().getAsJsonPrimitive("GET").getAsString();
            return new Gson().fromJson(String.format(Locale.ENGLISH, "{GET:%s}", content), GetResponse.class);
        }
    }

    static Service getService() {
        if (service == null) {
            GsonBuilder gsonBuilder = new GsonBuilder().setLenient();
            gsonBuilder.registerTypeHierarchyAdapter(GetResponse.class, new GetResponseSerializer());
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://ec2-54-210-25-34.compute-1.amazonaws.com/94b37c98a64e0a85b8c69a34a7b97e59c0/")
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .build();

            service = retrofit.create(Service.class);
        }
        return service;
    }
}