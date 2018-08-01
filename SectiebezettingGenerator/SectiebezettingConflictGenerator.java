package nl.donna.pti.cs.infrabezetting.testutil;

import java.util.Random;

import nl.donna.pti.cs.infra.data.secties.SectieSleutel;
import nl.donna.pti.cs.infrabezetting.data.conflict.SectiebezettingConflict;
import nl.donna.pti.cs.planning.data.planelement.PlanelementId;
import nl.donna.pti.cs.planning.data.planfase.Planfase;

public class SectiebezettingConflictGenerator {

    private SectiebezettingConflictGenerator() {
        // utility class
    }

    /**
     * Een SectiebezettingConflictBuilder die vervolgens naar wens aangepast kan worden.
     * Alleen bij deze builder zijn standaard vast waardes ingevuld, je kan dus daarna gelijk een build doen en dan heb je een SectiebezettingConflict
     *
     * Begintijd - :00.0
     * Eindtijd - :59.9
     * Planfase - BU
     * Geldigheid - ODAN
     * SectieId - 1L
     * PlanelementIdPaar - Random
     *
     * @return SectiebezettingConflictBuilder
     */
    public static Generator randomConflict() {

        return new Generator()
                .randomPlanelementIdPaar()
                .randomBezettingTijd()
                .randomGeldigheid(Planfase.BU)
                .randomSectieSleutel();

    }

    public static class Generator extends SectiebezettingConflict.Builder {

        private Random uniform = new Random();

        public Generator randomBezettingTijd() {
            return randomBezettingTijd(20, 6);
        }

        private int randomSecondeInUur() {
            return uniform.nextInt(3600);
        }

        private int normaalVerdeeldeWaarde(int gemiddelde, int deviatie) {
            return (int) (gemiddelde + Math.round(deviatie * uniform.nextGaussian()));
        }

        private PlanelementId randomPlanelementId() {
            return new PlanelementId(uniform.nextLong(), uniform.nextLong(), uniform.nextLong());
        }

        private String secondeNaarTijd(int seconden) {
            StringBuilder tijd = new StringBuilder(":");
            tijd.append(seconden / 60);
            tijd.append((seconden % 60) / 6);
            return tijd.toString();
        }

        public Generator randomSectieSleutel() {
            int iagNummer = uniform.nextInt(800); // aantal iagebieden in basisversieinfra
            int sectieNummer = uniform.nextInt(20000); // aantal secties in basisversieinfra
            this.metSectieSleutel(new SectieSleutel("IA" + Integer.toString(iagNummer), Integer.toString(sectieNummer) + "T"));
            return this;
        }

        public Generator randomPlanelementIdPaar() {
            this.metPlanelementIdPaar(randomPlanelementId(), randomPlanelementId());
            return this;
        }

        public Generator randomGeldigheid(Planfase planfase) {
            if (planfase != Planfase.BU) {
                throw new UnsupportedOperationException("TODO Ondersteuning van randomGeldigheid op BD/SD");
            }
            StringBuilder geldigheid = new StringBuilder();
            if (uniform.nextBoolean()) {
                geldigheid.append("O");
            }
            if (uniform.nextBoolean()) {
                geldigheid.append("D");
            }
            if (uniform.nextBoolean()) {
                geldigheid.append("A");
            }
            if (uniform.nextBoolean()) {
                geldigheid.append("N");
            }
            metGeldigheid(planfase.name(), geldigheid.toString());
            return this;
        }

        public Generator randomBezettingTijd(int gemiddelde, int deviatie) {
            int start = this.randomSecondeInUur();
            int eind = start + Math.max(6, this.normaalVerdeeldeWaarde(gemiddelde, deviatie));
            metBegintijd(secondeNaarTijd(start));
            metEindtijd(secondeNaarTijd(eind));
            return this;
        }

    }
}
