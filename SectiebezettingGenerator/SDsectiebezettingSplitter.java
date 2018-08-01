package nl.donna.pti.cs.infrabezetting.testutil;

import java.util.ArrayList;
import java.util.List;

import nl.donna.pti.cs.infrabezetting.data.bezetting.Sectiebezetting;

/* default */ class SDsectiebezettingSplitter {

    /* default */ static final int AANTAL_SECONDEN_IN_DAG = 86_400;

    /**
     * mag alleen gebruikt worden binnen de testutil i.c.m. de generator van sectiebezetting collection.
     */
    /* default */ SDsectiebezettingSplitter() {
    }

    /**
     * Splits sectiebezettingen op nachtovergang.
     * Voorbeeld:
     * je hebt een sectiebezetting die vertrekt om 0800 met een geldigheid: JNNNJN en hij komt een dag later aan om 15:00
     * Dan krijg je twee sectiebezettingen terug.
     * - 1 van 08:00 t/m 23:59.59 met geldigheid JNNNJN
     * - 1 van 00:00.00 t/m 15:00.00 met geldigheid NJNNNJ
     *
     * LET OP. Wanneer het splitsen ervoor zorgt dat dat de sectiebezetting niet meer geldig is in de planvariant(dus binnen de standaard periode
     * van twee weken) dan worden de sectiebezettingen die buiten de planvariant vallen niet terug gegeven.
     *
     * @param ongesplitsteSB
     *            sectiebezettingen waar wel of niet nachtovergangen in zitten.
     * @return een lijst met gesplitste sectiebezettingen.
     */
    /* default */ static List<Sectiebezetting> splits(final List<Sectiebezetting> ongesplitsteSB) {
        final List<Sectiebezetting> gesplitsteSB = new ArrayList<>();
        ongesplitsteSB.forEach(sb -> gesplitsteSB.addAll(splits(sb)));

        return gesplitsteSB;
    }

    /**
     * Bepaald het aantal nachtovergangen in de sectiebezetting en roept de methodes aan die de verschillende nieuwe sectiebezettingen maken.
     *
     * @param sb
     *            een enkele sectiebezetting.
     */
    private static List<Sectiebezetting> splits(final Sectiebezetting sb) {
        final List<Sectiebezetting> result = new ArrayList<>();
        final int aantalNachtovergangen = (int) ((double) sb.getEindtijd()) / AANTAL_SECONDEN_IN_DAG;

        if (aantalNachtovergangen == 0) {
            result.add(sb);
        } else if (aantalNachtovergangen == 1) {
            result.add(maakBeginSB(sb));
            result.add(maakEindSB(sb, aantalNachtovergangen));
        } else if (aantalNachtovergangen >= 2) {
            result.add(maakBeginSB(sb));
            for (int i = 1; i < aantalNachtovergangen; i++) {
                result.add(maakTussenSB(sb, i));
            }
            result.add(maakEindSB(sb, aantalNachtovergangen));
        }

        return verwijderSBbuitenPlanvariant(result);
    }

    /**
     * Indien er een sectiebezetting moet worden gemaakt voor een nachtovergang wordt deze methode aangeroepen.
     * Deze sectiebezetting loopt van de originele start t/m 23:59.59
     *
     * @param sb
     *            enkele sectiebezetting
     */
    private static Sectiebezetting maakBeginSB(final Sectiebezetting sb) {
        final int beginTijd = sb.getBegintijd();

        return Sectiebezetting.builder()
                .kopieVan(sb)
                .metBegintijdAsInt(beginTijd)
                .metEindtijdAsInt(AANTAL_SECONDEN_IN_DAG - 1)
                .build();
    }

    /**
     * Indien er een sectiebezetting moet worden gemaakt tussen twee nachtovergangen wordt deze methode aangeroepen.
     * Deze sectiebezetting loopt van 00:00.00 t/m 23:59.59
     *
     * @param sb
     *            enkele sectiebezetting
     */
    private static Sectiebezetting maakTussenSB(final Sectiebezetting sb, final int geldigheidVerschuiving) {
        final long nieuweGeldigheid = sb.getGeldigheid() << geldigheidVerschuiving;

        return Sectiebezetting.builder()
                .kopieVan(sb)
                .metBegintijdAsInt(0)
                .metEindtijdAsInt(AANTAL_SECONDEN_IN_DAG - 1)
                .metGeldigheid(nieuweGeldigheid)
                .build();
    }

    /**
     * Indien er een sectiebezetting moet worden gemaakt na de laatste nachtovergang wordt deze methode aangeroepen.
     * Deze sectiebezetting loopt van de 00:00.00 t/m originele aankomst.
     *
     * @param sb
     *            enkele sectiebezetting
     */
    private static Sectiebezetting maakEindSB(final Sectiebezetting sb, final int geldigheidVerschuiving) {
        final int origineleEindtijd = sb.getEindtijd();
        final long nieuweGeldigheid = sb.getGeldigheid() << geldigheidVerschuiving;

        return Sectiebezetting.builder()
                .kopieVan(sb)
                .metBegintijdAsInt(0)
                .metEindtijdAsInt(origineleEindtijd % AANTAL_SECONDEN_IN_DAG)
                .metGeldigheid(nieuweGeldigheid)
                .build();
    }

    /**
     * Deze methode controleert of er in de lijst met sectiebezettingen, nog sectiebezettingen voorkomen waarbij er niet 1 dag geldig is in de
     * periode van de planvariant (de standaard twee weken).
     *
     * @param sectiebezettingen
     * @return
     */
    /* default */ static List<Sectiebezetting> verwijderSBbuitenPlanvariant(final List<Sectiebezetting> sectiebezettingen) {
        final List<Sectiebezetting> result = new ArrayList<>();

        sectiebezettingen.forEach(sb -> {
            final String s = Long.toBinaryString(sb.getGeldigheid());
            if (s.length() > 14) {
                final String substring = s.substring((s.length() - 14), s.length());
                if (substring.contains("1")) {
                    result.add(Sectiebezetting.builder()
                            .kopieVan(sb)
                            .metGeldigheid(Long.parseLong(substring, 2))
                            .build());
                }
            } else {
                result.add(sb);
            }
        });

        return result;
    }
}
