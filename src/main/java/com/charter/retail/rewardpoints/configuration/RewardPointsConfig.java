package com.charter.retail.rewardpoints.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "reward.points")
public class RewardPointsConfig {
    private Threshold threshold = new Threshold();
    private Points points = new Points();

    public static class Threshold {
        private int first;
        private int second;

        public int getFirst() { return first; }
        public void setFirst(int first) { this.first = first; }
        public int getSecond() { return second; }
        public void setSecond(int second) { this.second = second; }
    }

    public static class Points {
        private int first;
        private int second;

        public int getFirst() { return first; }
        public void setFirst(int first) { this.first = first; }
        public int getSecond() { return second; }
        public void setSecond(int second) { this.second = second; }
    }

    public Threshold getThreshold() { return threshold; }
    public void setThreshold(Threshold threshold) { this.threshold = threshold; }
    public Points getPoints() { return points; }
    public void setPoints(Points points) { this.points = points; }
}
