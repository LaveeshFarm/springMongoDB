package ru.mrsu.springmongodb.model;

import lombok.Value;
import org.bson.types.ObjectId;

public enum ClientDTO{;
    private interface Id {ObjectId getId();}
    private interface Name {String getName();}
    private interface Number {String getNumber();}

    @Value
    public static class ClientNoId implements Name, Number {
        String name;
        String number;

        public boolean isEmpty() {
            return (name == null || name.equals("")) &&
                    (number == null || number.equals(""));
        }
    }
}
