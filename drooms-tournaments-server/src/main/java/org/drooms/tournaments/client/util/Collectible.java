package org.drooms.tournaments.client.util;

import java.util.Map;

public class Collectible {
    private final String id;
    private int price;
    private int expiration;
    private double probability;

    public Collectible(String id) {
        this.id = id;
    }

    public Collectible(String id, int price, int expiration, double probability) {
        this.id = id;
        this.price = price;
        this.expiration = expiration;
        this.probability = probability;
    }

    public Collectible(String id, Map<String, String> configuration) {
        this.id = id;

        this.price = Integer.parseInt(configuration.get("collectible.price." + id));
        this.expiration = Integer.parseInt(configuration.get("collectible.expiration." + id));
        this.probability = Double.parseDouble(configuration.get("collectible.probability." + id));
    }

    public String getId() {
        return id;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public void addToConfiguration(Map<String, String> configuration) {
        String collectibles = configuration.get("collectibles");
        configuration.put("collectibles", (collectibles != null ? collectibles + "," : "") + id);
        configuration.put("collectible.price." + id, Integer.toString(price));
        configuration.put("collectible.expiration." + id, Integer.toString(expiration));
        configuration.put("collectible.probability." + id, Double.toString(probability));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Collectible other = (Collectible) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
