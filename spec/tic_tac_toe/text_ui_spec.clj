(ns tic-tac-toe.text-ui-spec
  (:use [speclj.core]
        [tic-tac-toe.text-ui]
        [tic-tac-toe.board :only [init-board]]
        [clojure.string :only [join]]))

(defmacro no-out
  "Swallows output to *out* so we can test without noise on std out."
  [& body]
  `(let [s# (new java.io.StringWriter)]
     (binding [*out* s#]
       ~@body)))

(defn moves-str [& moves]
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
  "Formatting a cell on the board for display"
  (it "returns spaces for a nil spot on the board"
      (should= "   " (format-cell nil)))

  (it "returns an uppercase character for a keyword"
      (should= " X " (format-cell :x))
      (should= " O " (format-cell :o))))

(describe
  "Formatting the board"
  (it "represents the current state"
      (should= "XXXOXOOOO"
               (apply str 
                      (filter #{\X \O} 
                              (format-board [:x :x :x :o :x :o :o :o :o]))))))

(describe 
  "Printing messages"
  (it "displays the winning message"
      (should (re-seq #"X has won the game"
                      (with-out-str
                        (print-results [:x :x :x nil nil nil nil nil nil])))))

  (it "displays the tie message"
      (should (re-seq #"The game has ended in a tie"
                      (with-out-str
                        (print-results [:x :o :x
                                                :o :x :o
                                                :o :x :o]))))))

(describe 
  "Reading input"
  (it "reads until it gets a number" 
      (should= 8 (no-out (with-in-str (moves-str "A" "B" "C" "D" 9) 
                           (read-until-valid (init-board) :x)))))

  (it "reads until the number is in range" 
      (should= 4 (no-out (with-in-str (moves-str 11 16 17 23 12312 5) 
                           (read-until-valid (init-board) :x)))))

  (it "reads until the number is a valid move" 
      (should= 3 (no-out (with-in-str (moves-str 1 2 3 4) 
                           (read-until-valid [:x :x :x nil nil nil nil nil nil] :x)))))

  (it "decrements the number" 
      (should= 0 (no-out (with-in-str (moves-str 1) 
                           (read-until-valid (init-board) :x)))))

  (it "prompts the player each time through the loop"
      (should= 3 (count (re-seq #"Please select a move for 'X' \[1-9\]"
                                (with-out-str
                                  (with-in-str
                                    (moves-str "Z" "X" 1)
                                    (read-until-valid (init-board) :x)))))))

  (it "informs the player of invalid moves"
      (should= 2 (count (re-seq #"Invalid move"
                                (with-out-str
                                  (with-in-str
                                    (moves-str "Z" "X" 1)
                                    (read-until-valid (init-board) :x))))))))

(describe 
  "Playing different game types"
  (it "prompts the player to choose X or O"
      (let [output (with-out-str
                     (with-in-str (moves-str 1 1 9 6) (game)))]
        (should (re-seq #"Select game type:" output))
        (should (re-seq #"1\) Human    vs. Computer" output))
        (should (re-seq #"2\) Computer vs. Human" output))
        (should (re-seq #"3\) Computer vs. Computer" output))))

  (it "displays the correct move prompt when playing as X"
      (let [output (with-out-str
                     (with-in-str (moves-str 1 1 9 6) (game)))]
        (should (re-seq #"Please select a move for 'X'" output))))

  (it "displays the correct move prompt when playing as O"
      (let [output (with-out-str
                     (with-in-str (moves-str 2 2 1 9 6) (game)))]
        (should (re-seq #"Please select a move for 'O'" output))))


  (describe
    "Picking the player"
    (it "sets players correctly when the person chooses to play as X"
        (let [[player1 player2] (no-out (with-in-str "1\n" (get-players)))]
          (should= {:mark :x :mover move-human}    player1)
          (should= {:mark :o :mover move-computer} player2)))

    (it "sets players correctly when the person chooses to play as O"
        (let [[player1 player2] (no-out (with-in-str "2\n" (get-players)))]
          (should= {:mark :x :mover move-computer} player1)
          (should= {:mark :o :mover move-human}    player2)))

    (it "sets players correctly when the person chooses computer vs computer "
        (let [[player1 player2] (no-out (with-in-str "3\n" (get-players)))]
          (should= {:mark :x :mover move-computer-and-display} player1)
          (should= {:mark :o :mover move-computer-and-display} player2)))))


(describe 
  "Playing"
  (it "validates the input"
      (game-with-input-should (moves-str 1 "A" 1 9 6) #"Invalid move")
      (game-with-input-should (moves-str 1 14 1 9 6) #"Invalid move")
      (game-with-input-should (moves-str 1 1 1 9 6) #"Invalid move"))

  (it "shows the winner"
      (game-with-input-should (moves-str 2 1 9 6) #"X has won the game!")) 

  (it "shows a tie message"
      (game-with-input-should (moves-str 1 1 9 8 3 4) #"The game has ended in a tie."))
  
  
  )


