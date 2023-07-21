package ru.mrsu.springmongodb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClientNoId {
    String name;
    String number;

    public boolean isEmpty() {
        return (name == null || name.equals("")) &&
                (number == null || number.equals(""));
    }
}
