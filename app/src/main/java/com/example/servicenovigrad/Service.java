package com.example.servicenovigrad;

import java.util.ArrayList;
import java.util.HashMap;

public class Service {
    String serviceName, serviceDescription;
    boolean nom, dob, address, typePermis, preuveDomicile, preuveStatut, photoClient;


    public Service(String serviceName) {
        this(serviceName, false, false, false, false, false, false, false);
    }

    public Service(String serviceName, boolean nom, boolean dob, boolean address, boolean typePermis, boolean preuveDomicile, boolean preuveStatut, boolean photoClient) {
        this.serviceName = serviceName;
        serviceDescription = "";
        this.nom = nom;
        this.dob = dob;
        this.address = address;
        this.typePermis = typePermis;
        this.preuveDomicile = preuveDomicile;
        this.preuveStatut = preuveStatut;
        this.photoClient = photoClient;
        modifyServiceDescription(createServiceDescription());
    }

    public boolean isNom() {
        return nom;
    }

    public boolean isDob() {
        return dob;
    }

    public boolean isAddress() {
        return address;
    }

    public boolean isTypePermis() {
        return typePermis;
    }

    public boolean isPreuveDomicile() {
        return preuveDomicile;
    }

    public boolean isPreuveStatut() {
        return preuveStatut;
    }

    public boolean isPhotoClient() {
        return photoClient;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }


    public void modifyServiceDescription(String newString) {
        this.serviceDescription = newString;
    }

    public void removeDescription() {
        modifyServiceDescription("");
    }

    public String toString() {
        return "Service de " + getServiceName() + ":\n";
    }

    public static Service fromHashMap(HashMap<String, String> h) {
        return new Service(h.get("serviceName"), Boolean.parseBoolean(h.get("nom")),
                Boolean.parseBoolean(h.get("dob")), Boolean.parseBoolean(h.get("address")),
                Boolean.parseBoolean(h.get("typePermis")), Boolean.parseBoolean(h.get("preuveDomicile")),
                Boolean.parseBoolean(h.get("preuveStatut")), Boolean.parseBoolean(h.get("photoClient")));
    }

    public static HashMap<String, String> toHashMap(Service s) {
        HashMap<String, String> h = new HashMap<>();
        h.put("serviceName", s.getServiceName());
        h.put("nom", String.valueOf(s.isNom()));
        h.put("dob", String.valueOf(s.isDob()));
        h.put("address", String.valueOf(s.isAddress()));
        h.put("typePermis", String.valueOf(s.isTypePermis()));
        h.put("preuveDomicile", String.valueOf(s.isPreuveDomicile()));
        h.put("preuveStatut", String.valueOf(s.isPreuveStatut()));
        h.put("photoClient", String.valueOf(s.isPhotoClient()));
        return h;
    }

    public String createServiceDescription() {
        String retVal = "Formulaire a remplir:\n";
        int iteration = 1;
        // SECTION FORMULAIRE
        if (nom) {
            retVal += "" + iteration + ". Nom:\n";
            iteration++;
        }
        if (dob) {
            retVal += "" + iteration + ". Date de Naissance:\n";
            iteration++;
        }
        if (address) {
            retVal += "" + iteration + ". Addresse:\n";
            iteration++;
        }
        if (typePermis) {
            retVal += "" + iteration + ". Type de Permis:\n";
        }
        // SECTION DOCUMENTS
        iteration = 1;
        retVal += "Documents necessaires:\n";
        if (preuveDomicile) {
            retVal += "" + iteration + ". Preuve de Domicile:\n";
            iteration++;
        }
        if (preuveStatut) {
            retVal += "" + iteration + ". Preuve de Statut:\n";
            iteration++;
        }
        if (photoClient) {
            retVal += "" + iteration + ". Photo:\n";
        }
        return retVal;
    }

    public ArrayList<String> createServiceRequestList() {
        ArrayList<String> list = new ArrayList<>();
        if (isNom()) {
            list.add("nom");
        }
        if (isDob()) {
            list.add("dob");
        }
        if (isAddress()) {
            list.add("address");
        }
        if (isTypePermis()) {
            list.add("typePermis");
        }
        if (isPreuveDomicile()) {
            list.add("preuveDomicile");
        }
        if (isPreuveStatut()) {
            list.add("preuveStatut");
        }
        if (isPhotoClient()) {
            list.add("photoClient");
        }
        return list;
    }

    public String createServiceString() {
        String string = new String();
        string += "serviceName:" + getServiceName() + "-" +
                "nom:" + isNom() + "-" +
                "dob:" + isDob() + "-" +
                "address:" + isAddress() + "-" +
                "typePermis:" + isTypePermis() + "-" +
                "preuveDomicile:" + isPreuveDomicile() + "-" +
                "preuveStatut:" + isPreuveStatut() + "-" +
                "photoClient:" + isPhotoClient();
        return string;
    }

    public static Service createServiceFromString(String s) {
        String serviceName = s.split("-")[0].split(":")[1];
        boolean nom = Boolean.parseBoolean(s.split("-")[1].split(":")[1]);
        boolean dob = Boolean.parseBoolean(s.split("-")[2].split(":")[1]);
        boolean address = Boolean.parseBoolean(s.split("-")[3].split(":")[1]);
        boolean typePermis = Boolean.parseBoolean(s.split("-")[4].split(":")[1]);
        boolean preuveDomicile = Boolean.parseBoolean(s.split("-")[5].split(":")[1]);
        boolean preuveStatut = Boolean.parseBoolean(s.split("-")[6].split(":")[1]);
        boolean photoClient = Boolean.parseBoolean(s.split("-")[7].split(":")[1]);
        return new Service(serviceName, nom, dob, address, typePermis, preuveDomicile, preuveStatut, photoClient);
    }


}
