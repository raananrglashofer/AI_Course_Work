package edu.yu.introtoalgs;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
public class OctopusCount implements OctopusCountI {
    private Set<Octopus> Octopuses = new HashSet<>();
    public OctopusCount() {
    }

    private class Leg {
        private ArmColor color;
        private int length = 0;
        private ArmTexture texture;

        private Leg(ArmColor color, int length, ArmTexture texture) {
            this.color = color;
            this.length = length;
            this.texture = texture;
        }

        @Override
        public boolean equals(Object obj){
            if (this == obj){
                return true;
            }
            if (obj == null || getClass() != obj.getClass()){
                return false;
            }
            Leg otherLeg = (Leg) obj;
            return this.hashCode() == otherLeg.hashCode();
        }

        @Override
        public int hashCode(){
            return Objects.hash(color, length, texture);
        }
    }

    private class Octopus {
        private Set<Leg> legs = new HashSet<>();
        private int observationId;
        private Octopus(int observationId) {

            this.observationId = observationId;
        }

        @Override
        public boolean equals(Object obj){
            if (this == obj){
                return true;
            }
            if (obj == null || getClass() != obj.getClass()){
                return false;
            }
            Octopus otherOctopus = (Octopus) obj;
            return this.legs.equals(otherOctopus.legs);
        }
        @Override
        public int hashCode(){
            return this.legs.hashCode();
        }
    }

    // First, going to check if all arguments are correct format
    // Then create a Leg object for each leg and add it to that octopus from the observation
    // Close the for loop, and add the new octopus to the Set of all Octopus
    @Override
    public void addObservation(int observationId, ArmColor[] colors, int[] lengthInCM, ArmTexture[] textures) {
        if (observationId < 0) {
            throw new IllegalArgumentException();
        }
        Octopus octopus = new Octopus(observationId);
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] == null || lengthInCM[i] < 1 || textures[i] == null) {
                throw new IllegalArgumentException();
            }
            Leg leg = new Leg(colors[i], lengthInCM[i], textures[i]);
            octopus.legs.add(leg);
        }
        this.Octopuses.add(octopus);
    }

    @Override
    public int countThem() {
        return this.Octopuses.size();
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        OctopusCount otherOctopusCount = (OctopusCount) obj;
        return this.Octopuses.equals(otherOctopusCount.Octopuses);
    }

    @Override
    public int hashCode(){
        return this.Octopuses.hashCode();
    }
}
