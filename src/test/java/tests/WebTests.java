package tests;

import com.codeborne.selenide.CollectionCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import tests.data.Language;

import java.awt.*;
import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class WebTests extends BaseTest {

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


    static Stream<Arguments> dataButton(){
        return Stream.of(
                Arguments.of(Language.EN, List.of("Quick start", "Docs", "FAQ", "Blog", "Javadoc", "Users", "Quotes"),
                Arguments.of(Language.RU, List.of("С чего начать?", "Док", "ЧАВО", "Блог", "Javadoc", "Пользователи", "Отзывы")))
        );
    }

    @MethodSource("dataButton")
    @Tag("WEB")
    @ParameterizedTest(name = "Корректное отображение кнопок в зависимости от выбранного языка")
    void selenideSiteShouldDisplayCorrectButtons(Language language, List<String> expectedButtons) {
        open("https://ru.selenide.org/");
        $$("#languages a").find(text(language.name())).click();
        $$(".main-menu-pages a").filter(visible).shouldHave(texts(expectedButtons));
    }
}

