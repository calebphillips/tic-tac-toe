(ns tic-tac-toe.text-ui-spec
  (:use [speclj.core]
        [tic-tac-toe.text-ui]
        [tic-tac-toe.board :only [init-board]]
        [clojure.string :only [join]]))

(defn to-input [& moves]
  (join "\n" moves))

(defn game-with-input-should [input expected-out]
  (should (re-seq expected-out
                  (with-out-str
                    (with-in-str input
                      (game))))))

(describe
  "format-player"
  (it "displays the upper case player name"
      (should= "X" (format-player :x))
      (should= "O" (format-player :o))))


(describe
  "Formatting the board"
  (it "represents the current state"
      (should= "XXXOXOOOO"
               (apply str 
                      (filter #{\X \O} 
                              (format-board [:x :x :x :o :x :o :o :o :o]))))))

(describe 
  "Selecting whether to play as X or O"
  (it "prompts the player to choose X or O"
      (let [output (with-out-str
                     (with-in-str (to-input 1 1 9 6) (game)))]
        (should (re-seq #"Would you like to play as:" output))
        (should (re-seq #"1\) X" output))
        (should (re-seq #"2\) O" output))
        (should (re-seq #"3\) Neither" output))))

  (it "displays the correct move prompt when playing as X"
      (let [output (with-out-str
                     (with-in-str (to-input 1 1 9 6) (game)))]
        (should (re-seq #"Please select a move for 'X'" output))))

  (it "displays the correct move prompt when playing as O"
      (let [output (with-out-str
                     (with-in-str (to-input 2 2 1 9 6) (game)))]
        (should (re-seq #"Please select a move for 'O'" output)))))

(describe
  "Picking the player"
  (binding [*out* (new java.io.StringWriter)]
    (it "sets players correctly when the person chooses to play as X"
        (let [[player1 player2] (with-in-str "1\n" (get-players))]
          (should= :x (:mark player1))
          (should= move-human (:mover player1)) 
          (should= :o (:mark player2))
          (should= move-computer (:mover player2))))

    (it "sets players correctly when the person chooses to play as O"
        (let [[player1 player2] (with-in-str "2\n" (get-players))]
          (should= :x (:mark player1))
          (should= move-computer (:mover player1))
          (should= :o (:mark player2))
          (should= move-human (:mover player2))))

    (it "sets players correctly when the person chooses computer vs computer "
        (let [[player1 player2] (with-in-str "3\n" (get-players))]
          (should= :x (:mark player1))
          (should= move-computer (:mover player1))
          (should= :o (:mark player2))
          (should= move-computer (:mover player2))))))

(describe 
  "Playing"
  (it "validates the input"
      (game-with-input-should (to-input 1 "A" 1 9 6) #"Invalid move")
      (game-with-input-should (to-input 1 14 1 9 6) #"Invalid move")
      (game-with-input-should (to-input 1 1 1 9 6) #"Invalid move"))

  (it "shows the winner"
      (game-with-input-should (to-input 2 1 9 6) #"X has won the game!")) 

  (it "shows a tie message"
      (game-with-input-should (to-input 1 1 9 8 3 4) #"The game has ended in a tie.")))

(describe 
  "Reading input"
  (binding [*out* (new java.io.StringWriter)]
    (it "reads until it gets a number" 
        (should= 8 (with-in-str (to-input "A" "B" "C" "D" 9) (read-until-valid (init-board) :x))))

    (it "reads until the number is in range" 
        (should= 4 (with-in-str (to-input 11 16 17 23 12312 5) (read-until-valid (init-board) :x))))

    (it "reads until the number is a valid move" 
        (should= 3 (with-in-str (to-input 1 2 3 4) (read-until-valid [:x :x :x nil nil nil nil nil nil] :x))))

    (it "decrements the number" 
        (should= 0 (with-in-str (to-input 1) (read-until-valid (init-board) :x))))
    )

  (it "prompts the player each time through the loop"
      (should= 3 (count (re-seq #"Please select a move for 'X' \[1-9\]"
                                (with-out-str
                                  (with-in-str
                                    (to-input "Z" "X" 1)
                                    (read-until-valid (init-board) :x)))))))

  (it "informs the player of invalid moves"
      (should= 2 (count (re-seq #"Invalid move"
                                (with-out-str
                                  (with-in-str
                                    (to-input "Z" "X" 1)
                                    (read-until-valid (init-board) :x)))))))
  )

(describe 
  "Printing messages"
  (it "displays the winning message"
      (should (re-seq #"X has won the game"
                      (with-out-str
                        (print-status-messages [:x :x :x nil nil nil nil nil nil])))))

  (it "displays the tie message"
      (should (re-seq #"The game has ended in a tie"
                      (with-out-str
                        (print-status-messages [:x :o :x
                                                :o :x :o
                                                :o :x :o]))))))
(describe
  "Formatting a cell on the board for display"
  (it "returns spaces for a nil spot on the board"
      (should= "   " (format-cell nil)))

  (it "returns an uppercase character for a keyword"
      (should= " X " (format-cell :x))
      (should= " O " (format-cell :o))))

