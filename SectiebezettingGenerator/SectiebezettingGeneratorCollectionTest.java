package nl.donna.pti.cs.infrabezetting.testutil;

import static nl.donna.pti.cs.infrabezetting.testutil.SectiebezettingGeneratorCollection.MAX_AANTAL_SECTIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import nl.donna.generiek.common.testutil.tags.ManualTests;
import nl.donna.pti.cs.infra.data.secties.SectieSleutel;
import nl.donna.pti.cs.infrabezetting.data.bezetting.Sectiebezetting;
import nl.donna.pti.cs.planning.data.planelement.PlanelementId;
import nl.donna.pti.cs.planning.data.planfase.Planfase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SectiebezettingGeneratorCollectionTest {

    private static final double STANDAARD_SCHAAL = 1.0;

    private final Random random = new Random();

    private SectiebezettingGeneratorCollection collectionGenerator;

    @BeforeEach
    void setUp() {
        collectionGenerator = SectiebezettingGenerator.eenCollectionSectiebezettingen();
    }

    /**
     * Test dat je het juiste aantal planelementen terug krijgt van deze methode
     */
    @Test
    void met_SD_VIIsTest() {
        final int verwachteAantalPlanelementen = 8;
        final Map<PlanelementId, List<Sectiebezetting>> planelementenMetSB = collectionGenerator
                .met_SD_VIIs(verwachteAantalPlanelementen, STANDAARD_SCHAAL)
                .build();
        assertThat(planelementenMetSB).hasSize(verwachteAantalPlanelementen);
    }

    /**
     * Test dat je het juiste aantal planelementen terug krijgt van deze methode
     */
    @Test
    void met_SD_BTDsTest() {
        final int verwachteAantalPlanelementen = 13;
        final Map<PlanelementId, List<Sectiebezetting>> planelementenMetSB = collectionGenerator
                .met_SD_BTDs(verwachteAantalPlanelementen, STANDAARD_SCHAAL)
                .build();
        assertThat(planelementenMetSB).hasSize(verwachteAantalPlanelementen);
    }

    /**
     * Test dat je het juiste aantal planelementen terug krijgt van deze methode
     */
    @Test
    void met_SD_BrugopeningenTest() {
        final int verwachteAantalPlanelementen = 4;
        final Map<PlanelementId, List<Sectiebezetting>> planelementenMetSB = collectionGenerator
                .met_SD_BrugOpeningen(verwachteAantalPlanelementen, STANDAARD_SCHAAL)
                .build();
        assertThat(planelementenMetSB).hasSize(verwachteAantalPlanelementen);
    }

    /**
     * Test dat je het juiste aantal planelementen terug krijgt van deze methode
     */
    @Test
    void met_SD_BewegingenTest() {
        final int verwachteAantalPlanelementen = 19;
        final Map<PlanelementId, List<Sectiebezetting>> planelementenMetSB = collectionGenerator
                .met_SD_Bewegingen(verwachteAantalPlanelementen, STANDAARD_SCHAAL)
                .build();
        assertThat(planelementenMetSB).hasSize(verwachteAantalPlanelementen);
    }

    /**
     * Test dat je het juiste aantal planelementen terug krijgt van deze methode. Daarnaast gaat het hier erom dat de planfase van
     * alle sectiebezettingen BU is. Daar wordt dus ook op getest.
     */
    @Test
    void met_SD_PatronenTest() {
        final int verwachteAantalPlanelementen = 8;
        final List<List<Sectiebezetting>> sectieBezettingenList = new ArrayList<>();
        final Map<PlanelementId, List<Sectiebezetting>> planelementenMetSB = collectionGenerator
                .met_SD_Patronen(verwachteAantalPlanelementen, STANDAARD_SCHAAL)
                .build();
        assertThat(planelementenMetSB).hasSize(verwachteAantalPlanelementen);

        planelementenMetSB.forEach((key, value) -> sectieBezettingenList.add(value));
        sectieBezettingenList
                // voor iedere lijst sectiebezettingen van planelement
                .forEach(sectiebezettingList -> sectiebezettingList
                        // test voor iedere sectiebezetting uit de lijst dat de planfase BU is.
                        .forEach(sectiebezetting -> assertEquals(Planfase.BU.getPlanfaseChar(), sectiebezetting.getPlanfase())));
    }

    /**
     * Test dat je het juiste aantal planelementen terug krijgt van deze methode
     */
    @ManualTests
    @Test
    void metRealistische_SD_TestDataSetTest() {
        // 34299 is gedefinieerd in de methode .metRealistische_SD_TestdataSet. Dit zijn het aantal planelementen voor elk planelementType bij
        // elkaar opgeteld.
        final int verwachteAantalPlanelementen = 34299;
        final Map<PlanelementId, List<Sectiebezetting>> planelementenMetSB = collectionGenerator
                .metRealistische_SD_TestdataSet(STANDAARD_SCHAAL)
                .build();
        assertThat(planelementenMetSB).hasSize(verwachteAantalPlanelementen);
    }

    /**
     * Test dat de lijst met sectiebezettingen die je terug krijgt voldoen aan alle waardes die je meegeeft.
     * Dus dat de duur van de sectiebezettingen zijn zoals je verwacht en dat je het verwachte aantal sectiebezettingen terug krijgt en dat het
     * aantal dagen geldig klopt.
     * Ook controleert de test dat voor alle sectiebezettingen het planelementId netjes overal hetzelfde is.
     */
    @Test
    void maakSectiebezettingenBijPlanelementSDTest() {
        final int aantalSectiebezettingen = random.nextInt(10) + 1;
        final PlanelementWaardes typePlanelement = PlanelementWaardes.BTD_WAARDES;
        final PlanelementId planelementId = new PlanelementId(1L, 1L, 1L);

        final Collection<Sectiebezetting> sectiebezettingen = SectiebezettingGenerator
                .eenCollectionSectiebezettingen()
                .maakSectiebezettingenBijPlanelement(aantalSectiebezettingen, typePlanelement,
                        planelementId, STANDAARD_SCHAAL, Planfase.SD);

        sectiebezettingen.forEach(sb -> assertAll(
                () -> assertEquals(planelementId, sb.getPlanelementId()),
                () -> assertEquals(Planfase.SD.getPlanfaseChar(), sb.getPlanfase())));
    }

    /**
     * Test dat de lijst met sectiebezettingen die je terug krijgt voldoen aan alle waardes die je meegeeft.
     * Dus dat de duur van de sectiebezettingen zijn zoals je verwacht en dat je het verwachte aantal sectiebezettingen terug krijgt.
     * Ook controleert de test dat voor alle sectiebezettingen het planelementId netjes overal hetzelfde is.
     */
    @Test
    void maakSectiebezettingenBijPlanelementBUTest() {
        final int aantalSectiebezettingen = random.nextInt(40) + 1;
        final PlanelementWaardes typePlanelement = PlanelementWaardes.PATROON_WAARDES;
        final PlanelementId planelementId = new PlanelementId(1L, 1L, 1L);

        final Collection<Sectiebezetting> sectiebezettingen = SectiebezettingGenerator
                .eenCollectionSectiebezettingen()
                .maakSectiebezettingenBijPlanelement(aantalSectiebezettingen, typePlanelement,
                        planelementId, STANDAARD_SCHAAL, Planfase.BU);

        assertThat(sectiebezettingen.size()).isEqualTo(aantalSectiebezettingen);

        sectiebezettingen.forEach(sb -> assertAll(
                () -> assertThat(sb.getEindtijd() - sb.getBegintijd()).isBetween(typePlanelement.minDuur, typePlanelement.maxDuur),
                () -> assertEquals(planelementId, sb.getPlanelementId()),
                () -> assertEquals(Planfase.BU.getPlanfaseChar(), sb.getPlanfase()),
                () -> {
                    final String s = Long.toBinaryString(sb.getGeldigheid());
                    assertThat(s.chars().filter(c -> c == '1').count()).isBetween(1L, 15L);
                }));
    }

    /**
     * Test dat de lijst met Secties die je terug krijgt in de juiste volgorde staan en dat de tijden niet overlappen.
     */
    @Test
    void maakSectiebezettingenBijPlanelementTestVolgordeList() {
        final int aantalSectiebezettingen = random.nextInt(30) + 1;
        final PlanelementId planelementId = new PlanelementId(1L, 1L, 1L);

        final List<Sectiebezetting> sectiebezettingen = SectiebezettingGenerator
                .eenCollectionSectiebezettingen()
                .maakSectiebezettingenBijPlanelement(aantalSectiebezettingen, PlanelementWaardes.BEWEGING_WAARDES, planelementId, STANDAARD_SCHAAL,
                        Planfase.SD);

        Sectiebezetting voorgaandeSectiebezetting = sectiebezettingen.get(0);
        for (int i = 1; i < sectiebezettingen.size(); i++) {
            assertThat(sectiebezettingen.get(i).getBegintijd()).isGreaterThan(voorgaandeSectiebezetting.getEindtijd());
            voorgaandeSectiebezetting = sectiebezettingen.get(i);
        }
    }

    /**
     * Hier wordt nog een keer (maar dan over de hele keten) getest dat wanneer er VII sectiebezettingen gemaakt moeten worden,
     * deze aan het einde van de rit allemaal aan de statistische waardes van een VII voldoen.
     */
    @Test
    @Disabled
    void controleerRangeSectiebezettingenVii() {
        final List<Sectiebezetting> sectiebezettingen = collectionGenerator
                .met_SD_VIIs(1, STANDAARD_SCHAAL)
                .buildSingleList();
        final int minDuur = PlanelementWaardes.VII_WAARDES.minDuur;
        final int maxDuur = PlanelementWaardes.VII_WAARDES.maxDuur;
        final int minAantalSB = PlanelementWaardes.VII_WAARDES.minAantalSB;
        final int maxAantalSB = PlanelementWaardes.VII_WAARDES.maxAantalSB;
        final int aantalDagenGeldig = PlanelementWaardes.VII_WAARDES.aantalDgnGeldig;

        assertThat(sectiebezettingen.size()).isBetween(minAantalSB, maxAantalSB);
        sectiebezettingen.forEach(sb -> assertAll(
                () -> assertThat((sb.getEindtijd() - sb.getBegintijd())).isBetween(minDuur, maxDuur),
                () -> {
                    final String s = Long.toBinaryString(sb.getGeldigheid());
                    assertThat(s.chars().filter(c -> c == '1').count()).isEqualTo(aantalDagenGeldig);
                }));
    }

    /**
     * Test dat ondanks dat je meerdere keren een nieuwe collectieGeneratorAanroept, de planelementId's uniek blijven.
     */
    @Test
    void oplopendePlanelementIdTest() {
        final double schaal = STANDAARD_SCHAAL;
        final int aantalPlanelementen = 10;
        final List<PlanelementId> planelementIds = new ArrayList<>();

        final Map<PlanelementId, List<Sectiebezetting>> planelementenMap1 = SectiebezettingGenerator.eenCollectionSectiebezettingen()
                .met_SD_BTDs(aantalPlanelementen, schaal)
                .build();

        final Map<PlanelementId, List<Sectiebezetting>> planelementenMap2 = SectiebezettingGenerator.eenCollectionSectiebezettingen()
                .met_SD_BrugOpeningen(aantalPlanelementen, schaal)
                .build();

        final Map<PlanelementId, List<Sectiebezetting>> planelementenMap3 = SectiebezettingGenerator.eenCollectionSectiebezettingen()
                .met_SD_BrugOpeningen(aantalPlanelementen, schaal)
                .build();

        planelementIds.addAll(planelementenMap1.keySet());
        planelementIds.addAll(planelementenMap2.keySet());
        planelementIds.addAll(planelementenMap3.keySet());

        assertThat(planelementIds).hasSize(30);
        planelementIds.forEach(planelementId -> assertAll(() -> assertThat(planelementIds).containsOnlyOnce(planelementId)));
    }

    /**
     * test of de methode een getal binnen de onderstaande range terug geeft.
     * (3% van de gevallen ligt tussen de 0-2)
     * De methode kan geen 0 of een negatief getal terug geven dus verwachten we een range van 1-2.
     */
    @Test
    void bepaalRandomAantalTussenMinDeviation() {
        final Random r = Mockito.mock(Random.class);
        final SectiebezettingGeneratorCollection generator = new SectiebezettingGeneratorCollection(r);
        when(r.nextInt(anyInt())).thenReturn(2);
        assertThat(generator.bepaalRandomAantalTussenMinMax(0, 10, 5, 2)).isBetween(1, 2);
    }

    /**
     * test of de methode een getal binnen de onderstaande range terug geeft.
     * (94% van de gevallen ligt tussen de 3-7)
     */
    @Test
    void bepaalRandomAantalTussenDeviatie() {
        final Random r = Mockito.mock(Random.class);
        final SectiebezettingGeneratorCollection generator = new SectiebezettingGeneratorCollection(r);
        when(r.nextInt(anyInt())).thenReturn(new Random().nextBoolean() ? 3 : 96);
        assertThat(generator.bepaalRandomAantalTussenMinMax(0, 10, 5, 2)).isBetween(3, 7);
    }

    /**
     * test of de methode een getal binnen de onderstaande range terug geeft.
     * (3% van de gevallen ligt tussen de 8-10)
     */
    @Test
    void bepaalRandomAantalTussenDeviatieMax() {
        final Random r = Mockito.mock(Random.class);
        final SectiebezettingGeneratorCollection generator = new SectiebezettingGeneratorCollection(r);
        when(r.nextInt(anyInt())).thenReturn(97);
        assertThat(generator.bepaalRandomAantalTussenMinMax(0, 10, 5, 2)).isBetween(8, 10);
    }

    /**
     * Test dat wanneer je de collectiegenerator geen sectiebezettingen laat maken voordat je de buildSingleList uitvoert, deze geen exceptie gooit.
     */
    @Test
    void buildSingleListTest() {
        assertDoesNotThrow(() -> collectionGenerator.buildSingleList());
    }

    /**
     * Hier wordt getest dat de single build bij meerdere planelement de lijst goed vult.
     * Om dit te controleren maken we meer planelementen aan dan het maximale aantal sectiebezettingen voor een vii (vandaar de +1)
     * Als de lijst die er dan terug komt groter is dan het max aantal sectiebezettingen voor een VII, betekent dit dat er sowieso meerdere
     * planelementen zijn samengevoegd.
     */
    @Test
    void buildSingleListTestMeerderePlanelementen() {
        final int maxAantalSBvoorViiPlus1 = PlanelementWaardes.VII_WAARDES.maxAantalSB + 1;
        final List<Sectiebezetting> sectiebezettingen = collectionGenerator.met_SD_VIIs(maxAantalSBvoorViiPlus1, STANDAARD_SCHAAL).buildSingleList();
        assertThat(sectiebezettingen.size()).isGreaterThan(maxAantalSBvoorViiPlus1);
    }

    /**
     * Test dat de schaal niet negatief mag zijn.
     */
    @Test
    void valideerSchaalFoutTest() {
        assertThatThrownBy(() -> collectionGenerator.valideerSchaal(-0.1)).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("is " +
                "geen geldige waarde, deze moet positief zijn");
    }

    /**
     * Test dat er bij een positieve schaal geen exception wordt gegooid.
     */
    @Test
    void valideerSchaalGoedTest() {
        assertDoesNotThrow(() -> collectionGenerator.valideerSchaal(0.1));
    }

    /**
     * test of je een SectieSleutel terug krijgt binnen de aangegeven schaal.
     */
    @Test
    void randomSectieSleutelMetSchaal() {
        final double schaal = 0.0005;
        final int verwachtSectieNummer = (int) (MAX_AANTAL_SECTIES * schaal);
        final int nummerUitGebiednaam = Integer.valueOf(collectionGenerator.randomSectieSleutelMetSchaal(schaal).getIaGebiedNaam().substring(2));
        assertThat(nummerUitGebiednaam).isLessThanOrEqualTo(verwachtSectieNummer);
    }

    /**
     * Test of de methode bepaalBeginTIjd de laatste eindtijd +1 (deze staan in oplopende volgorde in de lijst) uit de lijst met sectiebezettingen.
     * haalt.
     *
     */
    @Test
    void bepaalBeginTijd() {
        final int eindtijd = 1200;
        final List<Sectiebezetting> sectiebezettingen = new ArrayList<>();

        final Sectiebezetting sectiebezetting = Sectiebezetting.builder()
                .metSectieSleutel(new SectieSleutel("IA124", "T1234"))
                .metBegintijdAsInt(1000)
                .metEindtijdAsInt(eindtijd)
                .metGeldigheid(1)
                .metPlanfase(Planfase.SD)
                .metPlanelementId(1, 1, 1)
                .build();
        sectiebezettingen.add(sectiebezetting);

        final int startTijd = collectionGenerator.bepaalBeginTijd(sectiebezettingen, 2136);
        assertThat(startTijd).isEqualTo(eindtijd + 1);
    }

    /**
     * Test of de methode een random start tijd mee geeft voor een SD planfase wanneer de lijst met sectiebezettingen leeg is.
     */
    @Test
    void bepaalBeginTijdLegeLijst() {
        final List<Sectiebezetting> sectiebezettingen = new ArrayList<>();
        final int randomGegenereerdeInt = new Random().nextInt();
        final int startTijd = collectionGenerator.bepaalBeginTijd(sectiebezettingen, randomGegenereerdeInt);
        assertThat(startTijd).isEqualTo(randomGegenereerdeInt);
    }
}
