package com.example.gra_blackjack.model

/**
 * Reprezentuje podajnik/but kasynowy (shoe) zawierający określoną liczbę talii kart.
 *
 * Wykorzystuje algorytm tasowania Fishera-Yatesa oraz regułę karty przecinającej
 * (cut card rule) do automatycznego przetasowania podajnika po zużyciu ok. 80% kart.
 *
 * @property numberOfDecks Liczba talii 52-kartowych w bucie kasynowym (domyślnie 6 talii = 312 kart).
 */
class Deck(val numberOfDecks: Int = 6) {
    private val cards = mutableListOf<Card>()

    /**
     * Liczba pozostałych kart w podajniku.
     */
    val remainingCards: Int
        get() = cards.size

    /**
     * Całkowita pojemność podajnika (liczba talii * 52).
     */
    val totalCards: Int
        get() = numberOfDecks * 52

    init {
        resetAndShuffle()
    }

    /**
     * Czyści podajnik, generuje nowe talie kart dla wszystkich rang i kolorów, a następnie losowo je tasuje.
     */
    fun resetAndShuffle() {
        cards.clear()
        repeat(numberOfDecks) {
            for (suit in Suit.entries) {
                for (rank in Rank.entries) {
                    cards.add(Card(rank, suit))
                }
            }
        }
        cards.shuffle()
    }

    /**
     * Wyciąga jedną kartę z podajnika.
     *
     * Jeśli w podajniku zostało mniej niż 20% kart (zasada cut card), podajnik jest automatycznie
     * resetowany i przetasowywany przed wydaniem karty.
     *
     * @return Wyciągnięta karta [Card].
     */
    fun drawCard(): Card {
        // Cut card rule: automatyczne przetasowanie gdy zostanie mniej niż 20% kart w bucie
        if (cards.size < (totalCards * 0.2)) {
            resetAndShuffle()
        }
        return cards.removeAt(cards.size - 1)
    }
}
