package nl.donna.pti.cs.infrabezetting.testutil;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import nl.donna.pti.cs.infra.data.secties.SectieSleutel;
import nl.donna.pti.cs.infrabezetting.data.bezetting.Sectiebezetting;
import nl.donna.pti.cs.planning.data.planelement.PlanelementId;
import nl.donna.pti.cs.planning.data.planfase.Planfase;

public class SectiebezettingGenerator {

    /* default */ static final int AANTAL_SECONDEN_IN_DAG = 86_400;

    /* default */ static final int AANTAL_SECONDEN_IN_UUR = 3_600;

    /* default */ static final int MAX_AANTAL_SECTIES = 22_000;

    private static final Random random = new Random();

    /**
     * Utility class en mag dus niet geinstantieerd worden.
     */
    private SectiebezettingGenerator() {
    }

    /**
     * Maakt een random BU sectiebezetting
     *
     */
    public static EnkeleSectiebezettingGenerator random_BU_Bezetting() {
        return new EnkeleSectiebezettingGenerator()
                .randomBezettingTijd(Planfase.BU)
                .randomGeldigheid(Planfase.BU, 0)
                .randomPlanelementId()
                .randomSectieSleutel();
    }

    /**
     * Maakt een semi random BU sectiebezetting, het planelementId en de sectieSleutel kun je hier zelf bepalen
     *
     */
    public static EnkeleSectiebezettingGenerator random_BU_Bezetting(final PlanelementId id, final SectieSleutel sectieSleutel) {
        final EnkeleSectiebezettingGenerator enkeleSBGenerator = new EnkeleSectiebezettingGenerator()
                .randomBezettingTijd(Planfase.BU)
                .randomGeldigheid(Planfase.BU, 0);
        enkeleSBGenerator.metSectieSleutel(sectieSleutel);
        enkeleSBGenerator.metPlanelementId(id);
        return enkeleSBGenerator;
    }

    /**
     *
     * @return een generator waarmee je meerdere sectiebezettingen kunt maken behorend bij een planelement.
     */
    public static SectiebezettingGeneratorCollection eenCollectionSectiebezettingen() {
        return new SectiebezettingGeneratorCollection();
    }

    /**
     * Generator methodes om een random (BU) sectiebezetting te maken
     */
    public static class EnkeleSectiebezettingGenerator extends Sectiebezetting.Builder {

        /* default */ int randomSecondenInUurOfDag(final Planfase planfase) {
            if (planfase.equals(Planfase.BU)) {
                return random.nextInt(AANTAL_SECONDEN_IN_UUR);
            } else {
                return random.nextInt(AANTAL_SECONDEN_IN_DAG);
            }
        }

        /**
         * Genereert een random sectieSleutel binnen een range van het maximale aantal secties.
         */
        public EnkeleSectiebezettingGenerator randomSectieSleutel() {
            final int sectieNummer = random.nextInt(MAX_AANTAL_SECTIES) + 1;
            this.metSectieSleutel(new SectieSleutel("IA" + sectieNummer, sectieNummer + "T"));
            return this;
        }

        /**
         * Genereert een random PlanelementID
         */
        public EnkeleSectiebezettingGenerator randomPlanelementId() {
            this.metPlanelementId(random.nextLong(), random.nextLong(), random.nextLong());
            return this;
        }

        /**
         * Afhankelijk van de planfase wordt er een random geldigheid op de sectiebezetting gezet.
         *
         * @param planfase
         *            adhv de planfase wordt dit een BU of SD geldigheid
         * @param gemAantalDagenGeldig
         *            deze wordt alleen gebruikt bij SD. zo kun je wel bepalen hoeveel dagen de SD geldig moet zijn. Maar binnnen
         *            een periode van twee weken worden deze wel random bepaald.
         */
        public EnkeleSectiebezettingGenerator randomGeldigheid(final Planfase planfase, final int gemAantalDagenGeldig) {

            switch (planfase) {
            case BU:
                this.metGeldigheid(maakRandomBUGeldigheid());
                break;
            case SD:
                this.metGeldigheid(maakRandom2WekenSDGeldigheid(gemAantalDagenGeldig));
                break;
            case BD:
            default:
                throw new UnsupportedOperationException("Ondersteuning van randomGeldigheid voor " + planfase + " is niet geimplementeerd");
            }
            this.metPlanfase(planfase);
            return this;
        }

        /**
         * Maakt een random SD geldigheid (er wordt uitgegaan van een periode van 2 weken
         *
         * @param gemAantalDagenGeldig
         *            hoeveel dagen de sectiebezetting een 'J' moet hebben in die periode van 2 weken.
         */
        /* default */ long maakRandom2WekenSDGeldigheid(final int gemAantalDagenGeldig) {
            final StringBuilder geldigheid = new StringBuilder("00000000000000");
            ThreadLocalRandom.current()
                    .ints(0, geldigheid.length())
                    .distinct()
                    .limit(gemAantalDagenGeldig)
                    .boxed()
                    .forEach(nummer -> geldigheid.replace(nummer, nummer + 1, "1"));

            return Long.parseLong(geldigheid.toString(), 2);
        }

        /**
         * Geeft een random BU geldigheid terug.
         *
         * Een BU sectiebezetting heeft als maximale geldigheid Ochtend, Dal, Avond, Nacht.
         * Als een byte reeks is dat 1111 ofwel 15.
         * Met een random geldigheid van 1 t/m 15 zijn dus alle mogelijk geldigheden van een BU mogelijk.
         */
        /* default */ long maakRandomBUGeldigheid() {
            return (long) (random.nextInt(15) + 1);
        }

        /**
         * Zet een random begin en eindtijd voor een sectiebezetting o.b.v. de planfase
         */
        /* default */ EnkeleSectiebezettingGenerator randomBezettingTijd(final Planfase planfase) {
            final int beginTijd = randomSecondenInUurOfDag(planfase);
            metBegintijdAsInt(beginTijd);
            metEindtijdAsInt(beginTijd + randomSecondenInUurOfDag(planfase));
            return this;
        }
    }
}
