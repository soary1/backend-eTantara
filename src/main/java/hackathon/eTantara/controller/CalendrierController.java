package hackathon.eTantara.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CalendrierController {

    public static class CalendarMonth {
        public String month;
        public List<String> events;

        public CalendarMonth(String month, List<String> events) {
            this.month = month;
            this.events = events;
        }
    }

    private static final List<String> MONTHS = Arrays.asList(
            "Janvier","Février","Mars","Avril","Mai","Juin",
            "Juillet","Août","Septembre","Octobre","Novembre","Décembre"
    );

    @GetMapping("/calendrier")
    public ResponseEntity<?> getCalendrier() {
        Path path = Paths.get("contenu", "calendrier_culturel.txt");
        if (!Files.exists(path)) {
            // try project root direct path
            path = Paths.get("calendrier_culturel.txt");
        }

        if (!Files.exists(path)) {
            return ResponseEntity.status(404).body("calendrier_culturel.txt not found");
        }

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            List<CalendarMonth> result = parseCalendar(lines);
            return ResponseEntity.ok(result);
        } catch (NoSuchFileException e) {
            return ResponseEntity.status(404).body("calendrier_culturel.txt not found");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur lecture fichier: " + e.getMessage());
        }
    }

    private List<CalendarMonth> parseCalendar(List<String> lines) {
        List<CalendarMonth> months = new ArrayList<>();
        String currentMonth = null;
        List<String> currentEvents = new ArrayList<>();

        for (String raw : lines) {
            String line = raw == null ? "" : raw.trim();
            if (line.isEmpty()) {
                continue;
            }

            // If the line is exactly a month name, start a new month
            if (MONTHS.contains(line)) {
                if (currentMonth != null) {
                    months.add(new CalendarMonth(currentMonth, new ArrayList<>(currentEvents)));
                    currentEvents.clear();
                }
                currentMonth = line;
            } else {
                // Otherwise treat as event
                if (currentMonth == null) {
                    // skip lines before first month
                    continue;
                }
                currentEvents.add(line);
            }
        }

        if (currentMonth != null) {
            months.add(new CalendarMonth(currentMonth, new ArrayList<>(currentEvents)));
        }

        return months.stream().collect(Collectors.toList());
    }
}
