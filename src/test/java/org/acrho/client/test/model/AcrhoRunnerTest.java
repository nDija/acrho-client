package org.acrho.client.test.model;

import lombok.extern.log4j.Log4j2;
import org.acrho.client.model.AcrhoRunner;
import org.acrho.client.test.TimingExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
class AcrhoRunnerTest {

    private static AcrhoRunner rd1 =
            new AcrhoRunner("Vincent Hullaert", LocalDate.of(1977, 5, 17),"V1","Cocoach",1);

    @Test
    @DisplayName("When I set value to a new runner I guess I'm having same value in getters")
    @ExtendWith(TimingExtension.class)
    void acrhoRunnerGetTest(){
        AcrhoRunner rd = new AcrhoRunner();
        rd.setBib(1);
        rd.setBirthDate(LocalDate.of(1977, 5, 17));
        rd.setCategory("V1");
        rd.setName("Vincent Hullaert");
        rd.setTeam("Cocoach");
        log.debug(rd.toString());
        assertEquals(rd1, rd);
    }
}
