

 //Dit is een test waarbij meerdere manieren van streams i.c.m. asserts worden gebruikt om een bepaalde waarde in een 
 @Test
    public void valideerPublicatieNummerMet0() {
        final DagBewegingVvvDto vvvDto = DagBewegingVvvBuilder.eenDagBewegingVvv().metPublicatieNummer(0L).build();
        final DagBeweging beweging = DagBewegingFactory.getInstance().createBdBeweging(vvvDto);
        logic.valideerPublicatieNummer(beweging);

		// flatExtraction. Dit zou op zich een goede manier zijn. Alleen in deze test niet want de code en de tekst is aan elkaar gelinked.
		// Hier stel je nu alleen maar zeker dat in de lijst een keer de juiste code voorkomt en een keer de juiste message. 
		// Je weet niet zeker dat deze twee ook bij elkaar horen
        assertThat(beweging.getFouten()).flatExtracting(HerontwerpException::getUserMessage).contains("Publicatienummer ongeldig, moet tussen 1 en 999999 liggen.");
        assertThat(beweging.getFouten()).flatExtracting(HerontwerpException::getCode).contains("PTI_PI_CL_409");

		// Dit is de manier die uiteindelijk is gekozen. Hier maak je een tuple, zo weet je zeker dat de combinatie van code + message voorkomt in de lijst
        assertThat(beweging.getFouten()).extracting(HerontwerpException::getUserMessage, HerontwerpException::getCode)
                .contains(tuple("Publicatienummer ongeldig, moet tussen 1 en 999999 liggen.","PTI_PI_CL_409"));
				
		// Onderstaande is een beetje omslachtig voor deze test. Maar het is een methode om een nieuwe lijst te maken o.b.v. een ander lijst		
        final List<String> userMessages = beweging.getFouten().stream().map(HerontwerpException::getUserMessage).collect(Collectors.toList());
        final List<String> foutCodes = beweging.getFouten().stream().map(HerontwerpException::getCode).collect(Collectors.toList());

        assertThat(userMessages).contains("");
        assertThat(foutCodes).contains("");
    }