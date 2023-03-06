package okhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ContactResponseDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class AddNewContactTestsOkhttp {

    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoicG9raEBpMi51YSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjc3ODI0Nj" +
            "czLCJpYXQiOjE2NzcyMjQ2NzN9.pjYVnLKK6drB_16BjGUzUeUzfPbb5Ly342uDx0VmkLg";

    public final MediaType JSON = MediaType.get("application/json;charset=utf-8");

    @Test
    public void newContactSuccess() throws IOException {
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
        Assert.assertEquals(response.code(),200);
       ContactResponseDto resDto =  gson.fromJson(response.body().string(), ContactResponseDto.class);
        System.out.println(resDto.getMessage());
        Assert.assertTrue(resDto.getMessage().contains("Contact was added"));
    }


    @Test
    public void addNewContactWrongName() throws IOException {

        ContactDto dto = ContactDto.builder()
                .lastName("Kor")
                .email("kira@i.ua")
                .phone("99098908988")
                .address("jjj")
                .description("gyug").build();

        RequestBody body = RequestBody.create(gson.toJson(dto),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization",token)
                .post(body).build();
        Response response = client.newCall(request).execute();

        Assert.assertEquals(response.code(),400);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getMessage().toString());
        Assert.assertEquals(errorDto.getMessage().toString(),"{name=must not be blank}");

    }

    @Test
    public void addNewContactWrongEmail() throws IOException {

        ContactDto dto = ContactDto.builder()
                .name("Kira")
                .lastName("Kor")
                .email("kirai.ua")
                .phone("99098908988")
                .address("jjj")
                .description("gyug").build();

        RequestBody body = RequestBody.create(gson.toJson(dto), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization", token)
                .post(body).build();
        Response response = client.newCall(request).execute();

        Assert.assertEquals(response.code(), 400);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getMessage().toString());
       Assert.assertEquals(errorDto.getMessage().toString(), "{email=must be a well-formed email address}");

    }

    }
