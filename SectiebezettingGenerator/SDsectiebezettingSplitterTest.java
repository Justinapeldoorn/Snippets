package nl.donna.pti.cs.infrabezetting.testutil;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.donna.pti.cs.infrabezetting.data.bezetting.Sectiebezetting;
import nl.donna.pti.cs.planning.data.planfase.Planfase;
import org.junit.jupiter.api.Test;

class SDsectiebezettingSplitterTest {

    private static final int BEGIN_SECONDE_DAG = 0;

    private static final int EIND_SECONDE_DAG = 86399;

    /**
     * Test hoe de methode omgaat met een lege lijst sectiebezettingen.
     */
    @Test
    void splitsSectiebezettingLegeList() {
        final List<Sectiebezetting> sectiebezettingen = new ArrayList<>();
        final Collection<Sectiebezetting> geslitsteSectieBezettingen = SDsectiebezettingSplitter.splits(sectiebezettingen);

        assertThat(geslitsteSectieBezettingen).hasSize(0);
    }

    /**
     * Test of de methode de sectiebezetting splitst naar 2 sectiebezettingen door dat er een nachtovergang in zit.
     */
    @Test
    void splitsSectiebezettingEenNachtovergang() {
        final int vertrekDagEen = 8000;
        final int AankomstDagTwee = 170000;
        final List<Sectiebezetting> sectiebezettingen = new ArrayList<>();

        final Sectiebezetting sectiebezetting = SectiebezettingGenerator.random_BU_Bezetting()
                .metBegintijdAsInt(vertrekDagEen)
                .metEindtijdAsInt(AankomstDagTwee)
                .metGeldigheid(0b00000000000011)
                .metPlanfase(Planfase.SD)
                .build();
        sectiebezettingen.add(sectiebezetting);

        final Collection<Sectiebezetting> geslitsteSectieBezettingen = SDsectiebezettingSplitter.splits(sectiebezettingen);

        final Sectiebezetting verwachteSectieBezetting1 = Sectiebezetting.builder()
                .kopieVan(sectiebezetting)
                .metBegintijdAsInt(vertrekDagEen)
                .metEindtijdAsInt(EIND_SECONDE_DAG)
                .metGeldigheid(0b00000000000011)
                .build();
        final Sectiebezetting verwachteSectieBezetting2 = Sectiebezetting.builder()
                .kopieVan(sectiebezetting)
                .metBegintijdAsInt(BEGIN_SECONDE_DAG)
                .metEindtijdAsInt(AankomstDagTwee % SDsectiebezettingSplitter.AANTAL_SECONDEN_IN_DAG)
                .metGeldigheid(0b00000000000110)
                .build();

        assertThat(geslitsteSectieBezettingen).containsExactlyInAnyOrder(verwachteSectieBezetting1, verwachteSectieBezetting2);
    }

    /**
     * Test of de methode de sectiebezetting niet splitst wanneer er geen nachtovergang in de sectiebezetting zit.
     */
    @Test
    void splitsSectiebezettingZonderNachtovergang() {
        final int vertrekDagEen = 10;
        final int aankomstDagEen = 8000;

        final List<Sectiebezetting> sectiebezettingen = new ArrayList<>();
        final Sectiebezetting sectiebezetting = SectiebezettingGenerator.random_BU_Bezetting()
                .metBegintijdAsInt(vertrekDagEen)
                .metEindtijdAsInt(aankomstDagEen)
                .metGeldigheid(0b00000000000101)
                .metPlanfase(Planfase.SD)
                .build();
        sectiebezettingen.add(sectiebezetting);

        final Collection<Sectiebezetting> geslitsteSectieBezettingen = SDsectiebezettingSplitter.splits(sectiebezettingen);

        final Sectiebezetting verwachteSectieBezetting1 = Sectiebezetting.builder()
                .kopieVan(sectiebezetting)
                .metBegintijdAsInt(vertrekDagEen)
                .metEindtijdAsInt(aankomstDagEen)
                .metGeldigheid(0b00000000000101)
                .build();

        assertThat(geslitsteSectieBezettingen).containsExactlyInAnyOrder(verwachteSectieBezetting1);
    }

    /**
     * Test of de methode de sectiebezetting met 5 nachtovergangen en een overgang naar een andere planversie splitst naar 5 sectiebezettingen.
     * (De sectiebezetting in de volgende planversie wordt "weggegooid").
     */

    @Test
    void splitsSectiebezettingMetMeerdereNachtovergangen() {
        final int dagEen = 61_200;
        final int dagVijftien = 493_200;

        final List<Sectiebezetting> sectiebezettingen = new ArrayList<>();

        final Sectiebezetting sectiebezetting = SectiebezettingGenerator.random_BU_Bezetting()
                .metPlanfase(Planfase.SD)
                .metBegintijdAsInt(dagEen)
                .metEindtijdAsInt(dagVijftien)
                .metGeldigheid(0b00001000000000)
                .build();

        sectiebezettingen.add(sectiebezetting);

        final Collection<Sectiebezetting> gesplitsteSectiebezettingen = SDsectiebezettingSplitter.splits(sectiebezettingen);

        final Sectiebezetting verwachteSectieBezetting1 = Sectiebezetting.builder()
                .kopieVan(sectiebezetting)
                .metBegintijdAsInt(dagEen)
                .metEindtijdAsInt(EIND_SECONDE_DAG)
                .metGeldigheid(0b00001000000000)
                .build();

        final Sectiebezetting verwachteSectieBezetting2 = Sectiebezetting.builder()
                .kopieVan(sectiebezetting)
                .metBegintijdAsInt(BEGIN_SECONDE_DAG)
                .metEindtijdAsInt(EIND_SECONDE_DAG)
                .metGeldigheid(0b00010000000000)
                .build();

        final Sectiebezetting verwachteSectieBezetting3 = Sectiebezetting.builder()
                .kopieVan(sectiebezetting)
                .metBegintijdAsInt(BEGIN_SECONDE_DAG)
                .metEindtijdAsInt(EIND_SECONDE_DAG)
                .metGeldigheid(0b00100000000000)
                .build();

        final Sectiebezetting verwachteSectieBezetting4 = Sectiebezetting.builder()
                .kopieVan(sectiebezetting)
                .metBegintijdAsInt(BEGIN_SECONDE_DAG)
                .metEindtijdAsInt(EIND_SECONDE_DAG)
                .metGeldigheid(0b01000000000000)
                .build();

        final Sectiebezetting verwachteSectieBezetting5 = Sectiebezetting.builder()
                .kopieVan(sectiebezetting)
                .metBegintijdAsInt(BEGIN_SECONDE_DAG)
                .metEindtijdAsInt(EIND_SECONDE_DAG)
                .metGeldigheid(0b10000000000000)
                .build();

        assertThat(gesplitsteSectiebezettingen).containsExactlyInAnyOrder(verwachteSectieBezetting1, verwachteSectieBezetting2,
                verwachteSectieBezetting3, verwachteSectieBezetting4, verwachteSectieBezetting5);
    }

    /**
     * Test of de methode de sectiebezetting splitst naar 2 sectiebezettingen door dat er een nachtovergang in zit.
     */
    @Test
    void splitsSectiebezettingMeerdereDagenGeldigheid() {
        final int dagTwee = 170000;
        final int vertrekDagEen = 8000;
        final List<Sectiebezetting> sectiebezettingen = new ArrayList<>();

        final Sectiebezetting sectiebezetting = SectiebezettingGenerator.random_BU_Bezetting()
                .metPlanfase(Planfase.SD)
                .metBegintijdAsInt(vertrekDagEen)
                .metEindtijdAsInt(dagTwee)
                .metGeldigheid(0b10000000000001)
                .build();
        sectiebezettingen.add(sectiebezetting);

        final Collection<Sectiebezetting> geslitsteSectieBezettingen = SDsectiebezettingSplitter.splits(sectiebezettingen);

        final Sectiebezetting verwachteSectieBezetting1 = SectiebezettingGenerator.random_BU_Bezetting()
                .kopieVan(sectiebezetting)
                .metBegintijdAsInt(vertrekDagEen)
                .metEindtijdAsInt(EIND_SECONDE_DAG)
                .metGeldigheid(0b10000000000001)
                .build();
        final Sectiebezetting verwachteSectieBezetting2 = SectiebezettingGenerator.random_BU_Bezetting()
                .kopieVan(sectiebezetting)
                .metBegintijdAsInt(BEGIN_SECONDE_DAG)
                .metEindtijdAsInt(dagTwee % SDsectiebezettingSplitter.AANTAL_SECONDEN_IN_DAG)
                .metGeldigheid(0b00000000000010)
                .build();

        assertThat(geslitsteSectieBezettingen).containsExactlyInAnyOrder(verwachteSectieBezetting1, verwachteSectieBezetting2);
    }

    /**
     * test dat de methode een string van meer dan 14 chars afkapt en een nieuwe bezetting terug geeft
     * met de afgekapte geldigheids string, in het geval dat er nog een dag geldigheid in zit.
     */
    @Test
    void verwijderSBbuitenPlanvariant() {
        final Sectiebezetting sectiebezetting = SectiebezettingGenerator.random_BU_Bezetting()
                .metGeldigheid(0b100001000101100) // 15 dagen geldig
                .metBegintijdAsInt(23648)
                .metEindtijdAsInt(45688)
                .build();
        final List<Sectiebezetting> sectiebezettingen = new ArrayList<>();
        sectiebezettingen.add(sectiebezetting);

        final List<Sectiebezetting> result = SDsectiebezettingSplitter.verwijderSBbuitenPlanvariant(sectiebezettingen);
        assertThat(result).hasSize(1);

        final Sectiebezetting sectiebezetting1 = result.get(0);
        assertEquals(0b00001000101100, sectiebezetting1.getGeldigheid());
    }
}
