import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class AppCardTest {
    DataGenerator dataGenerator = new DataGenerator();

    @BeforeEach
    void Setup() {
        open("http://localhost:9999");
    }
    
    @Test
    void shouldSubmitRequest() {
        String name = dataGenerator.makeName();
        String phone = dataGenerator.makePhone();
        String city = dataGenerator.makeCity();

        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue(city);
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        form.$("[name=name]").setValue(name);
        form.$("[name=phone]").setValue(phone);
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Запланировать")).click();
        $(withText("Успешно")).shouldBe(visible);

        open("http://localhost:9999");
        form.$("[placeholder='Город']").setValue(city);
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(4));
        form.$("[name=name]").setValue(name);
        form.$("[name=phone]").setValue(phone);
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Запланировать")).click();
        $(withText("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(visible);
        $$(".button__content").find(exactText("Перепланировать")).click();
        $(withText("Успешно")).shouldBe(visible);
    }

    @Test
    void shouldSubmitWithIncorrectCity() {
        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue("Даллас");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        form.$("[name=name]").setValue(dataGenerator.makeName());
        form.$("[name=phone]").setValue(dataGenerator.makePhone());
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Запланировать")).click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Доставка в выбранный город недоступна"));
    }


    @Test
    void shouldSubmitRequestWithoutDate() {
        $("[placeholder='Город']").setValue(dataGenerator.makeCity());
        $("[name=name]").setValue(dataGenerator.makeName());
        $("[name=phone]").setValue(dataGenerator.makePhone());
        $(".checkbox__box").click();
        $$(".button__content").find(exactText("Запланировать")).click();
        $(withText("Успешно")).shouldBe(visible);
    }

    @Test
    void shouldSubmitWithIncorrectDate() {
        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue(dataGenerator.makeCity());
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(1));
        form.$("[name=name]").setValue(dataGenerator.makeName());
        form.$("[name=phone]").setValue(dataGenerator.makePhone());
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Запланировать")).click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldSubmitWithoutName() {
        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue(dataGenerator.makeCity());
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        form.$("[name=phone]").setValue(dataGenerator.makePhone());
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Запланировать")).click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSubmitWithIncorrectName() {
        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue(dataGenerator.makeCity());
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        form.$("[name=name]").setValue("Name Surname");
        form.$("[name=phone]").setValue(dataGenerator.makePhone());
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Запланировать")).click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldSubmitWithoutNumber() {
        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue(dataGenerator.makeCity());
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        form.$("[name=name]").setValue(dataGenerator.makeName());
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Запланировать")).click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

//    @Test
//    void shouldSubmitWithIncorrectNumber() {
//        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
//        form.$("[placeholder='Город']").setValue(dataGenerator.makeCity());
//        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardShiftDate(3));
//        form.$("[name=name]").setValue(dataGenerator.makeName());
//        form.$("[name=phone]").setValue("89815463321");
//        form.$(".checkbox__box").click();
//        $$(".button__content").find(exactText("Запланировать")).click();
//        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
//                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
//    }

    @Test
    void shouldSubmitWithoutCheckbox() {
        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue(dataGenerator.makeCity());
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(dataGenerator.forwardDate(3));
        form.$("[name=name]").setValue(dataGenerator.makeName());
        form.$("[name=phone]").setValue(dataGenerator.makePhone());
        $$(".button__content").find(exactText("Запланировать")).click();
        form.$(".input_invalid")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

}
