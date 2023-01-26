import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.request.CreateUser;
import model.response.CreateUserResp;
import model.response.GetUserListResp;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;


import java.time.LocalDate;

import static io.restassured.RestAssured.given; //Rest Assured(предоставляет медоты из своей библиотеки)

public class ApiTests {

    public final String BASE_URI = "https://reqres.in";
    // Пример выполнения Get запроса без тестирования
    @Test
    public void getListUser_1(){
        given()
                .when()
                .log().all()     // выведи тело предответа Headers and other
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all();  //выведи тело ответа
    }

// пример выполнения  Get запроса с проверкой полей с помощью TestNg
    @Test
    public void getListUser_2(){
        Response response = given()            // сюда сохраним ответ
                .when()
                .log().all()     // выведи тело предответа Headers and other
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all().extract().response();  //выведи тело ответа . Забери, вытащи и сохрани ответ * response сохраняет в эту переменную

        Assert.assertEquals(response.getStatusCode(), 200, "The actual statusCode is not 200"); //  статус ответа
        Assert.assertEquals(response.body().jsonPath().getInt("data[2].id"), 9);       // проверяем сравнением id нулевого индекса *массива контактов
        Assert.assertEquals(response.body().jsonPath().getString("data[1].email"), "lindsay.ferguson@reqres.in"); //проверяем емайл

    }

    //Пример выполнения get запроса с проверкой полей с помощью Rest Assured(предоставляет медоты из своей библиотеки)
    @Test
    public void getListUser_3(){
        given() //все начинается с этой строки
                .when()                           //обязательно , когда есть
                .baseUri(BASE_URI)    //начальная часть ссылки
                .log().all()
                .get("/api/users?page=2")       // и когда есть запрос на конечная часть ссылки
                .then()
                .log().all()
                .assertThat() // тогда проверь
                .statusCode(200) //ожидаем, что  такой код
                .body("data[0].id", Matchers.equalTo(7))  //ожидаем, что будет 7
                .body("data[1].email", Matchers.equalTo("lindsay.ferguson@reqres.in"));    //ожидаем,что будет такой емайл

    }

    //Пример выполнения Post запроса с проверкой полей с помощью Rest Assured
    @Test
    public void createUser_1(){
        String user = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";
        RestAssured.given()
                .contentType(ContentType.JSON) // в заголовке запроса передаем текст в json формате
                //.header("Content-type", "application/json"); используем в виде json аналогично первому варианту
                .body(user) // то что передаем тело запроса
                .when()
                .baseUri(BASE_URI)
                .log().all()
                .post("/api/users")    //Endpath
                .then()
                .log().all()
                .assertThat()
                .statusCode(201); //ожидаем, что  такой код

    }
    @Test
    public void createUser_2(){
        CreateUser user = new CreateUser("morpheus","leader");

        RestAssured.given()
                .contentType(ContentType.JSON) // в заголовке запроса передаем текст в json формате
                //.header("Content-type", "application/json"); используем в виде json аналогично первому варианту
                .body(user) // то что передаем тело запроса
                .when()
                .baseUri(BASE_URI)
                .log().all()
                .post("/api/users")    //Endpath
                .then()
                .log().all()
                .assertThat()
                .statusCode(201) //ожидаем, что  такой код
                .body("name", Matchers.equalTo("morpheus"))
                .body("job", Matchers.equalTo("leader"));
    }

@Test
public void createUser_3(){
    CreateUser user = new CreateUser("morpheus","leader");
    CreateUserResp createUserResp=RestAssured.given()
            .contentType(ContentType.JSON) // в заголовке запроса передаем текст в json формате
            //.header("Content-type", "application/json"); используем в виде json аналогично первому варианту
            .body(user) // то что передаем тело запроса
            .when()
            .baseUri(BASE_URI)
            .log().all()
            .post("/api/users")    //Endpath
            .then()
            .log().all()
            .extract().as(CreateUserResp.class);//забери ответ как экземпляр класса

    Assert.assertEquals(createUserResp.name, "morpheus");
    Assert.assertEquals(createUserResp.name, "leader");
    Assert.assertTrue(createUserResp.createdAt.contains(LocalDate.now().toString()));//проверь что поле содержит сегодняшнюю дату *(true or false)

}
@Test
    public void getUserListRespClass(){
        //GetUserListResp в этом классе описаны переменные, кот будем использовать для данного объекта

    GetUserListResp GetUserListResp = RestAssured.given()//создание объекта  // Rest Assured(предоставляет медоты из своей библиотеки с помощью кот мы можем вытягивать переменные из класса GetUserListResp)
            .when()
            .baseUri(BASE_URI)
            .log().all()
            .get("/api/users?page=2")
            .then()
            .log().all()
            .extract().as(model.response.GetUserListResp.class); //извлечи как модель ответа листа из класса , забери ответ как экземпляр класса

    Assert.assertEquals(GetUserListResp.data.get(3).first_name,"Byron");

    System.out.println(GetUserListResp.data.get(1).last_name);
    System.out.println(GetUserListResp.data.get(4).id);
    System.out.println(GetUserListResp.data.get(5).first_name);

    System.out.println(GetUserListResp.page);
    System.out.println(GetUserListResp.total);

}

}
