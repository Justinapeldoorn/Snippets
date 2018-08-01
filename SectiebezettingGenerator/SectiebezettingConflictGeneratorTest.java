package nl.donna.pti.cs.infrabezetting.testutil;

import nl.donna.pti.cs.infrabezetting.data.conflict.SectiebezettingConflict;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.jupiter.api.Test;

class SectiebezettingConflictGeneratorTest {

    @Rule
    public JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Test
    void test() {
        SectiebezettingConflict conflict = SectiebezettingConflictGenerator.randomConflict().build();
        softly.assertThat(conflict.getBegintijd()).isNotNull();
        softly.assertThat(conflict.getEindtijd()).isNotNull();
        softly.assertThat(conflict.getGeldigheid()).isNotNull();
        softly.assertThat(conflict.getSectieSleutel()).isNotNull();
        softly.assertThat(conflict.getPlanelementIdPaar()).isNotNull();
        softly.assertThat(conflict.getPlanelementIdPaar().getFirst()).isNotNull();
        softly.assertThat(conflict.getPlanelementIdPaar().getSecond()).isNotNull();
    }
}
