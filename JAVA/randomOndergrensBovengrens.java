 final int i = random.nextInt(100) + 1;
        final Random random2 = new Random();
        final int returnValue;

        // om een random value te maken met een ondergrens en bovengrens doe je het volgende:
        // random.nextInt(bovengrens - ondergrens) - ondergrens
        // nu heb je een getal tussen de ondergrens en boven grens maar dan 1 lager, omdat de random methode bij de 0 begint met tellen
        // wil je bij de 1 beginnen met tellen dan wordt het dus:
        // random.nextInt(bovengrens - ondergrens + 1) - ondergrens

        int grens1 = gemiddelde - (deviatie * 2);
        final int grens2 = gemiddelde - deviatie;
        final int grens3 = gemiddelde + deviatie;
        final int grens4 = gemiddelde + (deviatie * 2);

        if (i <= 2) {
            if (grens1 < 1) {
                grens1 = 1;
                returnValue = random2.nextInt(gemiddelde - grens1 + 1) + grens1;
            } else {
                returnValue = random2.nextInt(grens1 - min + 1) + min;
            }
        } else if (i <= 16) {
            returnValue = random2.nextInt(grens2 - grens1 + 1) + grens1;
        } else if (i <= 50) {
            returnValue = random2.nextInt(gemiddelde - grens2 + 1) + grens2;
        } else if (i <= 84) {
            returnValue = random2.nextInt(grens3 - gemiddelde + 1) + gemiddelde;
        } else if (i <= 98) {
            returnValue = random2.nextInt(grens4 - grens3 + 1) + grens3;
        } else {
            // dus range 98 - 100
            returnValue = random2.nextInt(max - grens4 + 1) + grens4;
        }

        return returnValue < 1 ? 1 : returnValue;