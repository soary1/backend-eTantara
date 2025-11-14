// Implementing cultural calendar events in the site
const culturalCalendar = [
    { month: 'Janvier', events: [
        'Fomban-drazana : Fidirana amin’ny taona vaovao (rites familiaux)',
        'Famadihana préparations (selon régions)',
    ] },
    { month: 'Février', events: [
        'Tsakitsaky sy vary amin’anana (saison des légumes-feuilles)',
        'Traditions de pluie : fombafomba de demande de pluie en zone Bara & Antandroy',
    ] },
    { month: 'Mars', events: [
        'Fanokanana trano (bénédictions traditionnelles)',
        'Festival Donia (selon année)',
    ] },
    // ...additional months and events...
];

// Function to display cultural calendar on the site
function displayCulturalCalendar() {
    const calendarContainer = document.getElementById('cultural-calendar');
    if (calendarContainer) { // Check if calendarContainer is not null
        culturalCalendar.forEach(month => {
            const monthElement = document.createElement('div');
            monthElement.innerHTML = `<h3>${month.month}</h3><ul>${month.events.map(event => `<li>${event}</li>`).join('')}</ul>`;
            calendarContainer.appendChild(monthElement);
        });
    }
}

// Call the function to display the calendar
displayCulturalCalendar();