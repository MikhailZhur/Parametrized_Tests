package tests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import tests.data.Language;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.files.DownloadActions.click;

public class WebTests {

    @BeforeAll
    static void setUp() {
        Configuration.pageLoadStrategy = "eager";
    }

    @EnumSource(Language.class)
    @ParameterizedTest
    @Tag("WEB")
    @DisplayName("Корректное отображение текста 'Тут покупают дешёвые авиабилеты' в зависимости от выбранного языка")
    void aviasalesSiteShouldHeadDisplayCorrectText(Language language) {
        open("https://www.aviasales.ru");
        if (language == Language.valueOf("RU")) {
            $(".s__yGcFIRQ3jDUFsr1_").click();
            $(".s__ORGyGV3MdPAhAdi5").click();
        } else if (language == Language.valueOf("EN")) {
            $(".s__yGcFIRQ3jDUFsr1_").click();
            $("[placeholder='Поиск']").setValue("США");
            $(".s__S0YcHz6czIgNJKOD").click();
        }
        $("[data-test-id='page-header']").shouldHave(text(language.description));
    }

    @Test
    @Tag("WEB")
    @DisplayName("Корректное отображение текста 'Профиль, журнал, поддержка' в зависимости от выбранного языка")
    void aviasalesSiteShouldDisplayCorrectText() {
        open("https://www.aviasales.ru");
        $(".s__x45k4bUHEzooiNiY").shouldHave(text("Профиль"));
        $(".s__x45k4bUHEzooiNiY").shouldHave(text("Журнал"));
        $(".s__x45k4bUHEzooiNiY").shouldHave(text("Поддержка"));
    }
    //.find(text(Language.name()))


    @ParameterizedTest(name = "Для поискового запроса {0} должен выводить не пустой список")
    @ValueSource(strings = {
            "RestAssured", "JUnit 5"
    })
    @Tag("SMOKE")
    void searhResultsShouldDisplayNotBeEmptyList(String searchQuery) {
        open("https://www.startpage.com");
        $("#q").setValue(searchQuery).pressEnter();
        $$(".w-gl").shouldBe(CollectionCondition.sizeGreaterThan(0));
    }


    @ParameterizedTest(name = "Для поискового запроса {0} должна быть ссылка {1}")
    @CsvSource(value = {
            "RestAssured, https://rest-assured.io",
            "JUnit 5, https://junit.org"
    })
    @Tag("SMOKE")
    void searhResultsShouldContainExpectedUrl(String searchQuery, String expectedLink) {
        open("https://www.startpage.com");
        $("#q").setValue(searchQuery).pressEnter();
        $(".wgl-title-link-container").shouldHave(text(expectedLink));
    }


    @ParameterizedTest(name = "Для поискового запроса {0} должна быть ссылка {1}")
    @CsvFileSource(resources = "/test_data/searhResultsShouldContainExpectedUrl.csv")
    @Tag("SMOKE")
    void searhResultsShouldContainExpectedUrlCsvFileSource(String searchQuery, String expectedLink) {
        open("https://www.startpage.com");
        $("#q").setValue(searchQuery).pressEnter();
        $(".wgl-title-link-container").shouldHave(text(expectedLink));
    }

    public static void main(String[] args) {
        System.out.println(Language.RU.description);
        System.out.println(Language.EN.description);
    }

}

