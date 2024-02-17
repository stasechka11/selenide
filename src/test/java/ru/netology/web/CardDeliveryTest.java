package ru.netology.web;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

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
        $("[data-test-id='name'] input").setValue("Потапова Анна");
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
}
