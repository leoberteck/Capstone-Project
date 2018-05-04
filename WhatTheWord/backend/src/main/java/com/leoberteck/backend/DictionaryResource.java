package com.leoberteck.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Api(
    name = "wtwapi",
    version = "v1",
    namespace = @ApiNamespace(
        ownerDomain = "backend.leoberteck.com",
        ownerName = "backend.leoberteck.com"
    )
)
public class DictionaryResource {

    private final Gson gson = new GsonBuilder().create();
    private final List<WordEntry> dictionary = new ArrayList<>();
    private final Random random = new Random();

    @ApiMethod(name = "word")
    public WordEntry getRandomWord(@Named("exceptions") @Nullable Collection<Integer> exceptions) throws FileNotFoundException {
        if(dictionary.isEmpty()){
            init();
        }
        List<WordEntry> filtered = dictionary.stream()
            .filter(wordEntry -> exceptions == null || !exceptions.contains(wordEntry.get_id()))
            .collect(Collectors.toList());
        return filtered.get(random.nextInt(filtered.size()));
    }

    private void init() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File("WEB-INF/dictionary.json"));
        String result = new BufferedReader(new InputStreamReader(inputStream))
                .lines().parallel().collect(Collectors.joining(""));
        dictionary.addAll(gson.fromJson(result, new TypeToken<List<WordEntry>>(){}.getType()));
    }
}