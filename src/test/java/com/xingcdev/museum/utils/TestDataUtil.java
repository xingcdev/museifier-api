package com.xingcdev.museum.utils;

import com.xingcdev.museum.domain.entities.Museum;
import com.xingcdev.museum.domain.entities.Visit;

public class TestDataUtil {

    public static Museum createMuseumLeLouvre() {
        return Museum.builder()
                .name("musée du Louvre")
                .address("Musée du Louvre, 75058 Paris CEDEX 01")
                .postalCode("75058")
                .city("Paris")
                .department("Paris")
                .phoneNumber("01 40 20 50 50")
                .url("www.louvre.fr")
                .latitude(48.86079459806906)
                .longitude(2.3378156596817785)
                .build();
    }

    public static Museum createMuseumPicasso() {
        return Museum.builder()
                .name("musée Picasso")
                .address("5, rue Thorigny")
                .postalCode("75003")
                .city("Paris")
                .department("Paris")
                .phoneNumber("01 42 71 25 21")
                .url("www.museepicassoparis.fr/")
                .latitude(48.8597)
                .longitude(2.362644)
                .build();
    }

    public static Museum createMuseumChintreuil() {
        return Museum.builder()
                .name("musée Chintreuil")
                .address("66 rue Maréchal de Lattre-de-Tassigny")
                .postalCode("1190")
                .city("Pont-de-Vaux")
                .department("Ain")
                .phoneNumber("03 85 51 45 75")
                .url("www.musee-chintreuil.com/")
                .latitude(46.430076)
                .longitude(4.938125)
                .build();
    }

    public static Museum createMuseumMuseeDuBois() {
        return Museum.builder()
                .name("musée du bois")
                .address("place de la République")
                .postalCode("1420")
                .city("Mairie")
                .department("Ain")
                .phoneNumber("04 50 56 21 55")
                .url("")
                .latitude(45.959038)
                .longitude(5.831489)
                .build();
    }

    // Provide userId because the user is from Keycloak and the id changes each time we setup a new instance.
    public static Visit createVisitA(String userId, Museum museum) {
        return Visit.builder()
                .comment("Comment of visit A")
                .museum(museum)
                .userId(userId)
                .build();
    }

    public static Visit createVisitB(String userId, Museum museum) {
        return Visit.builder()
                .comment("Comment of visit B")
                .museum(museum)
                .userId(userId)
                .build();
    }

    public static Visit createVisitC(String userId, Museum museum) {
        return Visit
                .builder()
                .comment("Comment of visit C")
                .museum(museum)
                .userId(userId)
                .build();
    }
}
