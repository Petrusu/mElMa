package retrofit;

import java.util.ArrayList;

import models.Author;
import models.Book;
import models.BookPageResponse;
import models.Editor;
import models.Favorite;
import models.Theme;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("/api/ForAllUsers/fillbook")
    Call<ArrayList<Book>> GetBooks(

    );
    @GET("/api/ForAllUsers/getinformationaboutbook")
    Call<Book> GetCurrentBookInfotmation(
            @Query("bookId") int bookId
    );
    @POST("/api/ForAllUsers/addbookforfavorite")
    Call<Void> AddBookForFavorite(
            @Query("idBook") int idBook,
            @Header("Authorization") String authHeader
    );
    @GET("/api/ForAllUsers/Favorite")
    Call<ArrayList<Favorite>> Favorite(
            @Header("Authorization") String authHeader
    );
    @DELETE("/api/ForAllUsers/removebookfromfavorites")
    Call<Void> RemoveBookFromFavorites(
            @Query("idBook") int idBook,
            @Header("Authorization") String authHeader
    );
    @GET("/api/ForAllUsers/getLogin")
    Call<String> GetUserLogin(
            @Header("Authorization") String authHeader
    );
    @PUT("/api/ForAllUsers/changepassword")
    Call<Void> ChangePass(
            @Query("password") String password,
            @Header("Authorization") String authHeader
    );
    @PUT("/api/ForAllUsers/changelogin")
    Call<Void> ChangeLogin(
            @Query("login") String login,
            @Header("Authorization") String authHeader
    );
    @PUT("/api/ForAllUsers/changeemail")
    Call<Void> ChangeEmail(
            @Query("email") String email,
            @Header("Authorization") String authHeader
    );
    @GET("/api/ForAllUsers/fillauthors")
    Call<ArrayList<Author>> GetAuthors(

    );
    @GET("/api/ForAllUsers/filleditors")
    Call<ArrayList<Editor>> GetEditors(

    );
    @GET("/api/ForAllUsers/fillthemes")
    Call<ArrayList<Theme>> GetThemes(

    );
    @GET("/api/ForAllUsers/fillbookonpage")
    Call<BookPageResponse> getBooks(
            @Query("page") int page,
            @Query("size") int size
    );

}
