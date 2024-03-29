package ru.netology.web;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    private String generateDate(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    public void shouldBeSuccessCardDeliveryOrder() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");
        String planningDate = generateDate(5, "dd.MM.yyyy");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Потапова Алёна");
        $("[data-test-id='phone'] input").setValue("+71230077389");
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        $("button.button .spin").shouldBe(Condition.disappear, Duration.ofSeconds(16));
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    public void shouldBeSuccessCardDeliveryOrderDefaultDate() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Орёл");
        String planningDate = generateDate(3, "dd.MM.yyyy");
        $("[data-test-id='date'] input").shouldBe(Condition.exactValue(planningDate));
        $("[data-test-id='name'] input").setValue("Влас-Петр Кротов");
        $("[data-test-id='phone'] input").setValue("+00000000000");
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        $("button.button .spin").shouldBe(Condition.disappear, Duration.ofSeconds(16));
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    public void shouldSelectCityFromDropdownCardDeliveryOrder() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Ор");
        $$(".menu-item__control").findBy(Condition.exactText("Оренбург")).click();
        String planningDate = generateDate(3, "dd.MM.yyyy");
        $("[data-test-id='name'] input").setValue("Иванов Сергей");
        $("[data-test-id='phone'] input").setValue("+71230077389");
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        $("button.button .spin").shouldBe(Condition.disappear, Duration.ofSeconds(16));
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    public void shouldSelectDateInCalendarCardDeliveryOrder() {
        open("http://localhost:9999/");
        $("[data-test-id='city'] input").setValue("Нижний Новгород");
        $(".icon_name_calendar").click();
        String planningDate = generateDate(7, "dd.MM.yyyy");
        String planningDay = generateDate(7, "dd");
        String planningMonth = generateDate(7, "MM");

        if (!generateDate(3, "MM").equals(planningMonth)) {
            $(".calendar__arrow_direction_right[data-step='1']").click();
        }

        $$("td.calendar__day").findBy(Condition.exactText(planningDay)).click();
        $("[data-test-id='name'] input").setValue("Иванов Сергей");
        $("[data-test-id='phone'] input").setValue("+71230077389");
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        $("button.button .spin").shouldBe(Condition.disappear, Duration.ofSeconds(16));
        $("[data-test-id='notification'] .notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + planningDate));
    }
}
