package nl.donna.pti.cs.infrabezetting.testutil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import nl.donna.pti.cs.infra.data.secties.SectieSleutel;
import nl.donna.pti.cs.infrabezetting.data.bezetting.Sectiebezetting;
import nl.donna.pti.cs.planning.data.planelement.PlanelementId;
import nl.donna.pti.cs.planning.data.planfase.Planfase;

public class SectiebezettingGeneratorCollection {

    private final HashMap<PlanelementId, List<Sectiebezetting>> sectiebezettingenMap = new HashMap<>();

    private Random random = new Random();

    /* default */ static final int MAX_AANTAL_SECTIES = 22_000;

    /**
     * package private constructor zodat als het goed is alleen de SectiebezettingGenerator deze class maar instantieert
     */
    /* default */ SectiebezettingGeneratorCollection(final Random random) {
        this.random = random;
    }

    /**
     * package private constructor zodat als het goed is alleen de SectiebezettingGenerator deze class maar instantieert
     */
    /* default */ SectiebezettingGeneratorCollection() {
    }

    private static long planelementIdCounter = 1L;

    /**
     * Deze methode geeft een generator met realistische SD testdata set aan sectiebezettingen terug.
     * Wanneer je een kleinere data set wilt hebben, geef je een kleinere parameter mee
     * Voorbeeld:
     * complete data set, dan voor schaal 1.0
     * 20 procent van de data set, dan voor schaal 0.2
     *
     * @param schaal
     *            getal double van 0.0 t/m ... maakt niet uit
     */
    public SectiebezettingGeneratorCollection metRealistische_SD_TestdataSet(final double schaal) {
        valideerSchaal(schaal);

        // onderstaande waardes zijn het standaard aantal planelementen in een actuele SD planvariant
        // die kan adhv de schaal verkleind worden.
        final int aantalViiPlanelementen = (int) (82 * schaal);
        final int aantalBrugopeningPlanelementen = (int) (18 * schaal);
        final int aantalBTDPlanelementen = (int) (99 * schaal);
        final int aantalBewegingPlanelementen = (int) (32558 * schaal);
        final int aantalPatroonPlanelementen = (int) (1542 * schaal);

        met_SD_VIIs(aantalViiPlanelementen, schaal);
        met_SD_BrugOpeningen(aantalBrugopeningPlanelementen, schaal);
        met_SD_BTDs(aantalBTDPlanelementen, schaal);
        met_SD_Bewegingen(aantalBewegingPlanelementen, schaal);
        met_SD_Patronen(aantalPatroonPlanelementen, schaal);

        return this;
    }

    /**
     * Deze methode maakt sectiebezettingen voor het meegegeven aantal planelementen. Deze sectiebezettingen worden opgebouwd aan de hand van
     * de statistische gegevens van een VII in een actuele SD planversie.
     *
     * @param aantalPlanelementen
     *            het aantal planelementen waarvoor je sectiebezettingen wilt maken
     * @param schaal
     *            De schaal zorgt ervoor dat het max aantal secties die bezet kunnen worden vergroot of verkleind kunnen worden
     *
     */
    public SectiebezettingGeneratorCollection met_SD_VIIs(final int aantalPlanelementen, final double schaal) {
        valideerSchaal(schaal);
        maakPlanelementenEnSectiebezettingen(aantalPlanelementen, PlanelementWaardes.VII_WAARDES, schaal, Planfase.SD);
        return this;
    }

    /**
     * Deze methode maakt sectiebezettingen voor het meegegeven aantal planelementen. Deze sectiebezettingen worden opgebouwd aan de hand van
     * de statistische gegevens van een Brugopening in een actuele SD planversie.
     *
     * @param aantalPlanelementen
     *            het aantal planelementen waarvoor je sectiebezettingen wilt maken
     * @param schaal
     *            De schaal zorgt ervoor dat het max aantal secties die bezet kunnen worden vergroot of verkleind kunnen worden
     *
     */
    public SectiebezettingGeneratorCollection met_SD_BrugOpeningen(final int aantalPlanelementen, final double schaal) {
        valideerSchaal(schaal);
        maakPlanelementenEnSectiebezettingen(aantalPlanelementen, PlanelementWaardes.BRUGOPENING_WAARDES, schaal, Planfase.SD);
        return this;
    }

    /**
     * Deze methode maakt sectiebezettingen voor het meegegeven aantal planelementen. Deze sectiebezettingen worden opgebouwd aan de hand van
     * de statistische gegevens van een BTD in een actuele SD planversie.
     *
     * @param aantalPlanelementen
     *            het aantal planelementen waarvoor je sectiebezettingen wilt maken
     * @param schaal
     *            De schaal zorgt ervoor dat het max aantal secties die bezet kunnen worden vergroot of verkleind kunnen worden
     *
     */
    public SectiebezettingGeneratorCollection met_SD_BTDs(final int aantalPlanelementen, final double schaal) {
        valideerSchaal(schaal);
        maakPlanelementenEnSectiebezettingen(aantalPlanelementen, PlanelementWaardes.BTD_WAARDES, schaal, Planfase.SD);
        return this;
    }

    /**
     * Deze methode maakt sectiebezettingen voor het meegegeven aantal planelementen. Deze sectiebezettingen worden opgebouwd aan de hand van
     * de statistische gegevens van een Beweging in een actuele SD planversie.
     *
     * @param aantalPlanelementen
     *            het aantal planelementen waarvoor je sectiebezettingen wilt maken
     * @param schaal
     *            De schaal zorgt ervoor dat het max aantal secties die bezet kunnen worden vergroot of verkleind kunnen worden
     *
     */
    public SectiebezettingGeneratorCollection met_SD_Bewegingen(final int aantalPlanelementen, final double schaal) {
        valideerSchaal(schaal);
        maakPlanelementenEnSectiebezettingen(aantalPlanelementen, PlanelementWaardes.BEWEGING_WAARDES, schaal, Planfase.SD);
        return this;
    }

    /**
     * Deze methode maakt sectiebezettingen voor het meegegeven aantal planelementen. Deze sectiebezettingen worden opgebouwd aan de hand van
     * de statistische gegevens van een Patroon in een actuele SD planversie. (Ja er komen patronen voor in een SD planversie)
     *
     * @param aantalPlanelementen
     *            het aantal planelementen waarvoor je sectiebezettingen wilt maken
     * @param schaal
     *            De schaal zorgt ervoor dat het max aantal secties die bezet kunnen worden vergroot of verkleind kunnen worden
     *
     */
    public SectiebezettingGeneratorCollection met_SD_Patronen(final int aantalPlanelementen, final double schaal) {
        valideerSchaal(schaal);
        maakPlanelementenEnSectiebezettingen(aantalPlanelementen, PlanelementWaardes.PATROON_WAARDES, schaal, Planfase.BU);
        return this;
    }

    /**
     * Roep deze methode aan het einde aan, nadat je hebt aangegeven wat je allemaal wilt genereren.
     *
     * @return een hashmap van planelement met daarbij een lijst met sectiebezettingen
     */
    public Map<PlanelementId, List<Sectiebezetting>> build() {
        return sectiebezettingenMap;
    }

    /**
     * Geeft een lijst terug van sectiebezettingen, ook als het meerdere planelementen in de sectiebezettingenMap zijn.
     * Vooral handig als je 1 planelement hebt en niks aan de map hebt.
     *
     * @return lijst van sectiebezettingen voor 1 of meer planelementen
     */
    public List<Sectiebezetting> buildSingleList() {
        return sectiebezettingenMap.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Maakt sectiebezettingen voor het aantal meegegeven planelementen op basis van de meegegeven statische waarden, bijvoorbeeld voor een VII.
     *
     * @param aantalPlanelementen
     *            aantal planelementen waarvoor je sectiebezettingen wilt maken
     * @param spw
     *            dit is een enum, hiermee geef je de statische waardes mee voor het type planelement dat je wilt maken
     * @param schaal
     *            hiermee kun je het maximale aantal secties waaruit wordt gekozen mee laten schalen.
     * @param planfase
     *            planfase van de sectiebezettingen
     */
    /* default */ void maakPlanelementenEnSectiebezettingen(final int aantalPlanelementen, final PlanelementWaardes spw, final double schaal,
            final Planfase planfase) {
        for (int i = 1; i <= aantalPlanelementen; i++) {
            final long id = neemNieuwPlanelementId();
            final PlanelementId planelementId = new PlanelementId(id, id, id);
            final int aantalSectiebezettingen = bepaalRandomAantalTussenMinMax(spw.minAantalSB, spw.maxAantalSB, spw.gemAantalSB,
                    spw.deviatieAantalSB);

            final List<Sectiebezetting> sectiebezettingen = maakSectiebezettingenBijPlanelement(aantalSectiebezettingen, spw, planelementId,
                    schaal, planfase);

            sectiebezettingenMap.put(planelementId, sectiebezettingen);
        }
    }

    private static long neemNieuwPlanelementId() {
        return planelementIdCounter++;
    }

    /**
     * Maakt een lijst met planelementen behorende bij het meegegeven planelementId.
     *
     * @param aantalSectiebezettingen
     *            hiermee kun je aangeven hoeveel sectiebezettingen er moeten worden gemaakt.
     * @param spw
     *            dit is een enum, hiermee geef je de statische waardes mee voor het type planelement dat je wilt maken
     * @param planelementId
     *            bij dit id worden sectiebezettingen gemaakt.
     * @param schaal
     *            hiermee kun je het maximale aantal secties waaruit wordt gekozen mee laten schalen.
     * @param planfase
     *            planfase van de sectiebezettingen
     */
    /* default */ List<Sectiebezetting> maakSectiebezettingenBijPlanelement(final int aantalSectiebezettingen, final PlanelementWaardes spw,
            final PlanelementId planelementId, final double schaal, final Planfase planfase) {

        final List<Sectiebezetting> sectiebezettingen = new ArrayList<>();
        final SectiebezettingGenerator.EnkeleSectiebezettingGenerator generator = new SectiebezettingGenerator.EnkeleSectiebezettingGenerator();
        final long geldigheid;
        if (Planfase.BU == planfase) {
            geldigheid = generator.maakRandomBUGeldigheid();
        } else {
            geldigheid = generator.maakRandom2WekenSDGeldigheid(spw.aantalDgnGeldig);
        }
        final int duur = bepaalRandomAantalTussenMinMax(spw.minDuur, spw.maxDuur, spw.gemDuur, spw.deviatieDuur);
        int beginTijd = generator.randomSecondenInUurOfDag(planfase);
        int eindTijd = beginTijd + duur;

        for (int i = 0; i < aantalSectiebezettingen; i++) {
            if (spw.isBeweging) {
                beginTijd = bepaalBeginTijd(sectiebezettingen, beginTijd);
                eindTijd = beginTijd + bepaalRandomAantalTussenMinMax(spw.minDuur, spw.maxDuur, spw.gemDuur, spw.deviatieDuur);
            }

            final Sectiebezetting sectiebezetting = Sectiebezetting.builder()
                    .metSectieSleutel(randomSectieSleutelMetSchaal(schaal))
                    .metBegintijdAsInt(beginTijd)
                    .metEindtijdAsInt(eindTijd)
                    .metGeldigheid(geldigheid)
                    .metPlanfase(planfase)
                    .metPlanelementId(planelementId)
                    .build();
            sectiebezettingen.add(sectiebezetting);
        }

        return SDsectiebezettingSplitter.splits(sectiebezettingen);
    }

    /**
     * Pak de laatste sectiebezettingen uit de lijst (deze staan in oplopende volgorde in de lijst). Wanneer er nog niks in staat wordt
     * er een random startTijd bepaald. Wanneer er al wel een voorgaande sectiebezetting is, dan wordt de starttijd van de
     * nieuwe sectiebezetting gelijk aan de eindTijd van de voorgaande sectiebezetting, plus 1 seconde.
     *
     * @param sectiebezettingen
     *            lijst met sectiebezettingen
     * @return begintijd voor volgende sectiebezetting
     */
    /* default */ int bepaalBeginTijd(final List<Sectiebezetting> sectiebezettingen, final int eersteBeginTijd) {
        final int startTijd;
        final int laatsteSectiebezettingInLijst = sectiebezettingen.size() - 1;

        if (sectiebezettingen.isEmpty()) {
            startTijd = eersteBeginTijd;
        } else {
            startTijd = (sectiebezettingen.get(laatsteSectiebezettingInLijst).getEindtijd() + 1);
        }
        return startTijd;
    }

    /**
     * Deze methode geeft een getal tussen de min en de max terug. Bijvoorbeeld:
     *
     * min - min deviatie - gemiddelde - max deviatie - max
     * 1 - 20 - 30 - 40 - 100
     *
     * 3% van de gevallen valt in het bereik 1-19
     * 94% van de gevallen valt in het bereik 20-40
     * 3% van de gevallen valt in het bereik 41-100
     *
     * Let op, er wordt wel altijd minimaal 1 terug gegeven.
     * Negatieve waardes kunnen niet.
     *
     * @param min
     *            het minimale getal dat je terug wilt krijgen
     * @param max
     *            het maximale getal dat je terug wilt krijgen
     * @param gemiddelde
     *            spreekt voor zich
     * @param deviatie
     *            de standaard afwijking ten opzichte van het gemiddelde
     *
     * @return een random getal binnen de range.
     */
    /* default */ int bepaalRandomAantalTussenMinMax(final int min, final int max, final int gemiddelde, final int deviatie) {
        final int i = random.nextInt(100) + 1;
        final Random random2 = new Random();
        final int returnValue;

        if (i <= 3) {
            returnValue = random2.nextInt((gemiddelde - deviatie) - min) + min;
        } else if (i <= 97) {
            returnValue = random2.nextInt((gemiddelde + deviatie + 1) - (gemiddelde - deviatie)) + (gemiddelde - deviatie);
        } else {
            // dus range 98 - 100
            final int hoogsteMin = gemiddelde + deviatie;
            returnValue = random2.nextInt(max - hoogsteMin) + hoogsteMin + 1;
        }

        return returnValue < 1 ? 1 : returnValue;
    }

    /**
     * Retourneert een random SectieSleutel binnen de range van het maximale aantal secties in een planvariant.
     *
     * @param schaal
     *            Afhankelijk van de schaal kan met maximaal aantal secties vergroten/verkleinen
     * @return
     */
    /* default */ SectieSleutel randomSectieSleutelMetSchaal(final double schaal) {
        final int sectieNummer = random.nextInt((int) (MAX_AANTAL_SECTIES * schaal)) + 1;
        return new SectieSleutel("IA" + sectieNummer, sectieNummer + "T");
    }

    /**
     * Valideer dat de schaal niet negatief is.
     */
    /* default */ void valideerSchaal(final double schaal) {
        if (schaal <= 0.0) {
            throw new IllegalArgumentException(schaal + " is geen geldige waarde, deze moet positief zijn");
        }
    }
}

/**
 * Deze enum bevat de statische waardes voor de verschillende planelementen in SD actuele planvariant
 */
/* default */ enum PlanelementWaardes {
    // waardes staan voor:
    // MinDuurSB,
    // maxDuurSB,
    // gemDuurSB,
    // deviatieDuurSB,
    // minAantalSB (per planelement),
    // maxAantalSB (per planelement),
    // gemAantalSB (per planelement),
    // deviatieAantalSB (per planelement),
    // aantalDagenGeldig (hoeveel J's er staan in een J/N string binnen een periode van twee weken),
    // isBeweging (hebben we het over een beweging?)

    VII_WAARDES(60_000, 1_122_420_000, 200_332_600, 116_106_452, 1, 22, 4, 2, 5, false),
    BRUGOPENING_WAARDES(120, 1200, 479, 200, 1, 38, 7, 4, 5, false),
    BTD_WAARDES(3_540_000, 1_900_740_000, 104_732_343, 50_000, 1, 22, 4, 2, 5, false),
    BEWEGING_WAARDES(2, 5360, 23, 18, 1, 1222, 243, 217, 5, true),
    // aantal dagen geldig voor een patroon slaat nergens op natuurlijk, dus wordt hier een onzin getal meegegeven.
    PATROON_WAARDES(2, 5360, 23, 18, 1, 1222, 243, 217, 0, true);

    final int minDuur;

    final int maxDuur;

    final int gemDuur;

    final int deviatieDuur;

    final int minAantalSB;

    final int maxAantalSB;

    final int gemAantalSB;

    final int deviatieAantalSB;

    final int aantalDgnGeldig;

    final boolean isBeweging;

    @SuppressWarnings("squid:S00107")
    /* default */ PlanelementWaardes(final int minDuur, final int maxDuur, final int gemDuur, final int deviatieDuur, final int minAantalSB,
            final int maxAantalSB, final int gemAantalSB, final int deviatieAantalSB, final int aantalDgnGeldig, final boolean isBeweging) {
        this.minDuur = minDuur;
        this.maxDuur = maxDuur;
        this.gemDuur = gemDuur;
        this.deviatieDuur = deviatieDuur;
        this.minAantalSB = minAantalSB;
        this.maxAantalSB = maxAantalSB;
        this.gemAantalSB = gemAantalSB;
        this.deviatieAantalSB = deviatieAantalSB;
        this.aantalDgnGeldig = aantalDgnGeldig;
        this.isBeweging = isBeweging;
    }
}
