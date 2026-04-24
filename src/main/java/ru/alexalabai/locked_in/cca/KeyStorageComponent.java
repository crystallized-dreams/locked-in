package ru.alexalabai.locked_in.cca;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KeyStorageComponent implements IKeyStorageComponent {
    List<String> keys=new ArrayList<>();
    @Override
    public boolean add(String id) {
        if(keys.contains(id)) return false;
        keys.add(id);
        return true;
    }
    @Override
    public boolean remove(String id) {
        if(!keys.contains(id)) return false;
        keys.remove(id);
        return true;
    }
    @Override
    public boolean contains(String id) {
        return keys.contains(id);
    }
    @Override
    public List<String> getAll() {
        return keys;
    }

    @Override
    public String create() {
        UUID uuid=UUID.randomUUID();
        while(keys.contains(uuid.toString())) uuid=UUID.randomUUID();
        String newId=uuid.toString();
        add(newId);
        return newId;
    }
}
