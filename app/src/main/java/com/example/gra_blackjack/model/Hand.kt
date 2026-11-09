package com.example.gra_blackjack.model

/**
 * Reprezentuje rękę (zbiór kart) gracza lub krupiera w grze w Blackjacka.
 * Odpowiada za przechowywanie kart oraz przeliczanie punktacji z uwzględnieniem elastycznej wartości Asa (1 lub 11).
 */
class Hand {
    /**
     * Lista kart znajdujących się obecnie na ręce.
     */
    val cards = mutableListOf<Card>()

    /**
     * Dodaje kartę do ręki.
     *
     * @param card Karta do dodania.
     */
    fun addCard(card: Card) {
        cards.add(card)
    }

    /**
     * Czyszczenie ręki przed rozpoczęciem nowego rozdania.
     */
    fun clear() {
        cards.clear()
    }

    /**
     * Oblicza łączną wartość punktową kart na ręce.
     *
     * Jeśli suma punktów przekracza 21, wartość Asa jest automatycznie redukowana z 11 na 1 pkt (odjęcie 10 pkt),
     * dopóki suma nie spadnie do 21 lub nie zabraknie Asów.
     *
     * @return Łączna suma punktów.
     */
    fun calculateScore(): Int {
        var score = cards.sumOf { it.rank.baseValue }
        var aceCount = cards.count { it.rank == Rank.ACE }

        while (score > 21 && aceCount > 0) {
            score -= 10
            aceCount--
        }
        return score
    }

    /**
     * Sprawdza, czy gracz przekroczył 21 punktów (tzw. "Bust" / "Fura").
     *
     * @return `true` jeśli suma punktów jest większa niż 21, w przeciwnym razie `false`.
     */
    fun isBust(): Boolean = calculateScore() > 21

    /**
     * Sprawdza, czy ręka stanowi naturalnego Blackjacka (dokładnie 2 karty dające 21 punktów).
     *
     * @return `true` jeśli ręka zawiera 2 karty dające 21 punktów, w przeciwnym razie `false`.
     */
    fun isBlackjack(): Boolean = cards.size == 2 && calculateScore() == 21
}
