package ru.netology.delivery;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class AppCardTest {
    DataGenerator dataGenerator = new DataGenerator();
    SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");


    @BeforeEach
    void Setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitRequest() {
        String name = dataGenerator.makeName();
        String phone = dataGenerator.makePhone();
        String city = dataGenerator.makeCity();

        form.$("[placeholder='Город']").setValue(city);
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        form.$("[name=name]").setValue(name);
        form.$("[name=phone]").setValue(phone);
        form.$(".checkbox__box").click();
        $(".button__text").click();
        $(withText("Успешно")).shouldBe(visible);

        open("http://localhost:9999");
        form.$("[placeholder='Город']").setValue(city);
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(4));
        form.$("[name=name]").setValue(name);
        form.$("[name=phone]").setValue(phone);
        form.$(".checkbox__box").click();
        $(".button__text").click();
        $(withText("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(visible);
        $("[data-test-id=replan-notification] button.button").click();
        $(withText("Успешно")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorMessageIfWeSubmitWithIncorrectCity() {
        form.$("[placeholder='Город']").setValue("Даллас");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        form.$("[name=name]").setValue(dataGenerator.makeName());
        form.$("[name=phone]").setValue(dataGenerator.makePhone());
        form.$(".checkbox__box").click();
        $(".button__text").click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Доставка в выбранный город недоступна"));
    }


    @Test
    void shouldSubmitRequestWithoutChangeOfDate() {
        $("[placeholder='Город']").setValue(dataGenerator.makeCity());
        $("[name=name]").setValue(dataGenerator.makeName());
        $("[name=phone]").setValue(dataGenerator.makePhone());
        $(".checkbox__box").click();
        $(".button__text").click();
        $(withText("Успешно")).shouldBe(visible);
    }

    @Test
    void shouldGetErrorMessageIfWeSubmitSubmitWithIncorrectDate() {
        form.$("[placeholder='Город']").setValue(dataGenerator.makeCity());
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(1));
        form.$("[name=name]").setValue(dataGenerator.makeName());
        form.$("[name=phone]").setValue(dataGenerator.makePhone());
        form.$(".checkbox__box").click();
        $(".button__text").click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldGetErrorMessageIfWeSubmitSubmitWithoutName() {
        form.$("[placeholder='Город']").setValue(dataGenerator.makeCity());
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        form.$("[name=phone]").setValue(dataGenerator.makePhone());
        form.$(".checkbox__box").click();
        $(".button__text").click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldGetErrorMessageIfWeSubmitSubmitWithIncorrectName() {
        form.$("[placeholder='Город']").setValue(dataGenerator.makeCity());
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        form.$("[name=name]").setValue("Name Surname");
        form.$("[name=phone]").setValue(dataGenerator.makePhone());
        form.$(".checkbox__box").click();
        $(".button__text").click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldGetErrorMessageIfWeSubmitSubmitWithoutNumber() {
        form.$("[placeholder='Город']").setValue(dataGenerator.makeCity());
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        form.$("[name=name]").setValue(dataGenerator.makeName());
        form.$(".checkbox__box").click();
        $(".button__text").click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldGetErrorMessageIfWeSubmitWithoutCheckbox() {
        form.$("[placeholder='Город']").setValue(dataGenerator.makeCity());
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        form.$("[name=name]").setValue(dataGenerator.makeName());
        form.$("[name=phone]").setValue(dataGenerator.makePhone());
        $(".button__text").click();
        form.$(".input_invalid")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

}
