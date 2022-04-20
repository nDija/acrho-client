package org.acrho.client.test.model;

import org.acrho.client.model.AcrhoRunner;
import org.acrho.client.test.TimingExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AcrhoRunnerTest {

    Logger log = LoggerFactory.getLogger(AcrhoRunnerTest.class);

    private static final AcrhoRunner rd1 =
            new AcrhoRunner("Vincent Hullaert", LocalDate.of(1977, 5, 17),"V1","Cocoach",1, 460L);

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
        rd.setId(460L);
        log.debug(rd.toString());
        assertEquals(rd1, rd);
    }
}
