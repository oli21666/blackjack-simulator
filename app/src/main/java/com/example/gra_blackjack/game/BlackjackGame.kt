package com.example.gra_blackjack.game

import com.example.gra_blackjack.model.Deck
import com.example.gra_blackjack.model.Hand

/**
 * Fazy stanu rozgrywki w grze w Blackjacka.
 */
enum class GameState {
    /** Faza składania zakłądu i wyboru żetonów przed rozpoczęciem rozdania. */
    BETTING,

    /** Faza tury gracza (możliwość wykonania Hit, Stand, Double Down). */
    PLAYER_TURN,

    /** Faza tury krupiera (odkrycie karty i automatyczny dobór do min. 17 pkt). */
    DEALER_TURN,

    /** Koniec rozdania, ogłoszenie wyniku i rozliczenie salda. */
    GAME_OVER
}

/**
 * Ostateczny wynik rozdania gry.
 */
enum class RoundOutcome {
    /** Gracza uzyskał naturalnego Blackjacka (wygrana 3:2). */
    PLAYER_BLACKJACK,

    /** Gracz wygrał wyższą liczbą punktów niż krupier (wygrana 1:1). */
    PLAYER_WIN,

    /** Krupier uzyskał wyższą liczbę punktów (przegrana gracza). */
    DEALER_WIN,

    /** Krupier przekroczył 21 punktów (wygrana gracza 1:1). */
    DEALER_BUST,

    /** Remis – jednakowa liczba punktów (zwrot zakładu). */
    PUSH,

    /** Gracz przekroczył 21 punktów (przegrana gracza). */
    PLAYER_BUST
}

/**
 * Główny silnik kontrolujący zasady, stany rozgrywki oraz wirtualny portfel w grze w Blackjacka.
 *
 * @param numberOfDecks Liczba talii w bucie kasynowym (domyślnie 6 talii = 312 kart).
 */
class BlackjackGame(numberOfDecks: Int = 6) {

    /** Wirtualne saldo gracza (startowe 1000). */
    var balance: Int = 1000
        private set

    /** Stawka obecnego zakładu. */
    var currentBet: Int = 0
        private set

    /** Ręka gracza. */
    val playerHand = Hand()

    /** Ręka krupiera. */
    val dealerHand = Hand()

    private val deck = Deck(numberOfDecks)

    /** Liczba pozostałych kart w podajniku. */
    val remainingCardsInDeck: Int
        get() = deck.remainingCards

    /** Aktualny stan gry [GameState]. */
    var gameState: GameState = GameState.BETTING
        private set

    /** Określa, czy druga karta krupiera pozostaje zakryta. */
    var isDealerCardHidden: Boolean = true
        private set

    /** Ostatni rozstrzygnięty wynik rundy [RoundOutcome]. */
    var lastOutcome: RoundOutcome? = null
        private set

    /** Kwota przyznanej wygranej/wypłaty w ostatniej rundzie. */
    var payoutAmount: Int = 0
        private set

    /**
     * Dodaje podaną kwotę żetonu do aktualnego zakładu.
     *
     * @param amount Kwota do dodania (np. 10, 25, 100, 500).
     * @return `true` jeśli zakład został pomyślnie dodany (zakład nie przekracza salda), `false` w przeciwnym razie.
     */
    fun addBet(amount: Int): Boolean {
        if ((gameState != GameState.BETTING) && (gameState != GameState.GAME_OVER)) return false
        if (currentBet + amount <= balance) {
            currentBet += amount
            return true
        }
        return false
    }

    /**
     * Zeruje aktualnie obstawiony zakład.
     */
    fun clearBet() {
        if (gameState == GameState.BETTING || gameState == GameState.GAME_OVER) {
            currentBet = 0
        }
    }

    /**
     * Odnawia saldo gracza do $1000 w przypadku utraty wszystkich środków.
     */
    fun resetBalanceIfZero() {
        if (balance <= 0) {
            balance = 1000
            currentBet = 0
            gameState = GameState.BETTING
        }
    }

    /**
     * Rozpoczyna nowe rozdanie: pobiera zakład z salda, rozdaje po 2 karty dla gracza i krupiera.
     * Sprawdza również wystąpienie natychmiastowych Blackjacków.
     *
     * @return `true` jeśli rozdanie zostało pomyślnie rozpoczęte, `false` w przypadku braku zakładu/środków.
     */
    fun startDeal(): Boolean {
        if (balance <= 0) {
            resetBalanceIfZero()
            return false
        }

        if (currentBet !in 1..balance) {
            return false
        }

        balance -= currentBet
        playerHand.clear()
        dealerHand.clear()
        lastOutcome = null
        payoutAmount = 0
        isDealerCardHidden = true

        playerHand.addCard(deck.drawCard())
        dealerHand.addCard(deck.drawCard())
        playerHand.addCard(deck.drawCard())
        dealerHand.addCard(deck.drawCard())

        if (playerHand.isBlackjack() || dealerHand.isBlackjack()) {
            isDealerCardHidden = false
            resolveInstantBlackjack()
        } else {
            gameState = GameState.PLAYER_TURN
        }
        return true
    }

    /**
     * Akcja "Dobierz" (Hit) – gracz dociąga dodatkową kartę.
     * Jeśli gracz przekroczy 21 punktów, następuje automatyczna przegrana (Fura).
     */
    fun hit() {
        if (gameState != GameState.PLAYER_TURN) return

        playerHand.addCard(deck.drawCard())
        if (playerHand.isBust()) {
            isDealerCardHidden = false
            lastOutcome = RoundOutcome.PLAYER_BUST
            payoutAmount = 0
            gameState = GameState.GAME_OVER
        }
    }

    /**
     * Akcja "Pasuj" (Stand) – gracz kończy swoją turę i przekazuje ruch krupierowi.
     */
    fun stand() {
        if (gameState != GameState.PLAYER_TURN) return

        gameState = GameState.DEALER_TURN
        isDealerCardHidden = false
    }

    /**
     * Akcja "Podwój" (Double Down) – podwaja stawkę zakładu, dociąga dokładnie 1 kartę dla gracza
     * i przekazuje ruch krupierowi (lub kończy grę w przypadku fury).
     *
     * @return `true` jeśli podwojenie było możliwe i zostało wykonane, `false` w przeciwnym razie.
     */
    fun doubleDown(): Boolean {
        if (gameState != GameState.PLAYER_TURN) return false
        if (playerHand.cards.size != 2) return false
        if (balance < currentBet) return false

        balance -= currentBet
        currentBet *= 2

        playerHand.addCard(deck.drawCard())
        isDealerCardHidden = false

        if (playerHand.isBust()) {
            lastOutcome = RoundOutcome.PLAYER_BUST
            payoutAmount = 0
            gameState = GameState.GAME_OVER
        } else {
            gameState = GameState.DEALER_TURN
        }
        return true
    }

    /**
     * Wykonuje pojedynczy krok tury krupiera.
     * Krupier musi dobierać karty dopóki suma jego punktów wynosi mniej niż 17.
     *
     * @return `true` jeśli krupier dobrał kartę i wymaga kolejnego kroku, `false` jeśli ruch krupiera dobiegł końca.
     */
    fun dealerStep(): Boolean {
        if (gameState != GameState.DEALER_TURN) return false

        return if (dealerHand.calculateScore() < 17) {
            dealerHand.addCard(deck.drawCard())
            true
        } else {
            finalizeDealerTurn()
            false
        }
    }

    private fun resolveInstantBlackjack() {
        gameState = GameState.GAME_OVER
        val playerBj = playerHand.isBlackjack()
        val dealerBj = dealerHand.isBlackjack()

        when {
            playerBj && dealerBj -> {
                lastOutcome = RoundOutcome.PUSH
                payoutAmount = currentBet
                balance += payoutAmount
            }
            playerBj -> {
                lastOutcome = RoundOutcome.PLAYER_BLACKJACK
                payoutAmount = currentBet + (currentBet * 3 / 2)
                balance += payoutAmount
            }
            else -> {
                lastOutcome = RoundOutcome.DEALER_WIN
                payoutAmount = 0
            }
        }
    }

    private fun finalizeDealerTurn() {
        gameState = GameState.GAME_OVER
        val playerScore = playerHand.calculateScore()
        val dealerScore = dealerHand.calculateScore()

        when {
            dealerHand.isBust() -> {
                lastOutcome = RoundOutcome.DEALER_BUST
                payoutAmount = currentBet * 2
                balance += payoutAmount
            }
            playerScore > dealerScore -> {
                lastOutcome = RoundOutcome.PLAYER_WIN
                payoutAmount = currentBet * 2
                balance += payoutAmount
            }
            dealerScore > playerScore -> {
                lastOutcome = RoundOutcome.DEALER_WIN
                payoutAmount = 0
            }
            else -> {
                lastOutcome = RoundOutcome.PUSH
                payoutAmount = currentBet
                balance += payoutAmount
            }
        }
    }

    /**
     * Resetuje stan gry do fazy obstawiania [GameState.BETTING] przed nowym rozdaniem.
     */
    fun resetForNextRound() {
        if (gameState == GameState.GAME_OVER) {
            gameState = GameState.BETTING
            if (currentBet > balance) {
                currentBet = if (balance > 0) balance else 0
            }
        }
    }
}
