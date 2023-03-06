package okhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ContactResponseDto;
import okhttp3.*;
import org.jetbrains.annotations.TestOnly;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class DeleteContactById {

    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoicG9raEBpMi51Y" +
            "SIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjc3ODI0NjczLCJpYXQiOjE2NzcyMjQ2NzN9.pjYVnLKK6drB_16BjGUzUeUzfPbb5Ly342uDx0VmkLg";


    public final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    @BeforeMethod
    public void precondition() throws IOException {
        int i = new Random().nextInt(1000)+1000;
        ContactDto dto = ContactDto.builder()
                .name("Kira")
                .lastName("Kor")
                .email("kira"+i+"@i.ua")
                .phone("990908988"+i)
                .address("jjj")
                .description("gyug").build();
        RequestBody body = RequestBody.create(gson.toJson(dto),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization",token)
                .post(body).build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        ContactResponseDto resDto =  gson.fromJson(response.body().string(), ContactResponseDto.class);
        System.out.println(resDto.getMessage());

        String message = resDto.getMessage();
        String[] all = message.split(": ");

    }



    @Test
    public void deleteContactByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/")
                .delete()
                .addHeader("Authorization",token).build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        ContactResponseDto resdto = gson.fromJson(response.body().string(), ContactResponseDto.class);
        Assert.assertEquals(resdto.getMessage(), "Contact was deleted!");






    }
}

