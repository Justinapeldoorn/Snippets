int count = sectiebezettingenMap.values().stream().mapToInt(Collection::size).sum();
// dingen uit een lijst optellen

return sectiebezettingenMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
// dingen uit een lijst halen en optellen.