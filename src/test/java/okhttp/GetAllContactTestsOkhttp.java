import com.google.gson.Gson;
import dto.AllContactsDto;
import dto.ContactDto;
import dto.ErrorDto;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class GetAllContactTestsOkhttp {
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoicG9raEBpMi51YSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjc3ODI0NjczLCJpYXQiOjE2NzcyMjQ2NzN9.pjYVnLKK6drB_16BjGUzUeUzfPbb5Ly342uDx0VmkLg";


    @Test
    public void getAllContactsSuccess() throws IOException {
        Request  request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization",token)
                .get().build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());

        AllContactsDto allContactsDto = gson.fromJson(response.body().string(), AllContactsDto.class);
        List<ContactDto> contacts = allContactsDto.getContacts();

        for(ContactDto contact: contacts){
            System.out.println(contact.getId());
            System.out.println("********");
        }
    }

    @Test
    public void getAllContactNegative() throws IOException {
        Request  request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization","gyfytfyf")
                .get().build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401);

        ErrorDto errorDto = gson.fromJson(response.body().string(),ErrorDto.class);
        System.out.println(errorDto.getMessage().toString());
        System.out.println(errorDto.getError());
        Assert.assertEquals(errorDto.getMessage().toString(),"JWT strings must contain exactly 2 period characters. Found: 0");


    }
}
