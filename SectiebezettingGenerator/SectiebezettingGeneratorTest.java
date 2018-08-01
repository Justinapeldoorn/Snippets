package nl.donna.pti.cs.infrabezetting.testutil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import nl.donna.pti.cs.infra.data.secties.SectieSleutel;
import nl.donna.pti.cs.infrabezetting.data.bezetting.Sectiebezetting;
import nl.donna.pti.cs.planning.data.planelement.PlanelementId;
import nl.donna.pti.cs.planning.data.planfase.Planfase;
import org.junit.jupiter.api.Test;

public class SectiebezettingGeneratorTest {

    /**
     * Test dat bij het gebruiken van deze method je een gevulde sectiebezetting terug krijgt.
     */
    @Test
    void genereerRandomSectiebezetting() {
        Sectiebezetting bezetting = SectiebezettingGenerator.random_BU_Bezetting().build();
        assertAll(
                () -> assertThat(bezetting.getBegintijd()).isGreaterThan(0),
                () -> assertThat(bezetting.getEindtijd()).isGreaterThan(bezetting.getBegintijd()),
                () -> assertNotNull(bezetting.getSectieSleutel()),
                () -> assertNotNull(bezetting.getPlanelementId()),
                () -> assertNotNull(bezetting.getPlanelementId().getLogischeSleutel()),
                () -> assertNotNull(bezetting.getPlanelementId().getId()),
                () -> assertNotNull(bezetting.getPlanelementId().getVersie()),
                () -> assertNotNull(bezetting.getGeldigheid()),
                () -> assertNotNull(bezetting.getPlanfase()));
    }

    /**
     * Test dat bij het gebruiken van deze method je een gevulde sectiebezetting terug krijgt.
     */
    @Test
    void genereerNietZoRandomSectiebezetting() {
        PlanelementId planelementId = new PlanelementId(0L, 1L, 2L);
        SectieSleutel sectieSleutel = new SectieSleutel("IA8", "20T");
        Sectiebezetting bezetting = SectiebezettingGenerator.random_BU_Bezetting(planelementId, sectieSleutel).build();

        assertAll(
                () -> assertThat(bezetting.getBegintijd()).isGreaterThan(0),
                () -> assertNotNull(bezetting.getGeldigheid()),
                () -> assertNotNull(bezetting.getPlanfase()),
                () -> assertNotNull(bezetting.getPlanelementId()),
                () -> assertEquals(bezetting.getSectieSleutel(), sectieSleutel),
                () -> assertEquals(bezetting.getPlanelementId(), planelementId));
    }

    /**
     * Test of de methode bij SD een juiste duur bij de sectiebezetting zet (die dus tussen de 1 seconden en 86.400 seconden ligt)
     */
    @Test
    void randomBezettingTijdSDTest() {
        final SectiebezettingGenerator.EnkeleSectiebezettingGenerator generator = SectiebezettingGenerator.random_BU_Bezetting();
        final Sectiebezetting sectiebezetting = generator.randomBezettingTijd(Planfase.SD).build();
        assertThat(sectiebezetting.getEindtijd() - sectiebezetting.getBegintijd()).isBetween(1, SectiebezettingGenerator.AANTAL_SECONDEN_IN_DAG);
    }

    /**
     * Test of de methode bij BU een juiste duur bij de sectiebezetting zet (die dus tussen de 1 seconden en 3.600 seconden ligt)
     */
    @Test
    void randomBezettingTijdBUTest() {
        final SectiebezettingGenerator.EnkeleSectiebezettingGenerator generator = SectiebezettingGenerator.random_BU_Bezetting();
        final Sectiebezetting sectiebezetting = generator.randomBezettingTijd(Planfase.BU).build();
        assertThat(sectiebezetting.getEindtijd() - sectiebezetting.getBegintijd()).isBetween(1, SectiebezettingGenerator.AANTAL_SECONDEN_IN_UUR);
    }

    /**
     * Test dat de random BU geldigheid juist werkt en ligt tussen de grenzen van 1 - 15.
     * (Maximale BU geldigheid is Ochtend, Dal, Avond, Nacht. Ofwel 1111, ofwel 15)
     */
    @Test
    void randomGeldigheidBU() {
        final SectiebezettingGenerator.EnkeleSectiebezettingGenerator generator = SectiebezettingGenerator.random_BU_Bezetting();
        final Sectiebezetting sectiebezetting = generator.randomGeldigheid(Planfase.BU, 0).build();
        final String s = Long.toBinaryString(sectiebezetting.getGeldigheid());
        assertThat(s.chars().filter(c -> c == '1').count()).isBetween(1L, 15L);
    }

    /**
     * Test dat de random geldigheid voor BD nog niet is geimplementeerd.
     */
    @Test
    void randomGeldigheidBD() {
        final SectiebezettingGenerator.EnkeleSectiebezettingGenerator generator = SectiebezettingGenerator.random_BU_Bezetting();
        assertThatThrownBy(() -> generator.randomGeldigheid(Planfase.BD, 0))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("Ondersteuning van randomGeldigheid voor BD is niet geimplementeerd");
    }

    /**
     * Test dat de random SD geldigheid wel echt random is.
     * Om dit te doen worden er 10 sectiebezettingen aangemaakt die allemaal 3 dagen geldig zijn.
     * Vervolgens worden alle geldigheden in een list gestopt.
     * Er wordt 1 geldigheid uit deze list gehaald en alle andere geldigheden in die list voorkomen en hetzelfde zijn worden verwijderd.
     *
     * De overgebleven lijst moet minimaal 6 zijn. Ofwel 60% van de sectiebezettingen heeft dus een andere geldigheid.
     */
    @Test
    void randomGeldigheidSD1() {
        final int aantalDagenGeldig = 3;
        final List<Sectiebezetting> sectiebezettingen = new ArrayList<>();
        final SectiebezettingGenerator.EnkeleSectiebezettingGenerator generator = SectiebezettingGenerator.random_BU_Bezetting();
        final List<Long> geldigheden = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            sectiebezettingen.add(generator.randomGeldigheid(Planfase.SD, aantalDagenGeldig).build());
        }

        sectiebezettingen.forEach(sb -> geldigheden.add(sb.getGeldigheid()));

        final Long eenGeldigheid = geldigheden.get(0);
        boolean isErNogEenGeldigheidTeVerwijderen;
        do {
            isErNogEenGeldigheidTeVerwijderen = geldigheden.remove(eenGeldigheid);
        } while (isErNogEenGeldigheidTeVerwijderen);

        assertThat(geldigheden.size()).isGreaterThan(5);
    }

    /**
     * Test dat de random geldigheid voor een SD sectiebezetting net zoveeel dagen geldig is als dat er wordt meegegeven aan de methode.
     * Ook wordt gecontroleerdt dat de SD geldigheid niet langer is dan twee weken (16383 in Long)
     */
    @Test
    void randomGeldigheidSD2() {
        final int verwachteAantalDagenGeldig = 8;
        final int maximaleGeldigheidSD = 16383;
        final SectiebezettingGenerator.EnkeleSectiebezettingGenerator generator = SectiebezettingGenerator.random_BU_Bezetting();
        final Sectiebezetting sectiebezetting = generator.randomGeldigheid(Planfase.SD, verwachteAantalDagenGeldig).build();
        final String s = Long.toBinaryString(sectiebezetting.getGeldigheid());
        final long aantalDagenGeldig = s.chars().filter(c -> c == '1').count();

        assertAll(
                () -> assertThat(aantalDagenGeldig).isEqualTo(verwachteAantalDagenGeldig),
                () -> assertThat(aantalDagenGeldig).isLessThanOrEqualTo(maximaleGeldigheidSD));
    }

    /**
     * Test of de random sectieSleutel wel binnen de grens van het max aantal secties ligt en dus indirect ook of die gevuld is.
     */
    @Test
    void randomSectieSleutel() {
        final SectiebezettingGenerator.EnkeleSectiebezettingGenerator generator = SectiebezettingGenerator.random_BU_Bezetting();
        final Sectiebezetting sectiebezetting = generator.randomSectieSleutel().build();
        final int sectieId = Integer.parseInt(sectiebezetting.getSectieSleutel().getIaGebiedNaam().substring(2));
        assertThat(sectieId).isLessThanOrEqualTo(SectiebezettingGenerator.MAX_AANTAL_SECTIES);
    }

    /**
     * Test dat de methode een int terug geeft die binnen de range voor SD ligt.
     */
    @Test
    void randomSecondenInUurOfDagBU() {
        final SectiebezettingGenerator.EnkeleSectiebezettingGenerator generator = SectiebezettingGenerator.random_BU_Bezetting();
        assertThat(generator.randomSecondenInUurOfDag(Planfase.BU)).isBetween(0, SectiebezettingGenerator.AANTAL_SECONDEN_IN_UUR - 1);
    }

    /**
     * Test dat de methode een int terug geeft die binnen de range voor SD ligt.
     */
    @Test
    void randomSecondenInUurOfDagSD() {
        final SectiebezettingGenerator.EnkeleSectiebezettingGenerator generator = SectiebezettingGenerator.random_BU_Bezetting();
        assertThat(generator.randomSecondenInUurOfDag(Planfase.SD)).isBetween(0, SectiebezettingGenerator.AANTAL_SECONDEN_IN_DAG - 1);
    }

    /**
     * Test dat de methode een instantie van de juiste class terug geeft.
     */
    @Test
    void eenCollectionSectiebezettingen() {
        assertThat(SectiebezettingGenerator.eenCollectionSectiebezettingen()).isInstanceOf(SectiebezettingGeneratorCollection.class);
    }
}
